package com.xclone.learning.Controller;
import com.xclone.learning.APIResponse.WeatherResponse;
import com.xclone.learning.Entity.User;
import com.xclone.learning.Repository.UserRepository;
import com.xclone.learning.Services.ExternalAPI.WeatherServices;
import com.xclone.learning.Services.UserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private WeatherServices weatherServices;

    @Autowired
    private UserServices userServices;

    @Autowired
    private UserRepository userRepository;

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDB = userServices.findByUserName(userName);
        userInDB.setUserName(user.getUserName());
        userInDB.setPassword(user.getPassword());
        userServices.saveNewUser(userInDB);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserById(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUserName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping
    public ResponseEntity<?> greetings(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            WeatherResponse weatherResponse = weatherServices.getWeather("Mumbai");
            String greetings = "" ;
            if(weatherResponse!=null){
                greetings = ", weather feels like " + weatherResponse.getCurrent().getFeelslike();
            }
            return new ResponseEntity<>("Hi " + authentication.getName() + greetings,HttpStatus.OK);
        }catch (Exception e){
            logger.error("Error getting user :" ,e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
