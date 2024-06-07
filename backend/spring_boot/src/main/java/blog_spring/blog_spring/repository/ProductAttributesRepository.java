package blog_spring.blog_spring.repository;

import blog_spring.blog_spring.model.Product_Attributes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAttributesRepository extends MongoRepository<Product_Attributes, String> {
}
