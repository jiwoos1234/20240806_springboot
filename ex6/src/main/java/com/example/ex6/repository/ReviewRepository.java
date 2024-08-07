package com.example.ex6.repository;

import com.example.ex6.entity.Member;
import com.example.ex6.entity.Movie;
import com.example.ex6.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

  @EntityGraph(attributePaths = {"member"},
      type = EntityGraph.EntityGraphType.FETCH)
  List<Review> findByMovie(Movie movie);

  // 쿼리메서드나, deleteById등은 한건씩 진행을 한다.
  // @Query를 사용해서 delete, update를 할 경우 Bulk연산을 함
  // 그래서 트랜잭션을 복수개 할 것을 한번에 처리하기 때문에
  // 복수의 트랜잭션을 한번에 처리하기 위해 @Modifying
  @Modifying
  @Query("delete from Review r where r.member = :member")
  void deleteByMember(Member member);
}
