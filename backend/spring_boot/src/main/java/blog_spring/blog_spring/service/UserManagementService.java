package blog_spring.blog_spring.service;

import blog_spring.blog_spring.dto.ReqRes;
import blog_spring.blog_spring.model.User;
import blog_spring.blog_spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserManagementService {
    @Autowired
    private UserRepository usersRepo;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ReqRes updateLockUsser(String userId, String isLock) {
        ReqRes reqRes = new ReqRes();
        try {
            var user = usersRepo.findById(userId).get();
            if ( user.getId() != null ) {

                if ( isLock.equals("true") ) {
                    user.setEnabled(false);
                } else {
                    user.setEnabled(true);
                }
                var savedUser = usersRepo.save(user);

                reqRes.setData(savedUser);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Cập nhật hoàn tất");
            } else {
                throw new Exception("Tài khoản không tồn tại");
            }

        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage(e.getMessage());
        }
        return reqRes;
    }

    public ReqRes changeRole(String userId, String role) {
        ReqRes reqRes = new ReqRes();
        try {
            var user = usersRepo.findById(userId).get();
            if ( user.getId() != null ) {

                user.setRole(role);
                var savedUser = usersRepo.save(user);

                reqRes.setData(savedUser);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Cập nhật quyền hoàn tất");
            } else {
                throw new Exception("Tài khoản không tồn tại");
            }

        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage(e.getMessage());
        }
        return reqRes;
    }

    public ReqRes verifyEmailUser(String userId) {
        ReqRes reqRes = new ReqRes();
        try {
            var user = usersRepo.findById(userId).get();
            if ( user.getId() != null ) {

                user.setIsEmailVerified(true);
                var savedUser = usersRepo.save(user);

               reqRes.setData(savedUser);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Xác thực email hoàn tất");
            } else {
                throw new Exception("Tài khoản không tồn tại");
            }

        } catch (Exception e) {
            reqRes.setStatusCode(400);
            reqRes.setMessage(e.getMessage());
        }
        return reqRes;
    }

    public ReqRes checkPasswordAndToken(String token, String password) {
        ReqRes reqRes = new ReqRes();
        try {
            boolean isPass = jwtUtils.verifyPasswordAndToken(password, token);
            if ( isPass == true ) {

                reqRes.setIsVerified(isPass);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Verified password successfully");
            } else {
                throw new Exception("Sai mật khẩu");
            }

        } catch (Exception e) {
            reqRes.setStatusCode(400);
            reqRes.setMessage(e.getMessage());
        }
        return reqRes;

    }

    public ReqRes findByEmail(String email) {
        ReqRes reqRes = new ReqRes();
        try {
            User user = usersRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not found"));
            reqRes.setData(user);
            reqRes.setStatusCode(200);

            reqRes.setMessage("Users with id '" + user.getId() + "' found successfully");

        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes register(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();

        try {

            var existedEmail = usersRepo.findByEmail(registrationRequest.getEmail()).orElse(null);
            if (existedEmail != null) {
                throw new Exception("Email đã tồn tại. Không thể tạo tài khoản");
            }

            User user = new User();
            user.setEmail(registrationRequest.getEmail());
            user.setRole("USER");
            user.setUsername(registrationRequest.getUsername());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            user.setPhone(registrationRequest.getPhone());
            user.setAddress(registrationRequest.getAddress());

            User savedUser = usersRepo.save(user);

            if ( savedUser.getId() != null ) {
                resp.setData(savedUser);
            } else {
                throw new Exception("Không thể tạo tài khoản");
            }

            authenticationManager

                    // authenticationManager.authenticate(...) gọi phương thức authenticate
                    // và truyền vào đối tượng UsernamePasswordAuthenticationToken.
                    // AuthenticationManager sau đó sẽ chuyển đối tượng Authentication này tới các
                    // AuthenticationProvider đã được cấu hình trong ứng dụng.
                    // ( Trong file SecurityConfig )
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    registrationRequest.getEmail(),
                                    registrationRequest.getPassword()
                            )
                    );

            var userFinded = usersRepo.findByEmail(registrationRequest.getEmail()).orElseThrow();
            var jwt = jwtUtils.generateToken(userFinded);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), userFinded);

            resp.setStatusCode(200);
            resp.setToken(jwt);
            resp.setRole(userFinded.getRole());
            resp.setRefreshToken(refreshToken);
            resp.setExpirationTime("24Hrs");
            resp.setMessage("Đăng ký tài khoản thành công");

        } catch(Exception e) {

            resp.setStatusCode(500);
            resp.setMessage(e.getMessage());

        }
        return resp;
    }

    public ReqRes login(ReqRes loginRequest) {
        ReqRes resp = new ReqRes();

        try {

            // authenticationManager là một đối tượng của AuthenticationManager,
            // một giao diện cốt lõi trong Spring Security để xử lý xác thực.
            authenticationManager

                    // authenticationManager.authenticate(...) gọi phương thức authenticate
                    // và truyền vào đối tượng UsernamePasswordAuthenticationToken.
                    // AuthenticationManager sau đó sẽ chuyển đối tượng Authentication này tới các
                    // AuthenticationProvider đã được cấu hình trong ứng dụng.
                    // ( Trong file SecurityConfig )
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginRequest.getEmail(),
                                    loginRequest.getPassword()
                            )
                    );

            var user = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow();

            if (!user.getIsEmailVerified()) {
                throw new Exception("Email chưa được xác thực. Vui lòng xác thực email!");
            }

            if ( !user.getEnabled() ) {
                throw new Exception("Tài khoản của bạn đã bị khóa. Vui lòng thử lại sau");
            }

            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

            resp.setStatusCode(200);
            resp.setToken(jwt);
            resp.setRole(user.getRole());
            resp.setRefreshToken(refreshToken);
            resp.setExpirationTime("24Hrs");
            resp.setMessage("Đăng nhâp thành công");

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setMessage(e.getMessage());
        }

        return resp;
    }

    public ReqRes refreshToken(ReqRes refreshTokenRequest) {
        ReqRes resp = new ReqRes();
        try {

            String email = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            User user = usersRepo.findByEmail(email).orElseThrow();

            if ( jwtUtils.isTokenValid(refreshTokenRequest.getToken(), user) ) {
                var jwt = jwtUtils.generateToken(user);
                resp.setStatusCode(200);
                resp.setToken(jwt);
                resp.setRefreshToken(refreshTokenRequest.getToken());
                resp.setExpirationTime("24Hrs");
                resp.setMessage("Successfully Refreshed Token");
            }
            resp.setStatusCode(200);

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setMessage(e.getMessage());

        }
        return resp;
    }

    public ReqRes getAllUsers(String search) {
        ReqRes reqRes = new ReqRes();

        try {
            List<User> result = usersRepo.findAll();

            if ( search != null && !search.equals("undefined")) {
                result = result.stream().filter(
                        p -> p.getUsername().toLowerCase().contains(search) ||
                                p.getEmail().toLowerCase().contains(search) ||
                                p.getPhone().toLowerCase().contains(search)
                ).toList();
            }

            if (!result.isEmpty()) {
                reqRes.setDataList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }

    public ReqRes getUsersById(String id) {
        ReqRes reqRes = new ReqRes();
        try {
            User usersById = usersRepo.findById(String.valueOf(id)).orElseThrow(() -> new RuntimeException("User Not found"));
            reqRes.setData(usersById);
            reqRes.setStatusCode(200);
            System.out.println("Check user >>> " + usersById);
            System.out.println("Check reqres >>> " + reqRes);

            reqRes.setMessage("Users with id '" + usersById.getId() + "' found successfully");

        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes deleteUser(String userId) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<User> userOptional = usersRepo.findById(String.valueOf(userId));
            if (userOptional.isPresent()) {
                usersRepo.deleteById(String.valueOf(userId));
                reqRes.setStatusCode(200);
                reqRes.setMessage("Đã xóa tài khoản");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("Không tim thấy tài khoản");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Có lỗi xảy ra khi xóa tài khoản: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes updateUser(String userId, User updatedUser) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<User> userOptional = usersRepo.findById(String.valueOf(userId));
            if (userOptional.isPresent()) {
                User existingUser = userOptional.get();
                existingUser.setUsername(updatedUser.getUsername());
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setPhone(updatedUser.getPhone());
                existingUser.setAddress(updatedUser.getAddress());
                existingUser.setIsEmailVerified(updatedUser.getIsEmailVerified());
                existingUser.setEnabled(updatedUser.getEnabled());
                existingUser.setUpdatedAt(new Date());

                // Check if password is present in the request
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    // Encode the password and update it
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }

                User savedUser = usersRepo.save(existingUser);
                reqRes.setData(savedUser);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Dữ liệu đã được cập nhật");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("Không tim thấy tài khoản");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes getMyInfo(String email){
        ReqRes reqRes = new ReqRes();
        try {
            Optional<User> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                reqRes.setData(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }

        }catch (Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return reqRes;

    }
}
