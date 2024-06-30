package blog_spring.blog_spring.controller;

import blog_spring.blog_spring.dto.ReqResBrand;
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
    public ResponseEntity<ReqResBrand> addBrand(@RequestParam String name, @RequestParam String description) {

        ReqResBrand reqResBrand = new ReqResBrand();
        reqResBrand.setName(name);
        reqResBrand.setDescription(description);
        return ResponseEntity.ok(brandService.addBrand(reqResBrand));
    }

    @PutMapping("/admin/brands/{id}")
    public ResponseEntity<ReqResBrand> updateBrand(@PathVariable String id, @RequestBody ReqResBrand reqResBrand) {
        return ResponseEntity.ok(brandService.updateBrand(id,reqResBrand));
    }

    @PutMapping("/admin/brands/setHide/{id}")
    public ResponseEntity<ReqResBrand> updateBrandSetHide(@PathVariable String id, @RequestParam("isHide") String isHide) {
        return ResponseEntity.ok(brandService.updateBrandSetHide(id,isHide));
    }

    @DeleteMapping("/api/v1/brands/{id}")
    public ResponseEntity<ReqResBrand> deleteBrand(@PathVariable String id) {
        return ResponseEntity.ok(brandService.deleteBrand(id));
    }
}
