package blog_spring.blog_spring.service;

import blog_spring.blog_spring.dto.ReqRes;
import blog_spring.blog_spring.dto.ReqResBrand;
import blog_spring.blog_spring.dto.ReqResCate;
import blog_spring.blog_spring.model.Brand;
import blog_spring.blog_spring.model.Category;
import blog_spring.blog_spring.model.User;
import blog_spring.blog_spring.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    public ReqResBrand getById(String id) {
        ReqResBrand resp = new ReqResBrand();

        try {
            Brand brand = brandRepository.findById(id).get();

            if ( brand.getId() != null ) {
                resp.setStatusCode(200);
                resp.setMessage("Get Brand Successful");
                resp.setData(brand);
            } else {
                throw new Exception("Brand not found");
            }

        } catch(Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqResBrand getAllBrand() {
        ReqResBrand reqRes = new ReqResBrand();

        try {
            List<Brand> result = brandRepository.findAll();
            if (!result.isEmpty()) {
                reqRes.setDataList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No brands found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }

    public ReqResBrand addCategory(ReqResBrand registrationRequest) {
        ReqResBrand resp = new ReqResBrand();

        try {
            Brand brand = new Brand();
            brand.setName(registrationRequest.getName());
            brand.setDescription(registrationRequest.getDescription());
            var saved = brandRepository.save(brand);

            if ( saved.getId() != null ) {
                resp.setStatusCode(200);
                resp.setMessage("Saved Brand Successful");
                resp.setData(saved);
            }

        } catch(Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqResBrand updateBrand(String id, ReqResBrand registrationRequest) {
        ReqResBrand resp = new ReqResBrand();

        try {
            Brand brand = brandRepository.findById(id).get();
            brand.setName(registrationRequest.getName());
            brand.setDescription(registrationRequest.getDescription());
            var saved = brandRepository.save(brand);

            if ( saved.getId() != null ) {
                resp.setStatusCode(200);
                resp.setMessage("Updated Brand Successful");
                resp.setData(saved);
            }

        } catch(Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqResBrand deleteBrand(String id) {
        ReqResBrand reqRes = new ReqResBrand();
        try {
            Brand brand = brandRepository.findById(id).get();
            if ( brand != null ) {
                brandRepository.deleteById(brand.getId());
                reqRes.setStatusCode(200);
                reqRes.setMessage("Brand deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("Brand not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return reqRes;
    }
}
