package blog_spring.blog_spring.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqResCate<T> {

    private String name;

    private String description;
    
    private int statusCode;

    private String error;

    private String message;

    private String image;

    private T data;

    private List<T> dataList;
}