package com.example.ex5.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReplyRepositoryTests {

  @Autowired
  ReplyRepository replyRepository;

  @Test
  public void insertReplies() {
    /*IntStream.rangeClosed(1, 300).forEach(i -> {
      long bno = (long) (Math.random() * 100) + 1;
      long mno = (long) (Math.random() * 100) + 1;
      Board board = Board.builder().bno(bno).build();
      Reply reply = Reply.builder()
          .text("Reply...."+i)
          .replyer("user"+mno+"@a.a")
          .board(board)
          .build();
      replyRepository.save(reply);
    });*/
  }
}