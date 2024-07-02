package blog_spring.blog_spring.service;

import blog_spring.blog_spring.dto.ReqResCate;
import blog_spring.blog_spring.model.Category;
import blog_spring.blog_spring.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public ReqResCate updateCategory(String id, Category category) {
        ReqResCate reqRes = new ReqResCate();

        try {
            Category result = categoryRepository.findById(id).get();
            if (result != null) {
                result.setName(category.getName());
                result.setDescription(category.getDescription());
                result.setImage(category.getImage());

                var saved = categoryRepository.save(result);

                reqRes.setData(saved);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Update Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No categories found");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());

        }
        return reqRes;
    }

    public ReqResCate getById(String id) {
        ReqResCate resp = new ReqResCate();

        try {
            Category category = categoryRepository.findById(id).get();

            if ( category.getId() != null ) {
                resp.setStatusCode(200);
                resp.setMessage("Get Category Successful");
                resp.setData(category);
            } else {
                throw new Exception("Category not found");
            }

        } catch(Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqResCate getAllCategories() {
        ReqResCate reqRes = new ReqResCate();

        try {
            List<Category> result = categoryRepository.findAll();
//            if ( category != "" ) {
//                result = result.stream().filter(p -> p.getName().equals(category)).toList();
//            }


            if (!result.isEmpty()) {
                reqRes.setDataList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No categories found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }

    public ReqResCate addCategory(ReqResCate registrationRequest) {
        ReqResCate resp = new ReqResCate();

        try {
            Category category = new Category();
            category.setName(registrationRequest.getName());
            category.setDescription(registrationRequest.getDescription());
            category.setImage(registrationRequest.getImage());

            Category savedCate = categoryRepository.save(category);
            if ( savedCate.getId() != null ) {
                resp.setMessage("Saved User Successfully!");
                resp.setStatusCode(200);
                resp.setData(savedCate);
            }

        } catch(Exception e) {

            resp.setStatusCode(500);
            resp.setError(e.getMessage());

        }
        return resp;
    }

    public ReqResCate deleteCategory(String id) {
        ReqResCate reqRes = new ReqResCate();

        try {
            Category category = categoryRepository.findById(id).get();
            if ( category != null ) {
                categoryRepository.deleteById(category.getId());
                reqRes.setStatusCode(200);
                reqRes.setMessage("Category deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("Category not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return reqRes;
    }
}
