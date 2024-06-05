package blog_spring.blog_spring.controller;
import blog_spring.blog_spring.dto.ReqResCate;
import blog_spring.blog_spring.model.Category;
import blog_spring.blog_spring.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/api/v1/categories")
    public ResponseEntity<ReqResCate> getCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
    @GetMapping("/api/v1/categories/{id}")
    public ResponseEntity<ReqResCate> getCategory(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @PostMapping("/api/v1/categories")
    public ResponseEntity<ReqResCate> addCategory(@RequestBody ReqResCate reqResCate) {
        return ResponseEntity.ok(categoryService.addCategory(reqResCate));
    }

    @PutMapping("/api/v1/categories/{id}")
    public ResponseEntity<ReqResCate> updateCategory(@PathVariable String id, @RequestBody Category category) {
        return ResponseEntity.ok(categoryService.updateCategory(id,category));
    }

    @DeleteMapping("/api/v1/categories/{id}")
    public ResponseEntity<ReqResCate> deleteCategory(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }
}
