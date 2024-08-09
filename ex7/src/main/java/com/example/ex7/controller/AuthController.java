package com.example.ex7.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/auth")
public class AuthController {
  @GetMapping("/accessDenied")
  public void accessdenied() {
  }

  @GetMapping("/authenticationFailure")
  public void authenticationfailure() {
  }
}
