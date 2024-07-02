package blog_spring.blog_spring.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Document(collection = "users")
@Data
public class User implements UserDetails {

    public User() {
        image = "profile.jpg";
    }

    @Id
    private String id;

    private String image;

    @DBRef
    private List<WishList> wishLists ;

    @DBRef
    private List<Cart> carts ;

    private String username;

    private String email;

    private String password;

    private String phone;

    private String address;

    private Date createdAt;

    private Date updatedAt;

    private String role;


    // Nó được sử dụng để cung cấp các quyền (authorities) của người dùng.
    // Các quyền này được sử dụng bởi Spring Security để
    // xác định những gì người dùng được phép làm trong ứng dụng.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public String getUsername() {
        return email;
    }
}
