package blog_spring.blog_spring.controller;

import blog_spring.blog_spring.dto.ReqResCart;
import blog_spring.blog_spring.model.Cart;
import blog_spring.blog_spring.model.Product;
import blog_spring.blog_spring.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/api/v1/user/{id}/addCart")
    public ResponseEntity<ReqResCart> addCart(@PathVariable String id, @RequestBody Product product) {
        return ResponseEntity.ok(cartService.addCart(id,product));
    }

    @GetMapping("/api/v1/user/{id}/carts")
    public ResponseEntity<ReqResCart> getCart(@PathVariable String id) {
        return ResponseEntity.ok(cartService.getAllCart(id));
    }

    @PutMapping("/api/v1/carts/{id}")
    public ResponseEntity<ReqResCart> updateCart(@PathVariable String id, @RequestBody Cart cart) {
        return ResponseEntity.ok(cartService.updateCart(id,cart));
    }

    @DeleteMapping("/api/v1/user/{userId}/carts/{id}")
    public ResponseEntity<ReqResCart> deleteCart(@PathVariable String userId,@PathVariable String id) {
        return ResponseEntity.ok(cartService.deleteById(userId,id));
    }
}
