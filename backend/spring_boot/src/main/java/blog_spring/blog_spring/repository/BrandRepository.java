package blog_spring.blog_spring.repository;

import blog_spring.blog_spring.model.Brand;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends MongoRepository<Brand, String> {

    @Query("{ 'name' : { $regex: ?0, $options: 'i' } }")
    Brand findByName(String name);
}
