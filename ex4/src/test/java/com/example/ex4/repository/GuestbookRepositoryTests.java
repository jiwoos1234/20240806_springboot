package com.example.ex4.repository;

import com.example.ex4.dto.GuestbookDTO;
import com.example.ex4.dto.PageRequestDTO;
import com.example.ex4.dto.PageResultDTO;
import com.example.ex4.entity.Guestbook;
import com.example.ex4.entity.QGuestbook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@SpringBootTest
class GuestbookRepositoryTests {

  @Autowired
  private GuestbookRepository guestbookRepository;

  @Test
  public void testRepository() {
    System.out.println(">>"+guestbookRepository.getClass().getName());
  }

  @Test
  public void insertDummies() {
    IntStream.rangeClosed(1, 300).forEach(i -> {
      Guestbook guestbook = Guestbook.builder()
          .title("Title... " + i)
          .content("Content... " + i)
          .writer("user"+(i%10))
          .build();
      System.out.println(guestbookRepository.save(guestbook));
    });
  }
  @Test
  public void testUpdate() {
    Optional<Guestbook> result = guestbookRepository.findById(300L);
    if (result.isPresent()) {
      Guestbook guestbook = result.get();
      guestbook.changeTitle("Changed Title...");
      guestbook.changeContent("Changed Content...");
      guestbookRepository.save(guestbook);
    }
  }

  //단일 항목 검색
  @Test
  public void testQuery1() {
    // Pageable :: 페이지 정보(첫페이지,갯수,정렬)를 가진 객체
    Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
    // 동적 질의를 위한 Guestbook 객체의 QGuestbook 준비
    QGuestbook qGuestbook = QGuestbook.guestbook;
    // 검색할 변수
    String keyword = "1";
    // 검색을 실행할 객체
    BooleanBuilder builder = new BooleanBuilder();
    // 검색 조건 설정할 객체
    BooleanExpression expression = qGuestbook.title.contains(keyword);
    // 검색 실행 준비 완료
    builder.and(expression);
    // 검색 실행 후 Page 객체에 담음.
    Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);
    result.stream().forEach(new Consumer<Guestbook>() {
      @Override
      public void accept(Guestbook guestbook) {
        System.out.println(guestbook);
      }
    });
  }

  // 다중 항목 검색
  @Test
  public void testQuery2() {
    Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
    QGuestbook qGuestbook = QGuestbook.guestbook;
    String keyword = "1";
    BooleanBuilder builder = new BooleanBuilder();
    BooleanExpression exTitle = qGuestbook.title.contains(keyword);
    BooleanExpression exContent = qGuestbook.content.contains(keyword);
    BooleanExpression exAll = exTitle.or(exContent);
    builder.and(exAll);
    builder.and(qGuestbook.gno.gt(0L)); //형식적이지만 추가해서 조건을 온전하게 함.
    Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);
    result.stream().forEach(guestbook -> System.out.println(guestbook));
  }

}