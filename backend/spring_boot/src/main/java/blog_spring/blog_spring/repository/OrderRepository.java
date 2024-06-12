package blog_spring.blog_spring.repository;

import blog_spring.blog_spring.model.Order;
import blog_spring.blog_spring.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
}
