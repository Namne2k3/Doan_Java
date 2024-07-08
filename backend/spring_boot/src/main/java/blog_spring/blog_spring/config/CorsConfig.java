package blog_spring.blog_spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {

        // Đây là một phương thức trả về một WebMvcConfigurer.
        // WebMvcConfigurer là một giao diện trong Spring MVC cho phép bạn tùy chỉnh
        // các cấu hình mặc định của Spring MVC.
        return new WebMvcConfigurer() {

            // Phương thức này được sử dụng để cấu hình CORS cho ứng dụng.
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        // Cho phép tất cả các đường dẫn (URLs) trong ứng dụng được áp dụng cấu hình CORS.
                        .addMapping("/**")

                        // Cho phép các phương thức HTTP GET, POST, PUT, và DELETE từ các nguồn gốc khác.
                        .allowedMethods("GET", "POST", "PUT", "DELETE")

                        // Cho phép tất cả các nguồn gốc (domains) được phép truy cập vào tài nguyên
                        // của ứng dụng. Dấu "*" có nghĩa là không giới hạn nguồn gốc nào cả,
                        // bất kỳ nguồn gốc nào cũng có thể gửi yêu cầu.
                        .allowedOrigins(
                                "http://localhost:3000",
                                "https://4415krnq-3000.asse.devtunnels.ms",
                                "https://justtechshop.netlify.app"
                        )
                        .allowCredentials(true);
            }
        };
    }
}
