package com.example.ex5.repository;

import com.example.ex5.entity.Board;
import com.example.ex5.repository.search.SearchBoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {

  // 쿼리@을 활용하고 JPQL(Java Persistence Query Language)을 적용한다.
  // JPQL은 SQL을 추상화한 객체지향쿼리 언어이다. 테이블이 아닌 엔티티를 대상으로 쿼리함.
  // JPQL은 특정 데이터베이스의 SQL을 의존하지 않는다. 모든 데이터베이스에 공통으로 적용.
  // 엔티티와 속성은 대소문자를 구분한다.
  // 단점: 1)동적인 쿼리작성에 비효율적, 2) 문자열이라서 오류 있어도 정상작동 함.

  // Board 입장에서 Member를 참조하고 있는 경우 on 생략 가능: Board가 Member를 참조하고 있음.
  @Query("select b, w from Board b left join b.writer w where b.bno=:bno ")
  Object getBoardWithWriter(@Param("bno") Long bno);

  // Board 입장에서 Reply를 참조할 경우는 on 생략 불가: Board는 Reply에게 참조 당하고 있음.
  @Query("select b, r from Board b left join Reply r on r.board = b where b.bno=:bno ")
  List<Object[]> getBoardWithReply(@Param("bno") Long bno);

  // 목록페이지에 필요한 JPQL 작성
  // value는 게시글의 목록, countQuery는 목록의 갯수
  @Query(
      value = "select b, w, count(r) from Board b left join b.writer w " +
          "left join Reply r on r.board = b group by b "
      , countQuery = "select count(b) from Board b ")
  Page<Object[]> getBoardWithReplyCount(Pageable pageable);

  // 상세페이지에 필요한 JPQL 작성
  @Query("select b, w, count(r) from Board b left join b.writer w " +
      "left outer join Reply r on r.board = b " +
      "where b.bno=:bno ")
  Object getBoardByBno(@Param("bno") Long bno);
}
