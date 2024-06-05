package blog_spring.blog_spring.controller;

import blog_spring.blog_spring.dto.ReqResBrand;
import blog_spring.blog_spring.dto.ReqResCate;
import blog_spring.blog_spring.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("/api/v1/brands")
    public ResponseEntity<ReqResBrand> getAllBrands() {
        return ResponseEntity.ok(brandService.getAllBrand());
    }

    @GetMapping("/api/v1/brands/{id}")
    public ResponseEntity<ReqResBrand> getBrandById(@PathVariable String id) {
        return ResponseEntity.ok(brandService.getById(id));
    }

    @PostMapping("/api/v1/brands")
    public ResponseEntity<ReqResBrand> addBrand(@RequestBody ReqResBrand reqResBrand) {
        return ResponseEntity.ok(brandService.addCategory(reqResBrand));
    }

    @PutMapping("/api/v1/brands/{id}")
    public ResponseEntity<ReqResBrand> updateBrand(@PathVariable String id, @RequestBody ReqResBrand reqResBrand) {
        return ResponseEntity.ok(brandService.updateBrand(id,reqResBrand));
    }

    @DeleteMapping("/api/v1/brands/{id}")
    public ResponseEntity<ReqResBrand> deleteBrand(@PathVariable String id) {
        return ResponseEntity.ok(brandService.deleteBrand(id));
    }
}
