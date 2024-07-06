package blog_spring.blog_spring.controller;

import blog_spring.blog_spring.dto.ReqResComment;
import blog_spring.blog_spring.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/api/v1/products/{id}/comments")
    public ResponseEntity<ReqResComment> getCommentsByProductId(@PathVariable String id) {
        return ResponseEntity.ok(commentService.getCommentsByProductId(id));
    }

    @GetMapping("/admin/comments")
    public ResponseEntity<ReqResComment> getComments() {
        return ResponseEntity.ok(commentService.getComments());
    }

    @PostMapping(value = "/api/v1/comments")
    public ResponseEntity<ReqResComment> addComment(@RequestBody ReqResComment comment) {
        return ResponseEntity.ok(commentService.addComment(comment));
    }

    @PutMapping(value = "/api/v1/comments/{id}", params = "verify")
    public ResponseEntity<ReqResComment> updateComment(@PathVariable String id, @RequestParam("verify") String verify) {
        return ResponseEntity.ok(commentService.updateVerifyComment(id,verify));
    }

    @GetMapping(value = "/admin/comments", params = "verify")
    public ResponseEntity<ReqResComment> getVerifyComments(@RequestParam("verify") String verify) {
        return ResponseEntity.ok(commentService.getVerifyComments(verify));
    }
}
