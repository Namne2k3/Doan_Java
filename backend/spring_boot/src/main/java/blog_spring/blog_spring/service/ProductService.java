package blog_spring.blog_spring.service;

import blog_spring.blog_spring.dto.ReqResProduct;
import blog_spring.blog_spring.model.*;
import blog_spring.blog_spring.repository.*;
import com.mongodb.client.AggregateIterable;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ProductAttributesRepository productAttributesRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;

    public ReqResProduct getMost(String most) {
        ReqResProduct reqRes = new ReqResProduct();
        try {
            Product product = null;
            if ( most.equals("watchCount") ) {
                product = productRepository.findTopByOrderByWatchCountDesc();
            }

            if ( most.equals("sold") ) {
                product = productRepository.findTopByOrderBySoldDesc();
            }
            if ( product.getId() != null ) {
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

    public ReqResProduct getSearchProducts(String search) {
        ReqResProduct reqRes = new ReqResProduct();
        try {
            if (search != null && !search.isEmpty()) {

                boolean indexExists = false;
                for (Document index : mongoTemplate.getCollection("products").listIndexes()) {
                    if ("name_text".equals(index.get("name"))) {
                        indexExists = true;
                        break;
                    }
                }

                if (!indexExists) {
                    mongoTemplate.getCollection("products").createIndex(
                            new Document("name", "text")
                                    .append("content", "text")
                                    .append("description", "text")
                                    .append("price", "text")
                                    .append("product_attributes", "text")
                                    .append("category", "text")
                                    .append("brand", "text")
                    );
                }

                // Pipeline mới sử dụng $match với $text
                List<Document> pipeline = Arrays.asList(
                        new Document("$match", new Document("$text", new Document("$search", search))),
                        new Document("$set", new Document("id", new Document("$toString", "$_id"))), // Đổi _id thành id và chuyển đổi ObjectId thành String
                        new Document("$unset", "_id") // Bỏ _id khỏi tài liệu
                );

                AggregateIterable<Document> result = mongoTemplate.getCollection("products").aggregate(pipeline);

                List<Document> resultsList = new ArrayList<>();
                for (Document doc : result) {
                    resultsList.add(doc);
                }
                if (!resultsList.isEmpty()) {
                    reqRes.setDataList(resultsList);
                    reqRes.setStatusCode(200);
                    reqRes.setMessage("Successful");
                } else {
                    reqRes.setStatusCode(404);
                    reqRes.setMessage("No products found");
                }
            } else {
                List<Product> resultsList = productRepository.findAll().stream().sorted(Comparator.comparingLong(Product::getWatchCount).reversed()).collect(Collectors.toList());;
                if (!resultsList.isEmpty()) {
                    reqRes.setDataList(resultsList);
                    reqRes.setStatusCode(200);
                    reqRes.setMessage("Successful");
                } else {
                    reqRes.setStatusCode(404);
                    reqRes.setMessage("No products found");
                }
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        } finally {
            return reqRes;
        }
    }


    public ReqResProduct getPopulars(String category) {

        ReqResProduct reqRes = new ReqResProduct();

        try {
            List<Product> result = productRepository.findFirst5ByOrderByWatchCountDesc();
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
    public ReqResProduct getById(String id, String watchCount) {
        ReqResProduct reqRes = new ReqResProduct();
        try {
            Product product = productRepository.findById(id).get();
            if ( product.getId() != null ) {
                if ( watchCount != null && !watchCount.isEmpty()) {
                    product.setWatchCount(product.getWatchCount() + Integer.parseInt(watchCount));
                    var savedProduct = productRepository.save(product);
                    reqRes.setStatusCode(200);
                    reqRes.setMessage("Get Product successfully");
                    reqRes.setData(savedProduct);
                } else {
                    reqRes.setStatusCode(200);
                    reqRes.setMessage("Get Product successfully");
                    reqRes.setData(product);
                }
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

    public ReqResProduct getAllProductsBrand(String brand) {
        ReqResProduct reqRes = new ReqResProduct();

        try {
            List<Product> result = productRepository.findAll();
            if ( brand != null && !brand.equals("") ) {
                result = result.stream()
                        .filter(p -> p.getBrand().getId().equals(brand))
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

    public ReqResProduct getAllAdminProducts(String category, String pageParams) {
        ReqResProduct reqRes = new ReqResProduct();

        try {
            List<Product> result = productRepository.findAll();
            if ( pageParams != null  ) {
                int pageSize = 10;
                int page = Integer.parseInt(pageParams);
                Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
                reqRes.setAmountAllData(result.size());
                result = productRepository.findAllPage(pageable);
            }
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

    public ReqResProduct getAllProducts(String category) {
        ReqResProduct reqRes = new ReqResProduct();

        try {
            List<Product> result = productRepository.findAll().stream().filter(p -> !p.isHide() && !p.getBrand().isHide()).toList();
            if ( category != null && !category.equals("") && !category.equals("undefined") ) {
                result = result.stream()
                        .filter(p -> p.getCategory().getName().equals(category))
                        .toList();
            }
            if (!result.isEmpty()) {
                reqRes.setDataList(result.stream().sorted(Comparator.comparingLong(Product::getWatchCount).reversed()).toList());
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

    public ReqResProduct updateProductIsHide(String id, String isHide) {
        ReqResProduct resp = new ReqResProduct();


        try {

            var product = productRepository.findById(id).get();

            if ( product.getId() != null ) {
                if ( isHide != null && isHide.equals("true") ) {
                    product.setHide(true);
                }

                if ( isHide != null && isHide.equals("false") ) {
                    product.setHide(false);
                }
            }

            var saved = productRepository.save(product);
            if ( saved.getId() != null ) {
                resp.setStatusCode(200);
                resp.setMessage("Successful");
                resp.setData(saved);
            } else {
                resp.setStatusCode(404);
                resp.setError("Can't save the product to database");
            }


        } catch(Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqResProduct updateProduct(String id, ReqResProduct registrationRequest) {
        ReqResProduct resp = new ReqResProduct();


        try {

            var product = productRepository.findById(id).get();
            var category = categoryRepository.findById(registrationRequest.getCategory().getId()).get();
            var brand = brandRepository.findById(registrationRequest.getBrand().getId()).get();
            var savedProductAttr = productAttributesRepository.save(registrationRequest.getProduct_attributes());

            product.setName(registrationRequest.getName());
            product.setContent(registrationRequest.getContent());
            product.setDescription(registrationRequest.getDescription());
            product.setPrice(registrationRequest.getPrice());
            product.setStock_quantity(registrationRequest.getStock_quantity());
            product.setProduct_attributes(savedProductAttr);
            product.setCategory(category);
            product.setBrand(brand);
            product.setUpdatedAt(new Date());
            product.setImage(registrationRequest.getImage());
            product.setImages(registrationRequest.getImages());
            var saved = productRepository.save(product);
            if ( saved.getId() != null ) {
                resp.setStatusCode(200);
                resp.setMessage("Successful");
                resp.setData(saved);
            } else {
                resp.setStatusCode(404);
                resp.setMessage("Có lỗi! Không thể cập nật sản phẩm");
                resp.setError("Can't save the product to database");
            }


        } catch(Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

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
