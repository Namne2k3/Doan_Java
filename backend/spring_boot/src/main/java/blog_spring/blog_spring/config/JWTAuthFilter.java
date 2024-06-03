package blog_spring.blog_spring.config;

import blog_spring.blog_spring.service.JWTUtils;
import blog_spring.blog_spring.service.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        if ( authHeader == null || authHeader.isBlank() ) {
            filterChain.doFilter(request, response);
            return;
        }
        // Trích xuất JWT từ tiêu đề Authorization, bỏ qua phần tiền tố "Bearer ".
        // Tiêu đề Authorization thường có dạng Bearer <jwt_token>,
        // do đó, substring(7) sẽ lấy phần chuỗi sau "Bearer " (bỏ qua 7 ký tự đầu tiên).
        jwtToken = authHeader.substring(7);
        userEmail = jwtUtils.extractUsername(jwtToken);

        // Kiểm tra xem ngữ cảnh bảo mật hiện tại đã có thông tin xác thực chưa.
        // Nếu chưa, nghĩa là người dùng chưa được xác thực trong ngữ cảnh bảo mật hiện tại.
        if ( userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailService.loadUserByUsername(userEmail);

            if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
                // Tạo một ngữ cảnh bảo mật trống mới.
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                // Tạo một đối tượng UsernamePasswordAuthenticationToken chứa
                // thông tin xác thực của người dùng, gồm userDetails và các quyền (authorities) của họ.
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //  Thiết lập thông tin xác thực vào ngữ cảnh bảo mật mới.
                securityContext.setAuthentication(token);

                // Đặt ngữ cảnh bảo mật mới vào SecurityContex  tHolder,
                // làm cho thông tin xác thực có hiệu lực trong suốt vòng đời của yêu cầu HTTP hiện tại.
                SecurityContextHolder.setContext(securityContext);
            }
        }
        filterChain.doFilter(request, response);
    }
}
