package blog_spring.blog_spring.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "orders")
public class Order {

    // done id
    @Id
    private String id;

    @DBRef
    private User user;
    
    // done orderdetails
    @DBRef
    private List<OrderDetail> details;

    // done email
    private String email;

    // done created
    private Date orderDate;

    // done amount
    private Long totalAmount;

    private String phone;

    // status
    private String status;

    // done address
    private String shippingAddress;

    // paymentMethod
    private String paymentMethod;

    // done created
    private Date createdAt;

    // done created
    private Date updatedAt;
}
