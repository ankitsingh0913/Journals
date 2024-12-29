package com.xclone.learning.Controller;

import com.xclone.learning.DTO.UserDTO;
import com.xclone.learning.Entity.User;
import com.xclone.learning.Services.UserDetailsServiceIMPL;
import com.xclone.learning.Services.UserServices;
import com.xclone.learning.Utills.JWTUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
@Tag(name = "Public APIs",description = "Health Status, Signup, Login User")
public class PublicController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceIMPL userDetailsServiceIMPL;
    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UserServices userServices;

    @GetMapping("/health-check")
    public String healthCheck(){
        return "ok";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDTO user){
        try {
            User newUser = new User();
            newUser.setEmail(user.getEmail());
            newUser.setUserName(user.getUserName());
            newUser.setPassword(user.getPassword());
            newUser.setSentimentAnalysis(user.isSentimentAnalysis());
            userServices.saveNewUser(newUser);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword()));
            UserDetails userDetails = userDetailsServiceIMPL.loadUserByUsername(user.getUserName());
            String jwt = jwtUtils.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt,HttpStatus.CREATED);
        } catch (Exception e){
            log.error("Exception occurred while createAuthenticationToken", e);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }
    }
}
