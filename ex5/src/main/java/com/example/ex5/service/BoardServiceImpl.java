package com.example.ex5.service;

import com.example.ex5.dto.BoardDTO;
import com.example.ex5.dto.PageRequestDTO;
import com.example.ex5.dto.PageResultDTO;
import com.example.ex5.entity.Board;
import com.example.ex5.entity.Member;
import com.example.ex5.repository.BoardRepository;
import com.example.ex5.repository.ReplyRepository;
import org.springframework.transaction.annotation.Transactional;
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
public class BoardServiceImpl implements BoardService {
  private final BoardRepository boardRepository;
  private final ReplyRepository replyRepository;

  @Override
  public Long register(BoardDTO boardDTO) {
    log.info("register.....");
    Board board = dtoToEntity(boardDTO);
    boardRepository.save(board);
    return board.getBno();
  }

  @Override
  public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
    log.info(pageRequestDTO);
    Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board) en[0], (Member) en[1], (Long) en[2]));
    // Querydsl(동적 검색) 없는 페이징 처리
    //    Page<Object[]> result = boardRepository.getBoardWithReplyCount(
//        pageRequestDTO.getPageable(Sort.by("bno").descending()));

    // Querydsl(동적 검색)을 활용한 페이징 처리
    Page<Object[]> result = boardRepository.searchPage(
        pageRequestDTO.getType(),
        pageRequestDTO.getKeyword(),
        pageRequestDTO.getPageable(Sort.by("bno").descending()));

    return new PageResultDTO<>(result, fn);
  }

  @Override
  public BoardDTO get(Long bno) {
    Object result = boardRepository.getBoardByBno(bno);
    Object[] arr = (Object[]) result;
    return entityToDTO((Board) arr[0], (Member) arr[1], (Long) arr[2]);
  }

  @Override
  public void modify(BoardDTO boardDTO) {
    Optional<Board> result = boardRepository.findById(boardDTO.getBno());
    if (result.isPresent()) {
      Board board = result.get();
      board.changeTitle(boardDTO.getTitle());
      board.changeContent(boardDTO.getContent());
      boardRepository.save(board);
    }
  }

  @Transactional
  @Override
  public void removeWithReplies(Long bno) {
    replyRepository.deleteByBno(bno);
    boardRepository.deleteById(bno);
  }
}
