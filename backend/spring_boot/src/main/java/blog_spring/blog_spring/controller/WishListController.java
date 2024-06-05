package blog_spring.blog_spring.controller;

import blog_spring.blog_spring.dto.ReqResWishList;
import blog_spring.blog_spring.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WishListController {
    @Autowired
    private WishListService wishListService;

    @GetMapping("/api/v1/wishlists")
    public ResponseEntity<ReqResWishList> getAllWishLists() {
        return ResponseEntity.ok(wishListService.getAllWishList());
    }

    @GetMapping("/api/v1/wishlists/{id}")
    public ResponseEntity<ReqResWishList> getWishListById(@PathVariable String id) {
        return ResponseEntity.ok(wishListService.getById(id));
    }

    @PostMapping("/api/v1/wishlists")
    public ResponseEntity<ReqResWishList> addReview(@RequestBody ReqResWishList reqResWishList) {
        return ResponseEntity.ok(wishListService.addWishList(reqResWishList));
    }

    @PutMapping("/api/v1/wishlists/{id}")
    public ResponseEntity<ReqResWishList> updateReview(@PathVariable String id, @RequestBody ReqResWishList reqResWishList) {
        return ResponseEntity.ok(wishListService.updateWishList(id,reqResWishList));
    }

    @DeleteMapping("/api/v1/wishlists/{id}")
    public ResponseEntity<ReqResWishList> deleteReview(@PathVariable String id) {
        return ResponseEntity.ok(wishListService.deleteWishList(id));
    }
}
