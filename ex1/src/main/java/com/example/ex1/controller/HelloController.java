package com.example.ex1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// SSR(Server Side Rendering): 웹페이지를 주로 전송하는 방식
@Controller
@RequestMapping("/")
public class HelloController {
// view를 위한 Template Engine이 추가 되어 있지 않은 경우는 static활용
// static일 경우 컨트롤러의 주소가 매핑되지 않아도 사용 가능.
//  @GetMapping("/hello")
//  public String hello() {
//    return "hello.html";
//  }
}
