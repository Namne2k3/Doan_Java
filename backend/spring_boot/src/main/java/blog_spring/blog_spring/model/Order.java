package blog_spring.blog_spring.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "orders")
public class Order {
    @Id
    private String id;

    @DBRef
    private User user;

    @DBRef
    private List<OrderDetail> details;

    private Date orderDate;

    private Long totalAmount;

    private String status;

    private String shippingAddress;

    private String paymentMethod;

    private Date createdAt;

    private Date updatedAt;
}
