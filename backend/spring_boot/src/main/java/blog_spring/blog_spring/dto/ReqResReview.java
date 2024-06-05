package blog_spring.blog_spring.dto;

import blog_spring.blog_spring.model.Product;
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
public class ReqResReview<T> {


    private int statusCode;

    private String error;

    private String message;

    private T data;

    private List<T> dataList;

    @DBRef
    private Product product;

    @DBRef
    private User user;

    private Integer rating;

    private String comment;

    private Date createdAt;

    private Date updatedAt;
}
