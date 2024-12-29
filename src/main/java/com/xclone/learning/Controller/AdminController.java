package com.xclone.learning.Controller;

import com.xclone.learning.Cache.AppCache;
import com.xclone.learning.Entity.User;
import com.xclone.learning.Services.UserServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin APIs", description = "Create Admin, Get All User")
public class AdminController {

    Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserServices userServices;

    @Autowired
    private AppCache appCache;

    @PostMapping("/create-admin-user")
    public ResponseEntity<?> createAdmin(@RequestBody User user){
        try {
            userServices.saveAdmin(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e){
            logger.info(e.toString());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers(){
        List<User> all = userServices.getAll();
        if(all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("clear-app-cache")
    public void appCache(){
        appCache.init();
    }


}
