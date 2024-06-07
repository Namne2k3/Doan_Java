package blog_spring.blog_spring.dto;

import blog_spring.blog_spring.model.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqResProduct<T> {

    private int statusCode;

    private String error;

    private String message;

    private String name;

    private String content;

    private String description;

    private double price;

    private int stock_quantity;

    @DBRef
    private Product_Attributes attributes;

    @DBRef
    private Watch_Attributes watch_attributes;

    @DBRef
    private Category category;

    @DBRef
    private Brand brand;

    @DBRef
    private List<Order> orders ;

    private long watchCount;

    private Date createdAt;

    private Date updatedAt;

    private List<String> images;

    private String image;

    private T data;
    private List<T> dataList;
}
