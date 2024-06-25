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

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
                resp.setMessage("Saved User Successfully!");
                resp.setStatusCode(200);
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
            resp.setMessage("Registered Successfully!");

        } catch(Exception e) {

            resp.setStatusCode(500);
            resp.setError(e.getMessage());

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
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

            resp.setStatusCode(200);
            resp.setToken(jwt);
            resp.setRole(user.getRole());
            resp.setRefreshToken(refreshToken);
            resp.setExpirationTime("24Hrs");
            resp.setMessage("Successfully Logged In!");

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

    public ReqRes getAllUsers() {
        ReqRes reqRes = new ReqRes();

        try {
            List<User> result = usersRepo.findAll();
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
                reqRes.setMessage("User deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes updateUser(String userId, User updatedUser) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<User> userOptional = usersRepo.findById(String.valueOf(userId));
            if (userOptional.isPresent()) {
                User existingUser = userOptional.get();
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setUsername(updatedUser.getUsername());
                existingUser.setRole(updatedUser.getRole());

                // Check if password is present in the request
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    // Encode the password and update it
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }

                User savedUser = usersRepo.save(existingUser);
                reqRes.setData(savedUser);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User updated successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
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
