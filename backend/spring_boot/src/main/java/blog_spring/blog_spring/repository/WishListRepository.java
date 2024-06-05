package blog_spring.blog_spring.repository;

import blog_spring.blog_spring.model.WishList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends MongoRepository<WishList, String> {
}
