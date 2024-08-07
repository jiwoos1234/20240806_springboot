package com.example.ex2.repository;

import com.example.ex2.entity.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {

  // 쿼리메서드(Query Method): 메서드이름자체가 질의문이다.
  List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

  List<Memo> findByMemoTextContaining(String search);
  //  List<Memo> findByMemoTextNotLike(String search);

  Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

  void deleteMemoByMnoLessThan(Long num);

  //@Query은 쿼리메서드로는 해결되지 않는 경우 직접 쿼리를 작성
  @Query("select m from Memo m order by m.mno desc")
  List<Memo> getListDesc();

  //매개변수를 쿼리에 각각 전달할 때
  //@Param 사용시 쿼리의 :변수명과 @Param의 속성명은 반드시 일치
  @Transactional
  @Modifying
  @Query("update Memo m set m.memoText = :memoText1 where m.mno = :mno1 ")
  int updateMemoText(@Param("mno1") Long mno, @Param("memoText1") String memoText);

  //매개변수를 쿼리에 객체로 전달할 때
  @Transactional
  @Modifying
  @Query(
      "update Memo m set m.memoText = :#{#param.memoText} " +
          "where m.mno = :#{#param.mno} ")
  int updateMemoText(@Param("param") Memo memo);

  // Pageable 객체를 처리할 경우 목록과 갯수가 필요함. value와 countQuery 지정
  // @Param 미사용시 쿼리의 :변수명과 메서드의 매개 변수명은 반드시 일치
  @Query(value = "select m from Memo m where m.mno > :mno",
      countQuery = "select count(m) from Memo m where m.mno > :mno")
  Page<Memo> getListWithQuery(Long mno, Pageable pageable);

  // 페이징 처리를 하면서 다양한 속성들을 담아야 할 때 Page<Object[]>
  @Query(value = "select m.mno, m.memoText, CURRENT_DATE from Memo m " +
      "where m.mno > :mno",
      countQuery = "select count(m) from Memo m where m.mno > :mno")
  Page<Object[]> getListWithQueryObject(Long mno, Pageable pageable);

  // 데이터베이스 고유의 SQL 구문을 사용할 경우 nativeQuery = true
  @Query(value = "select * from tbl_memo where mno > 0", nativeQuery = true)
  List<Memo> getNativeResult();
}
