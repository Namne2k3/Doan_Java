package blog_spring.blog_spring.config;

import blog_spring.blog_spring.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private JWTAuthFilter jwtAuthFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity

                // Tắt bảo vệ CSRF (Cross-Site Request Forgery).
                // CSRF là một loại tấn công mà kẻ tấn công lợi dụng để thực hiện các hành động
                // trái phép trên trang web của người dùng.
                // Việc tắt CSRF thường được thực hiện khi bạn xây dựng API không có giao diện web
                // hoặc khi bạn sử dụng token để xác thực.
                .csrf(AbstractHttpConfigurer::disable)

                // Kích hoạt CORS (Cross-Origin Resource Sharing) với các thiết
                // lập mặc định. CORS là một cơ chế bảo mật cho phép các tài nguyên
                // trên một trang web được yêu cầu từ một domain khác với domain của trang web đó.
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(request-> request
                        .requestMatchers("/api/v1/**").permitAll()
                        .requestMatchers("/auth/**", "/public/**").permitAll()
                        .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
                        .requestMatchers("/user/**").hasAnyAuthority("USER")
                        .requestMatchers("/adminuser/**").hasAnyAuthority("ADMIN", "USER")
                        .anyRequest().authenticated())

                // Đặt chính sách quản lý phiên là STATELESS.
                // Điều này có nghĩa là ứng dụng sẽ không lưu trữ thông tin phiên của người dùng trên máy chủ,
                // thường được sử dụng khi bạn xây dựng API RESTful.
                .sessionManagement(manager->manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Đặt cấu hình cho provider xác thực.
                // Provider xác thực chịu trách nhiệm kiểm tra thông tin xác thực của người dùng.
                .authenticationProvider(
                        authenticationProvider()
                    )
                    //  Thêm một bộ lọc JWT xác thực (jwtAuthFilter) trước bộ lọc
                    //  UsernamePasswordAuthenticationFilter. Điều này có nghĩa là JWT filter sẽ xử lý các yêu cầu
                    //  trước khi đến bộ lọc mặc định để xác thực tên người dùng và mật khẩu.
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // Cuối cùng, phương thức trả về "httpSecurity.build()" để hoàn tất và xây dựng cấu hình bảo mật.
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
}
