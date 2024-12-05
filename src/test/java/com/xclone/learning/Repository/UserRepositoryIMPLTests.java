package com.xclone.learning.Repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryIMPLTests {
    @Autowired
    private UserRepositoryIMPL userRepositoryIMPL;

    @Test
    public void testUser(){
        Assertions.assertNotNull(userRepositoryIMPL.getUserForSA());
    }
}
