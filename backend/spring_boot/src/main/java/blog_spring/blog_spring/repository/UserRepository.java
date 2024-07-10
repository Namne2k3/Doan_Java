package blog_spring.blog_spring.repository;

import blog_spring.blog_spring.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query("{ 'username':  ?0}")
    Optional<User> findByUsername(String username);

    @Query("{ 'phone':  ?0}")
    Optional<User> findByPhone(String phone);

    @Query("{}")
    List<User> findAllPage(Pageable pageable);

    @Query("{ 'email' : ?0 }")
    Optional<User> findByEmail(String email);
}