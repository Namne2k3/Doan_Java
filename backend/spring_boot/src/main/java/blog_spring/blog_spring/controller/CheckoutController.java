package blog_spring.blog_spring.controller;

import blog_spring.blog_spring.model.Cart;
import blog_spring.blog_spring.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CheckoutController {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostMapping("/create-checkout-list-session")
    public Map<String, String> createCheckoutListSession(@RequestBody Map<String, Object> data) throws StripeException {

        Stripe.apiKey = stripeApiKey;

        List<Map<String, Object>> cartItems = (List<Map<String, Object>>) data.get("cartItems");
        Map<String, Object> profileInfo = (Map<String, Object>) data.get("profileInfo");

        String userId = (String) profileInfo.get("id");
        String email = (String) profileInfo.get("email");
        String address = (String) profileInfo.get("address");
        String phone = (String) profileInfo.get("phone");

        // Tạo danh sách các line items cho session thanh toán
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
        Map<String, Integer> productIdQuantityMap = new HashMap<>();
        for (Map<String, Object> product : cartItems) {
            String productId = (String) product.get("id");
            int quantity = (int) product.get("quantity");
            int price = (int) product.get("price");
            String name = (String) product.get("name");

            lineItems.add(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity((long) quantity)
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setUnitAmount((long) price )
                                            .setCurrency("vnd")
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName(name)
                                                            .build())
                                            .build())
                            .build()
            );
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
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/success") // URL sau khi thanh toán thành công
                .setCancelUrl("http://localhost:3000/cancel")  // URL nếu thanh toán bị hủy
                .addAllLineItem(lineItems)
                .putMetadata("userId", userId)
                .putMetadata("email", email)
                .putMetadata("address", address)
                .putMetadata("phone", phone)
                .putMetadata("productIdQuantity", productIdQuantityJson)
                .build();

        Session session = Session.create(params);

        Map<String, String> response = new HashMap<>();
        response.put("sessionId", session.getId());

        return response;
    }


    @PostMapping("/create-checkout-session")
    public Map<String, String> createCheckoutSession(@RequestBody Map<String, Object> data) throws StripeException {

        Stripe.apiKey = stripeApiKey;
        // Lấy thông tin sản phẩm từ request body
        String id = (String) data.get("id");
        Map<String, Object> product = (Map<String, Object>) data.get("product");
        Map<String, Object> profileInfo = (Map<String, Object>) data.get("profileInfo");
        String productId = (String) product.get("id");
        int quantity = (int)data.get("quantity");
        int price = (int) product.get("price");
        String name = (String) product.get("name");
        String userId = (String) profileInfo.get("id");
        String email = (String) profileInfo.get("email");
        String address = (String) profileInfo.get("address");
        // Tạo session tạo Stripe
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/myorder") // URL sau khi thanh toán thành công
                .setCancelUrl("http://localhost:3000/cancel")  // URL nếu thanh toán bị hủy
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity((long)quantity)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setUnitAmount(Long.valueOf(price + 30000))
                                                .setCurrency("vnd")
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(name)
                                                                .build())
                                                .build())
                                .build())
                .putMetadata("productId",productId)
                .putMetadata("email", email)
                .putMetadata("address", address)
                .putMetadata("userId", userId)
                .putMetadata("price", String.valueOf(price))
                .putMetadata("quantity", String.valueOf(quantity))
                .build();

        Session session = Session.create(params);

        Map<String, String> response = new HashMap<>();
        response.put("sessionId", session.getId());

        return response;
    }
}
