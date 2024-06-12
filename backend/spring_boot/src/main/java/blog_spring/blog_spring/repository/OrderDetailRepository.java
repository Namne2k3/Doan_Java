package blog_spring.blog_spring.repository;

import blog_spring.blog_spring.model.OrderDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends MongoRepository<OrderDetail, String> {
}
