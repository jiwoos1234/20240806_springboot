package com.example.ex7.controller;

import com.example.ex7.security.dto.ClubMemberAuthDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/sample")
public class SampleContorller {

  @GetMapping("/all")
  public void exAll() {log.info("/all");} //모든 이 접근

  @GetMapping("/manager")
  public void exManager(@AuthenticationPrincipal ClubMemberAuthDTO clubMemberAuthDTO) {
    log.info("/manager");
    log.info("clubMemberAuthDTO:", clubMemberAuthDTO);
  } // 로그인 사용자 접근


  @GetMapping("/admin")
  public void exAdmin() {log.info("/admin");} // 로그인 사용자중 관리자만 접근

  @GetMapping("/logout")
  public void exLogout() {log.info("/logout");} // 로그아웃

}
