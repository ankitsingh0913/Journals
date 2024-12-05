package com.xclone.learning.Services;

import com.xclone.learning.Services.ExternalAPI.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {

    @Autowired
    private EmailService emailService;

    @Test
    public void sendEmailTest(){
        emailService.sendEmail("ankitsingh21062002@gmail.com","Testing JavaMailSender","Hi Aap kaise hai?");
    }
}
