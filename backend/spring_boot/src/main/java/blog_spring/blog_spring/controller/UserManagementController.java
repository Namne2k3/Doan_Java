package blog_spring.blog_spring.controller;

import blog_spring.blog_spring.dto.ReqRes;
import blog_spring.blog_spring.dto.VerifyPasswordRequest;
import blog_spring.blog_spring.model.User;
import blog_spring.blog_spring.service.JWTUtils;
import org.springframework.security.core.Authentication;
import blog_spring.blog_spring.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserManagementController {
    @Autowired
    private UserManagementService usersManagementService;

    @Autowired
    private JWTUtils jwtUtils;

    @GetMapping("/api/v1/users")
    public ResponseEntity<ReqRes> getUsers(){
        return ResponseEntity.ok(usersManagementService.getAllUsers());
    }

    @PostMapping("/auth/register")
    public ResponseEntity<ReqRes> register(@RequestBody ReqRes reg){
        return ResponseEntity.ok(usersManagementService.register(reg));
    }

    @PutMapping("/auth/update")
    public ResponseEntity<ReqRes> updateUser(@RequestBody User user){
        return ResponseEntity.ok(usersManagementService.updateUser(user.getId(), user));
    }

    @PostMapping("/auth/verifyPasswordToken")
    public ResponseEntity<ReqRes> verifyPasswordToken(@RequestParam("token") String token,
                                                       @RequestParam("password") String password) {
        return ResponseEntity.ok(usersManagementService.checkPasswordAndToken(token, password));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes req){
        return ResponseEntity.ok(usersManagementService.login(req));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes req){
        return ResponseEntity.ok(usersManagementService.refreshToken(req));
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<ReqRes> getAllUsers(){
        return ResponseEntity.ok(usersManagementService.getAllUsers());

    }

    @GetMapping("/admin/get-users/{userId}")
    public ResponseEntity<ReqRes> getUSerByID(@PathVariable String userId){
        return ResponseEntity.ok(usersManagementService.getUsersById(userId));
    }

//    @PutMapping("/admin/update/{userId}")
//    public ResponseEntity<ReqRes> updateUser(@PathVariable String userId, @RequestBody User reqres){
//        return ResponseEntity.ok(usersManagementService.updateUser(userId, reqres));
//    }

    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<ReqRes> getMyProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ReqRes response = usersManagementService.getMyInfo(email);
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<ReqRes> deleteUSer(@PathVariable String userId){
        return ResponseEntity.ok(usersManagementService.deleteUser(userId));
    }
}
