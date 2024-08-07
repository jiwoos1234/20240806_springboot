package com.example.ex5.controller;

import com.example.ex5.dto.BoardDTO;
import com.example.ex5.dto.PageRequestDTO;
import com.example.ex5.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
  private final BoardService boardService;

  @RequestMapping({"","/","/list"})
  // @ModelAttribute("pageDTO") 커맨드객체를 다음페이지에 다른 이름으로 지정.
  public String list(PageRequestDTO pageDTO, Model model) {
    log.info("list...........");
    model.addAttribute(boardService.getList(pageDTO));
    return "/board/list";
  }

  @GetMapping("/register")
  public void registerGet() {
    log.info("register get.....");
  }

  @PostMapping("/register")
  public String registerPost(BoardDTO boardDTO, RedirectAttributes ra) {
    log.info("register post........");
    Long bno = boardService.register(boardDTO);
    ra.addFlashAttribute("msg", bno + "번이 등록");
    // redirect는 컨트롤러로 재전송한다는 의미
    return "redirect:/board/list";
  }

  @GetMapping({"/read", "/modify"})
  public void read(Long bno, PageRequestDTO pageRequestDTO, Model model) {
    BoardDTO boardDTO = boardService.get(bno);
    typeKeywordInit(pageRequestDTO);
    model.addAttribute("boardDTO", boardDTO);
  }

  @PostMapping("/modify")
  public String modify(BoardDTO boardDTO, PageRequestDTO pageRequestDTO,
                       RedirectAttributes ra) {
    boardService.modify(boardDTO);
    typeKeywordInit(pageRequestDTO);
    ra.addFlashAttribute("msg", boardDTO.getBno() + "번이 수정");
    ra.addAttribute("page", pageRequestDTO.getPage());
    ra.addAttribute("type", pageRequestDTO.getType());
    ra.addAttribute("keyword", pageRequestDTO.getKeyword());
    ra.addAttribute("bno", boardDTO.getBno());
    return "redirect:/board/read";
  }

  @PostMapping("/remove")
  public String remove(BoardDTO boardDTO, PageRequestDTO pageRequestDTO,
                       RedirectAttributes ra) {
    boardService.removeWithReplies(boardDTO.getBno());
    if (boardService.getList(pageRequestDTO).getDtoList().size() == 0 &&
        pageRequestDTO.getPage() != 1) {
      pageRequestDTO.setPage(pageRequestDTO.getPage() - 1);
    }
    typeKeywordInit(pageRequestDTO);
    ra.addFlashAttribute("msg", boardDTO.getBno() + "번이 삭제");
    ra.addAttribute("page", pageRequestDTO.getPage());
    ra.addAttribute("type", pageRequestDTO.getType());
    ra.addAttribute("keyword", pageRequestDTO.getKeyword());
    return "redirect:/board/list";
  }
  private void typeKeywordInit(PageRequestDTO pageRequestDTO){
    if (pageRequestDTO.getType().equals("null")) pageRequestDTO.setType("");
    if (pageRequestDTO.getKeyword().equals("null")) pageRequestDTO.setKeyword("");
  }
}
