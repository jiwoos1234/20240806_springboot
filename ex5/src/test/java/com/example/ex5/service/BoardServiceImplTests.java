package com.example.ex5.service;

import com.example.ex5.dto.BoardDTO;
import com.example.ex5.dto.PageRequestDTO;
import com.example.ex5.dto.PageResultDTO;
import com.example.ex5.entity.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardServiceImplTests {

  @Autowired
  private BoardService boardService;

  @Test
  void register() {
    BoardDTO boardDTO = BoardDTO.builder()
        .title("Test")
        .content("Test Content")
        .writerEmail("user55@a.a")
        .build();
    Long bno = boardService.register(boardDTO);
  }

  @Test
  void getList() {
    PageRequestDTO pageRequestDTO = new PageRequestDTO();
    PageResultDTO<BoardDTO, Object[]> result = boardService.getList(pageRequestDTO);
    for (BoardDTO dto : result.getDtoList()) {
      System.out.println(dto);
    }
  }

  @Test
  void get() {
    Long bno = 100L;
    BoardDTO boardDTO = boardService.get(bno);
    System.out.println(boardDTO);
  }

  @Test
  void modify() {
    BoardDTO boardDTO = BoardDTO.builder()
        .bno(2L)
        .title("제목 변경")
        .content("내용 변경")
        .build();
    boardService.modify(boardDTO);
  }

  @Test
  void removeWithReplies() {
    boardService.removeWithReplies(1L);
  }
}