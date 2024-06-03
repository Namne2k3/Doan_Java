package blog_spring.blog_spring.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "reviews")
public class Review {

    @Id
    private String id;

    @DBRef
    private Product product;

    @DBRef
    private User user;

    private Integer rating;

    private String comment;

    private Date createdAt;

    private Date updatedAt;


}
