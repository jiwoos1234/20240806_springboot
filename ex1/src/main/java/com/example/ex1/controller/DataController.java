package com.example.ex1.controller;

import com.example.ex1.dto.Foo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


// CSR(Client Side Rendering): 웹페이지를 제외한 데이터 전송 방식
@RestController
@RequestMapping("/data")
public class DataController {
  @GetMapping("/foo")
  public String getFoo() {
    return new Foo().toString();
  }
}
