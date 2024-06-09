package blog_spring.blog_spring.service;

import blog_spring.blog_spring.dto.ReqResProduct;
import blog_spring.blog_spring.model.Cart;
import blog_spring.blog_spring.model.Product;
import blog_spring.blog_spring.model.Product_Attributes;
import blog_spring.blog_spring.model.User;
import blog_spring.blog_spring.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductAttributesRepository productAttributesRepository;

    public ReqResProduct getById(String id) {
        ReqResProduct reqRes = new ReqResProduct();
        try {
            Product product = productRepository.findById(id).get();
            if ( product != null ) {
                reqRes.setStatusCode(200);
                reqRes.setMessage("Get Product successfully");
                reqRes.setData(product);
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("Product not found");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while get product: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqResProduct getAllProducts(String category) {
        ReqResProduct reqRes = new ReqResProduct();

        try {
            List<Product> result = productRepository.findAll();
            if ( category != null && !category.equals("") ) {
                result = result.stream()
                        .filter(p -> p.getCategory().getName().equals(category))
                        .toList();
            }
            if (!result.isEmpty()) {
                reqRes.setDataList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No products found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }

    public ReqResProduct addProduct(Product product, Product_Attributes at) {
        ReqResProduct resp = new ReqResProduct();
        try {
            product.setCreatedAt(new Date());
            product.setUpdatedAt(new Date());

            var savedAttr = productAttributesRepository.save(at);

            // luu attrbiute tai day
            if ( savedAttr.getId() != null ) {
                product.setProduct_attributes(savedAttr);
            } else {
                throw new Exception("Error occurred while saving product attributes");
            }

            var savedProduct = productRepository.save(product);
            if ( savedProduct.getId() != null ) {
                resp.setStatusCode(200);
                resp.setMessage("Successful");
                resp.setData(savedProduct);
            } else {
                resp.setStatusCode(404);
                resp.setError("Can't save the product to database");
            }
            return resp;
        } catch(Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

//    public ReqResProduct updateProduct(String id, ReqResProduct registrationRequest) {
//        ReqResProduct resp = new ReqResProduct();
//
//        try {
//            Product product = productRepository.findById(id).get();
//
//            product.setName(registrationRequest.getName());
//            product.setContent(registrationRequest.getContent());
//            product.setDescription(registrationRequest.getDescription());
//            product.setPrice(registrationRequest.getPrice());
//            product.setStock_quantity(registrationRequest.getStock_quantity());
//            product.setAttributes(registrationRequest.getAttributes());
//            product.setMobile_attributes(registrationRequest.getMobile_attributes());
//            product.setWatch_attributes(registrationRequest.getWatch_attributes());
//            product.setCategory(registrationRequest.getCategory());
//            product.setBrand(registrationRequest.getBrand());
//            product.setWatchCount(registrationRequest.getWatchCount());
//            product.setUpdatedAt(new Date());
//            product.setImage(registrationRequest.getImage());
//            product.setImages(registrationRequest.getImages());
//
//
//            var saved = productRepository.save(product);
//
//            if ( saved.getId() != null ) {
//                resp.setStatusCode(200);
//                resp.setMessage("Updated Product Successful");
//                resp.setData(saved);
//            }
//
//        } catch(Exception e) {
//            resp.setStatusCode(500);
//            resp.setError(e.getMessage());
//        }
//        return resp;
//    }

    public ReqResProduct deleteProduct(String id) {
        ReqResProduct reqRes = new ReqResProduct();
        try {
            Product product = productRepository.findById(id).get();
            if ( product != null ) {
                productRepository.deleteById(product.getId());
                reqRes.setStatusCode(200);
                reqRes.setMessage("Product deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("Product not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting product: " + e.getMessage());
        }
        return reqRes;
    }

}
