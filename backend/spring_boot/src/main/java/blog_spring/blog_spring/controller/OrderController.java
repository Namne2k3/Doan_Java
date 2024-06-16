package blog_spring.blog_spring.controller;

import blog_spring.blog_spring.dto.ReqResOrder;
import blog_spring.blog_spring.model.Order;
import blog_spring.blog_spring.model.OrderDetail;
import blog_spring.blog_spring.model.User;
import blog_spring.blog_spring.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/admin/orders")
    public ResponseEntity<ReqResOrder> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/api/v1/my_orders/{userId}")
    public ResponseEntity<ReqResOrder> getMyOrders(@PathVariable String userId) {
            return ResponseEntity.ok(orderService.getMyOrders(userId));
    }
    @GetMapping("/api/v1/orders/{id}")
    public ResponseEntity<ReqResOrder> getOrderById(@PathVariable String id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    @GetMapping("/api/v1/orders/createOrder")
    public ResponseEntity<Order> getOrderById() {
        return ResponseEntity.ok(orderService.createOrder());
    }
    @PostMapping("/api/v1/orders")
    public ResponseEntity<ReqResOrder> addOrder(
            @RequestBody Order order
//            @RequestParam List<OrderDetail> details,
//            @RequestParam String email,
//            @RequestParam String paymentMethod,
//            @RequestParam String shippingAddress,
//            @RequestParam String status,
//            @RequestParam int totalAmount
            ) {
//        Order order = new Order();
//        order.setDetails(details);
//        order.setEmail(email);
//        order.setOrderDate(new Date());
//        order.setPaymentMethod(paymentMethod);
//        order.setShippingAddress(shippingAddress);
//        order.setStatus(status);
//        order.setTotalAmount((long) totalAmount);
        return ResponseEntity.ok(orderService.addOrder(order));
    }

    @PutMapping("/admin/orders/{orderId}")
    public ResponseEntity<ReqResOrder> updateOrder(@PathVariable String orderId, @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateOrder(orderId,status));
    }

    @DeleteMapping("/api/v1/orders/{id}")
    public ResponseEntity<ReqResOrder> deleteOrder(@PathVariable String id) {
        return ResponseEntity.ok(orderService.deleteOrder(id));
    }
}
