package blog_spring.blog_spring.controller;

import blog_spring.blog_spring.model.User;
import blog_spring.blog_spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
public class UserController {

//    @Autowired
//    UserRepository userRepository;
//
//    @PostMapping("/api/v1/users")
//    public ResponseEntity<User> addUser(@RequestBody User userBody) {
//        User newUser = userRepository.save(userBody);
//        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
//    }
//
//    @GetMapping("/api/v1/users")
//    public List<User> getAll() {
//        return userRepository.findAll();
//    }
}
