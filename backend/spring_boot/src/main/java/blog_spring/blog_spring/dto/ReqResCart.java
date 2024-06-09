package blog_spring.blog_spring.dto;

import blog_spring.blog_spring.model.Product;
import blog_spring.blog_spring.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqResCart<T> {
    private int statusCode;

    private String error;

    private String message;

    private T data;

    private List<T> dataList;

    @DBRef
    private User user;

    @DBRef
    private Product product;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date createdAt;

    private int quantity;
}
