package blog_spring.blog_spring.dto;

import blog_spring.blog_spring.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes<T> {
    private int statusCode;

    private String error;

    private String message;

    private String token;

    private String refreshToken;

    private Boolean isVerified;

    private Boolean enabled = true;

    private Boolean isEmailVerified = false;

    private String expirationTime;

    private String username;

    private long amount;

    private int amountAllData;

    private String role;

    private String email;

    private String password;

    private String phone;

    private String address;

    private String image;

    private T data;

    private List<T> dataList;
}
