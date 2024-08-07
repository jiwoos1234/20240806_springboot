package com.example.ex5.service;

import com.example.ex5.dto.ReplyDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReplyServiceImplTests {

  @Autowired
  ReplyService replyService;

  @Test
  void register() {
  }

  @Test
  void getList() {
    Long bno = 50L;
    List<ReplyDTO> list = replyService.getList(bno);
    list.forEach(new Consumer<ReplyDTO>() {
      @Override
      public void accept(ReplyDTO replyDTO) {
        System.out.println(replyDTO);
      }
    });
  }

  @Test
  void modify() {
  }

  @Test
  void remove() {
  }
}