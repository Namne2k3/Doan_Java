package blog_spring.blog_spring.controller;

import blog_spring.blog_spring.dto.ReqResBrand;
import blog_spring.blog_spring.dto.ReqResCate;
import blog_spring.blog_spring.dto.ReqResProduct;
import blog_spring.blog_spring.model.*;
import blog_spring.blog_spring.service.BrandService;
import blog_spring.blog_spring.service.CategoryService;
import blog_spring.blog_spring.service.ProductService;
import blog_spring.blog_spring.service.UserManagementService;
import com.mongodb.lang.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
public class ProductController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/api/v1/products")
    public ResponseEntity<ReqResProduct> getProducts(@RequestParam @Nullable String category){
        return ResponseEntity.ok(productService.getAllProducts(category));
    }

    @GetMapping("/admin/v1/products")
    public ResponseEntity<ReqResProduct> getAdminProducts(@RequestParam @Nullable String category){
        return ResponseEntity.ok(productService.getAllAdminProducts(category));
    }

    @GetMapping(value = "/api/v1/products", params = "search")
    public ResponseEntity<ReqResProduct> getSearchProducts(@RequestParam @Nullable String search){
        return ResponseEntity.ok(productService.getSearchProducts(search));
    }

    @GetMapping("/api/v1/products/{id}")
    public ResponseEntity<ReqResProduct> getProduct(@PathVariable String id){
        return ResponseEntity.ok(productService.getById(id));
    }

    @GetMapping("/api/v1/products/populars")
    public ResponseEntity<ReqResProduct> getPopulars(@RequestParam @Nullable String category) {
        return ResponseEntity.ok(productService.getPopulars(category));
    }

    @PostMapping(value = "/api/v1/products")
    public ResponseEntity<ReqResProduct> addProduct(
            @RequestParam("name") String name,
            @RequestParam("content") String content,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam("category") String category,
            @RequestParam("brand") String brand,
            @RequestParam("quantity") int quantity,
            @RequestParam("watchCount") int watchCount,
            @RequestParam("image") String image,
            @RequestParam("images") List<String> images,
            @RequestParam("weight") double weight,
            @RequestParam("resolution") String resolution,
            @RequestParam("brightness") Integer brightness,
            @RequestParam("screen_size") String screenSize,

            // Laptop
            @RequestParam(value = "graphic", required = false) String graphic,
            @RequestParam(value = "cpu", required = false) String cpu,
            @RequestParam(value = "ram", required = false) Integer ram,
            @RequestParam(value = "ssd", required = false) Integer ssd,
            @RequestParam(value = "panel", required = false) String panel,

            // Mobile
            @RequestParam(value = "back_camera", required = false) String backCamera,
            @RequestParam(value = "front_camera", required = false) String frontCamera,
            @RequestParam(value = "video_feature_back", required = false) String videoFeatureBack,
            @RequestParam(value = "video_feature_front", required = false) String videoFeatureFront,
            @RequestParam(value = "video_record", required = false) String videoRecord,
            @RequestParam(value = "chipset", required = false) String chipset,
            @RequestParam(value = "gpu", required = false) String gpu,
            @RequestParam(value = "battery", required = false) String battery,
            @RequestParam(value = "charge_tech", required = false) String chargeTech,
            @RequestParam(value = "SIM", required = false) String SIM,

            // Watch
            @RequestParam(value = "screen_tech", required = false) String screenTech,
            @RequestParam(value = "diameter", required = false) String diameter,
            @RequestParam(value = "design", required = false) String design,
            @RequestParam(value = "time_charge", required = false) Double time_charge,
            @RequestParam(value = "battery_life", required = false) String batteryLife
    )
    {
        Product p = new Product();
        p.setName(name);
        p.setContent(content);
        p.setDescription(description);
        p.setPrice(price);

        ReqResCate newCate = categoryService.getById(category);
        p.setCategory((Category) newCate.getData());

        ReqResBrand newbr = brandService.getById(brand);

        p.setBrand((Brand) newbr.getData());
        p.setImage("/images/" + image);
        p.setImages(images);
        p.setStock_quantity(quantity);
        p.setWatchCount(watchCount);

        Product_Attributes at = new Product_Attributes();

        // Laptop
        if (Objects.equals(category, "665eee91d176ea3961e606c0")) {
            at.setGraphic(graphic);
            at.setCpu(cpu);
            at.setRam(ram);
            at.setSsd(ssd);
            at.setPanel(panel);
            at.setScreen_size(screenSize);
            at.setBrightness(brightness);
            at.setResolution(resolution);
            at.setWeight(weight);
        }

        // Mobile Phone
        if (Objects.equals(category, "665eee7ad176ea3961e606bf")) {
            at.setChipset(chipset);
            at.setCpu(cpu);
            at.setGpu(gpu);
            at.setBattery(battery);
            at.setBack_camera(backCamera);
            at.setFront_camera(frontCamera);
            at.setVideo_feature_back(videoFeatureBack);
            at.setVideo_feature_front(videoFeatureFront);
            at.setVideo_record(videoRecord);
            at.setRam(ram);
            at.setSsd(ssd);
            at.setScreen_tech(screenTech);
            at.setScreen_size(screenSize);
            at.setBrightness(brightness);
            at.setResolution(resolution);
            at.setWeight(weight);
        }

        // Watch
        if (Objects.equals(category, "66615ffbc875cc7d60827534")) {
            at.setScreen_tech(screenTech);
            at.setScreen_size(screenSize);
            at.setResolution(resolution);
            at.setDiameter(diameter);
            at.setTime_charge(time_charge);
            at.setBattery_life(batteryLife);
        }

        return ResponseEntity.ok(productService.addProduct(p, at));
    }

    @PutMapping("/admin/products/{id}")
    public ResponseEntity<ReqResProduct> updateProduct(@PathVariable String id, @RequestBody ReqResProduct reqResProduct){
        return ResponseEntity.ok(productService.updateProduct(id,reqResProduct));
    }

    @PutMapping("/admin/products/setHide/{id}")
    public ResponseEntity<ReqResProduct> updateProductIsHide(@PathVariable String id, @RequestParam("isHide") String isHide ){
        return ResponseEntity.ok(productService.updateProductIsHide(id,isHide));
    }

    @DeleteMapping("/api/v1/products/{id}")
    public ResponseEntity<ReqResProduct> deleteProduct(@PathVariable String id){
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

}
