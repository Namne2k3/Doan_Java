package blog_spring.blog_spring.controller;

import blog_spring.blog_spring.dto.ReqRes;
import blog_spring.blog_spring.dto.ReqResComment;
import blog_spring.blog_spring.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @GetMapping(value = "/api/v1/voucher/users/{id}", params = "voucher")
    public ResponseEntity<ReqRes> getCommentsByProductId(@PathVariable String id ,@RequestParam String voucher) {
        return ResponseEntity.ok(voucherService.calculateVoucher(id,voucher));
    }
}
