package blog_spring.blog_spring.controller;

import blog_spring.blog_spring.dto.ReqResBrand;
import blog_spring.blog_spring.dto.ReqResReview;
import blog_spring.blog_spring.service.BrandService;
import blog_spring.blog_spring.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/api/v1/reviews")
    public ResponseEntity<ReqResReview> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/api/v1/reviews/{id}")
    public ResponseEntity<ReqResReview> getReviewById(@PathVariable String id) {
        return ResponseEntity.ok(reviewService.getById(id));
    }

    @PostMapping("/api/v1/reviews")
    public ResponseEntity<ReqResReview> addReview(@RequestBody ReqResReview reqResReview) {
        return ResponseEntity.ok(reviewService.addReview(reqResReview));
    }

    @PutMapping("/api/v1/reviews/{id}")
    public ResponseEntity<ReqResReview> updateReview(@PathVariable String id, @RequestBody ReqResReview reqResReview) {
        return ResponseEntity.ok(reviewService.updateReview(id,reqResReview));
    }

    @DeleteMapping("/api/v1/reviews/{id}")
    public ResponseEntity<ReqResReview> deleteReview(@PathVariable String id) {
        return ResponseEntity.ok(reviewService.deleteReview(id));
    }

}
