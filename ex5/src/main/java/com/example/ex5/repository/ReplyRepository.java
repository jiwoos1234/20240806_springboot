package com.example.ex5.repository;

import com.example.ex5.entity.Board;
import com.example.ex5.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

  @Modifying  // update, delete를 사용할 때 무조건 붙임.
  @Query("delete from Reply r where r.board.bno = :bno")
  void deleteByBno(Long bno);

  // Query Method: 메서드이름이 곧 쿼리다.
  List<Reply> getRepliesByBoardOrderByRno(Board board);
}
