package blog_spring.blog_spring.controller;

import blog_spring.blog_spring.model.Order;
import blog_spring.blog_spring.model.OrderDetail;
import blog_spring.blog_spring.repository.OrderDetailRepository;
import blog_spring.blog_spring.repository.OrderRepository;
import blog_spring.blog_spring.repository.ProductRepository;
import blog_spring.blog_spring.repository.UserRepository;
import blog_spring.blog_spring.service.OrderService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionListLineItemsParams;
import com.stripe.param.checkout.SessionRetrieveParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/webhook")
public class StripeWebhookController {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        String endpointSecret = "whsec_5f78a4e52f1ae38ecdac69fb532634cee83ecf7dc33ad4dec55677694d545b98"; // Replace with your endpoint's secret
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }
        // Xác định loại sự kiện
        String eventType = event.getType();

        if ("charge.succeeded".equals(eventType)) {
            handleCheckoutSessionCompleted(event);
        } else if ("checkout.session.completed".equals(eventType)) {
            handleCheckoutSessionCompleted(event);
        }

        return ResponseEntity.ok("Received");
    }

    private void handleCheckoutSessionCompleted(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = dataObjectDeserializer.getObject().orElse(null);

        if (stripeObject instanceof Session) {
            Session session = (Session) stripeObject;
            Map<String, String> metadata = session.getMetadata();

            try {
                // Lấy chi tiết line items từ session
                Session sessionWithLineItems = Session.retrieve(session.getId());

                SessionListLineItemsParams params = SessionListLineItemsParams.builder().build();
                LineItemCollection lineItems = sessionWithLineItems.listLineItems(params);

                // Lấy chuỗi JSON từ metadata và chuyển đổi lại thành Map
                String productIdQuantityJson = metadata.get("productIdQuantity");
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Integer> productIdQuantityMap = mapper.readValue(productIdQuantityJson, new TypeReference<Map<String, Integer>>() {});

                // Tạo đơn hàng từ session và line items
                createOrderFromSession(session, lineItems, metadata, productIdQuantityMap);
            } catch (StripeException | IOException e) {
                e.printStackTrace();
            }
        }
    }
    private Order createOrderFromSession(Session session, LineItemCollection lineItems, Map<String, String> metadata, Map<String, Integer> productIdQuantityMap) {
        Order order = new Order();

        var findUser = userRepository.findById(metadata.get("userId")).get();
        if ( findUser.getId() != null ) {
            order.setUser(findUser);
        }

        order.setEmail(metadata.get("email"));
        order.setPhone(metadata.get("phone"));
        order.setShippingAddress(metadata.get("address"));
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());

        List<OrderDetail> orderDetails = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : productIdQuantityMap.entrySet()) {
            String productId = entry.getKey();
            int quantity = entry.getValue();

            OrderDetail orderDetail = new OrderDetail();

            var findProduct = productRepository.findById(productId).get();
            if ( findProduct.getId() != null ) {

                findProduct.setStock_quantity(findProduct.getStock_quantity() - quantity);
                findProduct.setSold(findProduct.getSold() + quantity);
                var savedFindProduct = productRepository.save(findProduct);
                orderDetail.setProduct(savedFindProduct);
            }

            orderDetail.setQuantity(quantity);
            orderDetails.add(orderDetail);
        }

        var savedOrderDetails = orderDetailRepository.saveAll(orderDetails);

        order.setDetails(savedOrderDetails);
        order.setOrderDate(new Date());
        order.setTotalAmount((long) session.getAmountTotal());
        order.setStatus("paid");

        var savedOrder = orderRepository.save(order);
        return savedOrder;
    }
}

