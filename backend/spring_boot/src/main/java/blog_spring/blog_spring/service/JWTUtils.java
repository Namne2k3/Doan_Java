package blog_spring.blog_spring.service;

import blog_spring.blog_spring.model.User;
import blog_spring.blog_spring.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JWTUtils {
    private final UserRepository userRepository;
    private SecretKey key;

    private static final Long EXPIRATION_TIME = 86400000L; // 24 hours


    // Constructor này khi khởi tạo sẽ tự động khởi tạo một key bí mật
    // Giúp cho việc mã hóa trở nên bảo mật
    private JWTUtils(UserRepository userRepository) {
        String secretString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
        byte[] keyBytes = Base64.getDecoder().decode(secretString.getBytes(StandardCharsets.UTF_8));
        this.key = new SecretKeySpec(keyBytes,"HmacSHA256");
        this.userRepository = userRepository;
    }


    // dùng để tạo một JSON Web Token (JWT) cho một
    // người dùng dựa trên các thông tin chi tiết của người dùng đó

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                // Đặt subject của JWT là tên người dùng (username).
                // Đây là thông tin chính của người dùng và sẽ được lưu trữ trong payload của JWT.
                .subject(userDetails.getUsername())

                // Đặt thời gian phát hành của JWT là thời điểm hiện tại.
                .issuedAt(new Date(System.currentTimeMillis()))

                // Đặt thời gian hết hạn của JWT
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))

                // Ký JWT bằng khóa bí mật (key).
                // Khóa này được tạo và khởi tạo trong constructor của lớp JWTUtils.
                .signWith(key)

                // Hoàn thành việc xây dựng JWT và chuyển đổi nó thành chuỗi JWT hoàn chỉnh.
                .compact()
            ;
    }

    public String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                // Đặt các thông tin bổ sung vào payload của JWT.
                // claims là một HashMap chứa các thông tin bổ sung mà bạn muốn lưu trữ trong token.
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    // Claims là các thông tin được lưu trữ trong payload của JWT.
    // Phương thức này cho phép bạn sử dụng một hàm (Function)
    // để xử lý và trích xuất thông tin cụ thể từ các claims.
    private <T> T extractClaims(String token, Function<Claims,T> claimsTFunction) {

        // Áp dụng hàm claimsTFunction lên các claims đã được trích xuất.
        // Hàm này sẽ xử lý và trả về giá trị mong muốn từ các claims.
        return claimsTFunction.apply(

                // Tạo một parser (trình phân tích cú pháp) cho JWT sử dụng thư viện
                Jwts.parser()
                        //  Xác minh chữ ký của JWT bằng khóa bí mật (key).
                        //  Điều này đảm bảo rằng token không bị giả mạo.
                        .verifyWith(key)

                        // Xây dựng parser với các cài đặt đã chỉ định.
                        .build()

                        // Phân tích cú pháp và xác minh token.
                        // Nếu token hợp lệ, nó sẽ trả về một đối tượng
                        // Jws<Claims>, chứa các claims và chữ ký của token.
                        .parseSignedClaims(token)

                        // Lấy payload của token, đây là phần chứa các claims.
                        .getPayload()
        );
    }

    public boolean verifyPasswordAndToken(String currentPassword, String token) {
        try {
            // 1. Giải mã token để lấy username
            String username = extractUsername(token);

            // 2. Lấy thông tin người dùng từ database dựa vào username
            User user = userRepository.findByUsername(username).get(); // Thay thế bằng phương thức của bạn

            if (user.getId() == null) {
                return false; // User không tồn tại
            }

            // 3. Giải mã password đã mã hóa (giả sử bạn đã mã hóa password trong database)
            String encodedPassword = user.getPassword(); // Lấy password đã mã hóa từ user

            // 4. So sánh mật khẩu mới với mật khẩu đã giải mã
            return new BCryptPasswordEncoder().matches(currentPassword, encodedPassword);
        } catch (Exception e) {
            // Xử lý exception (ví dụ: token không hợp lệ)
            e.printStackTrace();
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractClaims(token,Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration)
                .before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
