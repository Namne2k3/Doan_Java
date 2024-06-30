package blog_spring.blog_spring.dto;

import blog_spring.blog_spring.model.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqResBrand<T> {

    private int statusCode;

    private String error;

    private String message;

    private T data;

    private List<T> dataList;

    private String name;

    private String description;

    private String isHide;

    @DBRef
    private List<Product> products;
}
