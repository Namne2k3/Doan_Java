package blog_spring.blog_spring.controller;

import blog_spring.blog_spring.dto.ReqResBrand;
import blog_spring.blog_spring.dto.ReqResOrder;
import blog_spring.blog_spring.repository.OrderRepository;
import blog_spring.blog_spring.service.BrandService;
import blog_spring.blog_spring.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/api/v1/orders")
    public ResponseEntity<ReqResOrder> getAllBrands() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/api/v1/orders/{id}")
    public ResponseEntity<ReqResOrder> getBrandById(@PathVariable String id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    @PostMapping("/api/v1/orders")
    public ResponseEntity<ReqResOrder> addBrand(@RequestBody ReqResOrder reqResOrder) {
        return ResponseEntity.ok(orderService.addOrder(reqResOrder));
    }

    @PutMapping("/api/v1/orders/{id}")
    public ResponseEntity<ReqResOrder> updateBrand(@PathVariable String id, @RequestBody ReqResOrder reqResOrder) {
        return ResponseEntity.ok(orderService.updateOrder(id,reqResOrder));
    }

    @DeleteMapping("/api/v1/orders/{id}")
    public ResponseEntity<ReqResOrder> deleteBrand(@PathVariable String id) {
        return ResponseEntity.ok(orderService.deleteOrder(id));
    }
}
