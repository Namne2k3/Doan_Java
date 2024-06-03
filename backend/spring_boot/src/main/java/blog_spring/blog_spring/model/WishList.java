package blog_spring.blog_spring.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "wishlists")
public class WishList {
    @Id
    private String id;

    @DBRef
    private User user;

    @DBRef
    private Product product;

    private Date createdAt;
}
