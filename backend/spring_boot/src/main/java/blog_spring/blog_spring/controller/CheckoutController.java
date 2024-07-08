package blog_spring.blog_spring.controller;

import blog_spring.blog_spring.model.Cart;
import blog_spring.blog_spring.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.lang.Nullable;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class CheckoutController {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostMapping("/create-checkout-list-session")
    public Map<String, String> createCheckoutListSession(@RequestBody Map<String, Object> data,
            @RequestParam @Nullable String voucher) throws StripeException {

        Stripe.apiKey = stripeApiKey;

        List<Map<String, Object>> cartItems = (List<Map<String, Object>>) data.get("cartItems");
        Map<String, Object> profileInfo = (Map<String, Object>) data.get("profileInfo");

        String userId = (String) profileInfo.get("id");
        String email = (String) profileInfo.get("email");
        String address = (String) profileInfo.get("address");
        String phone = (String) profileInfo.get("phone");

        var totalAmount = 0;

        // Tạo danh sách các line items cho session thanh toán
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
        Map<String, Integer> productIdQuantityMap = new HashMap<>();
        for (Map<String, Object> product : cartItems) {
            String productId = (String) product.get("id");
            int quantity = (int) product.get("quantity");
            int price = (int) product.get("price");
            totalAmount += price;
            String name = (String) product.get("name");

            lineItems.add(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity((long) quantity)
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setUnitAmount((long) price)
                                            .setCurrency("vnd")
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName(name)
                                                            .build())
                                            .build())
                            .build());
            productIdQuantityMap.put(productId, quantity);
        }

        // Chuyển đổi Map productId và số lượng thành chuỗi JSON
        ObjectMapper mapper = new ObjectMapper();
        String productIdQuantityJson = "";
        try {
            productIdQuantityJson = mapper.writeValueAsString(productIdQuantityMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // Tạo session Stripe

        String couponId = "";
        if (voucher != null && !voucher.isEmpty() && !voucher.equals("null")) {
            couponId = voucher;
        }

        // Tạo session Stripe
        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/success")
                .setCancelUrl("http://localhost:3000")
                .addAllLineItem(lineItems)
                .putMetadata("userId", userId)
                .putMetadata("email", email)
                .putMetadata("address", address)
                .putMetadata("phone", phone)
                .putMetadata("productIdQuantity", productIdQuantityJson);
        if (voucher != null && !voucher.isEmpty() && !voucher.equals("null")) {
            paramsBuilder.putMetadata("voucher", voucher);
        }

        // Áp dụng couponId nếu có
        if (!couponId.isEmpty()) {
            paramsBuilder.addAllDiscount(
                    Arrays.asList(SessionCreateParams.Discount.builder().setCoupon(couponId).build()));
        }

        SessionCreateParams params = paramsBuilder.build();
        Session session = Session.create(params);

        Map<String, String> response = new HashMap<>();
        response.put("sessionId", session.getId());

        return response;
    }
}
