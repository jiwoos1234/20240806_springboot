package com.example.ex7.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/sample")
public class SampleController {

  @GetMapping("/all")
  public void exAll() {
    log.info("/all"); //  모든 이 접근
  }

  @GetMapping("/member")
  public void exMember() {
    log.info("/member");  // 로그인 사용자 접근
  }

  @GetMapping("/admin")
  public void exAdmin() {
    log.info("/admin"); // 로그인사용자 중 관리자만 접근
  }

  @GetMapping("/logout")
  public void exlogout() { log.info("/logout"); // 로그아웃
  }

}
