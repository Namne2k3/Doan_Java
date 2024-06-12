package blog_spring.blog_spring.dto;

import blog_spring.blog_spring.model.OrderDetail;
import blog_spring.blog_spring.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqResOrder<T> {

    private int statusCode;

    private String error;

    private String message;

    private T data;

    private List<T> dataList;

    private String email;

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
