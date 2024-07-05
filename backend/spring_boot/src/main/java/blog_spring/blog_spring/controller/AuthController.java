package blog_spring.blog_spring.controller;

import blog_spring.blog_spring.dto.AuthResponse;
import blog_spring.blog_spring.dto.GoogleAuthTokenRequest;
import blog_spring.blog_spring.model.User;
import blog_spring.blog_spring.repository.UserRepository;
import blog_spring.blog_spring.service.JWTUtils;
import blog_spring.blog_spring.service.UserManagementService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@RestController
public class AuthController {

    @Autowired
    private JWTUtils jwtUtils;

    private final String clientId = "1094118264922-622e85rqng6kd6pc93o29cjs88cd8qn4.apps.googleusercontent.com"; // Thay thế bằng Client ID của bạn
    @Autowired
    private UserManagementService userManagementService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/api/v1/auth/google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody GoogleAuthTokenRequest request) {
        String idTokenString = request.getToken();
        try {
            // Xác minh mã thông báo truy cập với Google
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                    .setAudience(Collections.singletonList(clientId))
                    .build();


            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                // Lấy thông tin người dùng từ payload
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                UserDetails userDetails = new UserDetails() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        return List.of();
                    }

                    @Override
                    public String getPassword() {
                        return "";
                    }

                    @Override
                    public String getUsername() {
                        return name;
                    }
                };

                // Kiểm tra xem người dùng đã tồn tại trong cơ sở dữ liệu của bạn chưa
                var userExisted = userManagementService.findByEmail(email);
                if ( userExisted.getData() != null && userExisted.getStatusCode() == 200 ) {
                    var jwt = jwtUtils.generateToken((User)userExisted.getData());
                    var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), (User)userExisted.getData());
                    return ResponseEntity.ok(new AuthResponse(jwt));

                } else {
                    User user = new User();
                    user.setEmail(email);
                    user.setRole("USER");
                    user.setUsername(name);
                    user.setPassword("");
                    user.setPhone("");
                    user.setAddress("");
                    user.setIsEmailVerified(true);

                    var savedUser = userRepository.save(user);

                    if ( savedUser.getId() != null ) {
                        var jwt = jwtUtils.generateToken(savedUser);
                        return ResponseEntity.ok(new AuthResponse(jwt));
                    }
                }
                // Tạo và trả về JWT hoặc thông tin xác thực khác cho người dùng
                String jwtToken = jwtUtils.generateToken(userDetails);
                return ResponseEntity.ok(new AuthResponse(jwtToken));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid ID token.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error authenticating with Google.");
        }
    }

    // ... other controller methods
}


