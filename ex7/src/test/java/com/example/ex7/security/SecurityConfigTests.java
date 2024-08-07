package com.example.ex7.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecurityConfigTests {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  public void testEncode() {
    String pass = "1";
    String enPw = passwordEncoder.encode(pass);
    System.out.println("enPw: " + enPw);
//    assertEquals(pass, passwordEncoder.encode(enPw));
    System.out.println(passwordEncoder.matches(pass, enPw));
  }

}