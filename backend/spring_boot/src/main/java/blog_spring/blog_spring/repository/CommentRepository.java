package blog_spring.blog_spring.repository;

import blog_spring.blog_spring.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

    @Query("{'productId': ?0}")
    List<Comment> findAllByProductId(String productId);

    @Query("{'isVerify': ?0}")
    List<Comment> findCommentsByVerify(String verify);
}
