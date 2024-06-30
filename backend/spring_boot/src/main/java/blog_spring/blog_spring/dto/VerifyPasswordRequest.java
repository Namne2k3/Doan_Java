package blog_spring.blog_spring.dto;

import lombok.Data;

@Data
public class VerifyPasswordRequest {
    private String token;
    private String password;
}
