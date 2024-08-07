package com.example.ex4.service;

import com.example.ex4.dto.GuestbookDTO;
import com.example.ex4.dto.PageRequestDTO;
import com.example.ex4.dto.PageResultDTO;
import com.example.ex4.entity.Guestbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GuestbookServiceTests {

  @Autowired
  private GuestbookService guestbookService;

  @Test
  void testRegister() {
    GuestbookDTO dto = GuestbookDTO.builder()
        .title("new Title")
        .content("new Content")
        .writer("user1")
        .build();
    Long gno = guestbookService.register(dto);
    System.out.println(gno);
  }

  @Test
  public void testList() {
    PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
        .page(1).size(10).build();
    PageResultDTO<GuestbookDTO, Guestbook> resultDTO =
        guestbookService.getList(pageRequestDTO);
    for (GuestbookDTO dto : resultDTO.getDtoList()) {
      System.out.println(dto);
    }
    System.out.println("PREV:"+resultDTO.isPrev());
    System.out.println("NEXT:"+resultDTO.isNext());
    System.out.println("TOTAL:"+resultDTO.getTotalPage());
    System.out.println("=======================================================");
    for (GuestbookDTO guestbookDTO : resultDTO.getDtoList()) {
      System.out.println(guestbookDTO);
    }
    System.out.println("=======================================================");
    resultDTO.getPageList().forEach(i -> {
      if(i!=1) System.out.print(", ");
      System.out.print(i);
    });
    System.out.println();
  }
}