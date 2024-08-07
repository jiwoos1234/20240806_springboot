package com.example.ex4.service;

import com.example.ex4.dto.GuestbookDTO;
import com.example.ex4.dto.PageRequestDTO;
import com.example.ex4.dto.PageResultDTO;
import com.example.ex4.entity.Guestbook;
import com.example.ex4.entity.QGuestbook;
import com.example.ex4.repository.GuestbookRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class GuestbookServiceImpl implements GuestbookService {

  private final GuestbookRepository guestbookRepository;

  @Override
  public Long register(GuestbookDTO dto) {
    Guestbook guestbook = dtoToEntity(dto);
    guestbookRepository.save(guestbook);
    return guestbook.getGno();
  }

  @Override
  public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO pageRequestDTO) {
    // 알고자하는 페이지 요청에 대한 정보 객체(번호, 갯수, 정렬, 검색타입, 검색 키워드)
    Pageable pageable = pageRequestDTO.getPageable(Sort.by("gno").descending());

    // 동적 검색(QueryDSL)의 조건이 담긴 객체 :: BooleanBuilder
    BooleanBuilder booleanBuilder = getSearch(pageRequestDTO);
    // 복수의 데이터를 처리할 때
    // repository에서 복수개의 데이터를 받을 때 페이징 필요하면   Page<>를 활용
    // repository에서 복수개의 데이터를 받을 때 페이징 불필요하면 List<>를 활용
    // Page<Guestbook> 원하는 페이지의 목록(복수)

    // 요청 페이지정보 객체와 동적검색 조건객체의 조건을 통해 리포지터리에서 검색한 결과를 => Page

    Page<Guestbook> result = guestbookRepository.findAll(booleanBuilder, pageable);

    //결과 객체인 Page의 원소 각각에 대해 Guestbook을 GuestbookDTO로 변환해 주는 함수
    Function<Guestbook, GuestbookDTO> fn = new Function<Guestbook, GuestbookDTO>() {
      @Override
      public GuestbookDTO apply(Guestbook guestbook) {
        return entityToDto(guestbook);
      }
    };
    // result는 Page<Guestbook>이며 이것은 List<GuestbookDTO>로 변환해야 함.
    // 그래서, fn에 result의 원소(Guestbook)를 GuestbookDTO로 변환기능 정의.
    return new PageResultDTO<>(result, fn);
  }

  private BooleanBuilder getSearch(PageRequestDTO pageRequestDTO) {
    String type = pageRequestDTO.getType();
    String keyword = pageRequestDTO.getKeyword();
    // 동적 검색을 하기 위한 객체 생성
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    QGuestbook qGuestbook = QGuestbook.guestbook; // 검색 대상인 QGuestbook
    BooleanExpression expression = qGuestbook.gno.gt(0L);//전체조건부여
    booleanBuilder.and(expression); // 첫번째 조건을 적용

    BooleanBuilder conditionBuilder = new BooleanBuilder(); // 세부 검색 조건 객체
    if(type == null || type.trim().length() == 0) return booleanBuilder;
    if(type.contains("t")) conditionBuilder.or(qGuestbook.title.contains(keyword));
    if(type.contains("c")) conditionBuilder.or(qGuestbook.content.contains(keyword));
    if(type.contains("w")) conditionBuilder.or(qGuestbook.writer.contains(keyword));
    booleanBuilder.and(conditionBuilder);
    return booleanBuilder;
  }

  @Override
  public GuestbookDTO read(Long gno) {
    // 단수의 데이터를 처리할 때 : findById를 통해서 유일한 하나의 객체를 찾아보는 것.
    // Optional의 특징 : null 값을 받아도 에러가 발생하지 않고, 형변환 안해도 안전
    Optional<Guestbook> result = guestbookRepository.findById(gno);
//    GuestbookDTO guestbookDTO = null;
//    if(result.isPresent()) {
//      guestbookDTO = entityToDto(result.get());
//    }
//    return guestbookDTO;
    return result.isPresent() ? entityToDto(result.get()) : null;
  }

  @Override
  public void modify(GuestbookDTO guestbookDTO) {
    // 수정하기위해서는 먼저 해당 항목을 검색하여 객체를 획득한 후에 필요 부분만 수정.
    Optional<Guestbook> result = guestbookRepository.findById(guestbookDTO.getGno());
    if (result.isPresent()) {
      Guestbook guestbook = result.get();
      guestbook.changeTitle(guestbookDTO.getTitle());
      guestbook.changeContent(guestbookDTO.getContent());
      guestbookRepository.save(guestbook);
    }
  }

  @Override
  public void remove(GuestbookDTO guestbookDTO) {
    Optional<Guestbook> result = guestbookRepository.findById(guestbookDTO.getGno());
    if (result.isPresent()) {
      guestbookRepository.deleteById(guestbookDTO.getGno());
    }
  }
}