package blog_spring.blog_spring.model;

import lombok.Data;
import lombok.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Document(collection = "carts")
@Data
public class Cart {

    @Id
    private String id;

    @DBRef
    private User user;

    @DBRef
    private Product product;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date createdAt;

    private int quantity = 0;
}
