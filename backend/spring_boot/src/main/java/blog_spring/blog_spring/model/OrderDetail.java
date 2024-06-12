package blog_spring.blog_spring.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "order_details")
public class OrderDetail {

    @Id
    private String id;
    // done product

    private Product product;

    // done quantity
    private int quantity;
}
