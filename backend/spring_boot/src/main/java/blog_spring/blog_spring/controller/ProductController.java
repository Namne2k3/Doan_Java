package blog_spring.blog_spring.controller;

import blog_spring.blog_spring.dto.ReqResCate;
import blog_spring.blog_spring.dto.ReqResProduct;
import blog_spring.blog_spring.model.Brand;
import blog_spring.blog_spring.model.Category;
import blog_spring.blog_spring.model.Product;
import blog_spring.blog_spring.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @GetMapping("/api/v1/products")
    public ResponseEntity<ReqResProduct> getProducts(){
//        logger.info(System.getProperty("user.dir") + "/src/main/resources/static/images");
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/api/v1/products/{id}")
    public ResponseEntity<ReqResProduct> getProduct(@PathVariable String id){
        return ResponseEntity.ok(productService.getById(id));
    }

    @PostMapping(value = "/api/v1/products")
    public ResponseEntity<ReqResProduct> addProduct(@RequestBody Product product){
        return ResponseEntity.ok(productService.addProduct(product));
    }

    @PutMapping("/api/v1/products/{id}")
    public ResponseEntity<ReqResProduct> updateProduct(@PathVariable String id, @RequestBody ReqResProduct reqResProduct){
        return ResponseEntity.ok(productService.updateProduct(id,reqResProduct));
    }

    @DeleteMapping("/api/v1/products/{id}")
    public ResponseEntity<ReqResProduct> deleteProduct(@PathVariable String id){
        return ResponseEntity.ok(productService.deleteProduct(id));
    }
}
