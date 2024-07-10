package blog_spring.blog_spring.repository;

import blog_spring.blog_spring.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findFirst5ByOrderByWatchCountDesc();

    Product findTopByOrderBySoldDesc();

    Product findTopByOrderByWatchCountDesc();

    @Query("{}")
    List<Product> findAllPage(Pageable pageable);
}
