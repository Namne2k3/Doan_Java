package blog_spring.blog_spring.controller;

import blog_spring.blog_spring.dto.ReqRes;
import blog_spring.blog_spring.model.User;
import blog_spring.blog_spring.service.EmailService;
import blog_spring.blog_spring.service.JWTUtils;
import blog_spring.blog_spring.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/emails")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UserManagementService userManagementService;

    @PostMapping("/send")
    public String sendEmail(@RequestParam String to,
                            @RequestParam String subject,
                            @RequestParam String body,
                            @RequestParam(required = false, defaultValue = "false") boolean isHtml) {
        if (isHtml) {
            emailService.sendHtmlEmail(to, subject, body);
        } else {
            emailService.sendSimpleEmail(to, subject, body);
        }
        return "Email đã được gửi!";
    }

    @PostMapping("/recovery_password")
    public ReqRes recoveryPassword_post(@Validated @RequestParam String email) {
        ReqRes reqRes = new ReqRes();
        try {
            var user = (User)userManagementService.findByEmail(email).getData();

            if ( user != null ) {
                var token = jwtUtils.generateToken(user);
                emailService.sendSimpleEmail(email,"Đặt lại mật khẩu","https://justtechshop.netlify.app/submit_recovery_password/" + token );
                reqRes.setStatusCode(200);
                reqRes.setMessage("Email được gửi!");
            }
            else {
                throw new Exception("Email chưa được đăng ký tài khoản!");
            }

        } catch (Exception e) {
            reqRes.setStatusCode(404);
            reqRes.setMessage(e.getMessage());
        }
        return reqRes;
    }

    @PostMapping("/verify")
    public ReqRes verifyEmail(@Validated @RequestParam String email) {
        ReqRes reqRes = new ReqRes();
        try {
            var user = (User) userManagementService.findByEmail(email).getData();

            if ( user != null ) {
                var token = jwtUtils.generateToken(user);
                emailService.sendSimpleEmail(email,"Nhấp vào đây để xác thực email","https://justtechshop.netlify.app/verifyEmail/" + token );
                reqRes.setStatusCode(200);
                reqRes.setMessage("Email được gửi!");
            }
            else {
                throw new Exception("Email chưa được đăng ký tài khoản!");
            }

        } catch (Exception e) {
            reqRes.setStatusCode(404);
            reqRes.setMessage(e.getMessage());
        }
        return reqRes;
    }
}
