package com.hangaramit.foodriskmap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.hangaramit.foodriskmap.entity.Board;
import com.hangaramit.foodriskmap.entity.Member;
import com.hangaramit.foodriskmap.repository.BoardRepository;
import com.hangaramit.foodriskmap.repository.MemberRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class BoardController {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    HttpSession session;

    // 댓글 추가
    // @PostMapping("/board/comment/add")
    // public String addComment(Long boardId, String comment) {
    // Authentication authentication =
    // SecurityContextHolder.getContext().getAuthentication();
    // if (authentication == null || !authentication.isAuthenticated()) {
    // // 인증되지 않은 사용자를 로그인 페이지로 리다이렉트
    // return "redirect:/member/signin";
    // }
    // Member member = (Member) authentication.getPrincipal();
    // Board board = boardRepository.findById(boardId).orElse(null);
    // Comment newComment = new Comment();
    // newComment.setComment(comment);
    // newComment.setBoard(board);
    // newComment.setMember(member);
    // commentRepository.save(newComment);
    // return "redirect:/board/view?id=" + boardId;
    // }

    @GetMapping("/board/delete/{id}")
    public String boardDelete(@PathVariable("id") long id) {
        boardRepository.deleteById(id);
        return "redirect:/board/list";
    }

    @GetMapping("/board/update/{id}")
    public String boardUpdate(Model model, @PathVariable("id") long id) {
        Optional<Board> data = boardRepository.findById(id);
        Board board = data.get();
        model.addAttribute("board", board);
        return "board/update";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(
            @ModelAttribute Board board, @PathVariable("id") long id) {
        Member member = (Member) session.getAttribute("member_info");
        board.setMember(member);
        board.setId(id);
        Date d = new Date();
        board.setVstDate(d);
        boardRepository.save(board);
        return "redirect:/board/" + id;
    }

    @GetMapping("/board/{id}")
    public String boardView(Model model, @PathVariable("id") long id) {
        Optional<Board> data = boardRepository.findById(id);
        Board board = data.get();
        model.addAttribute("board", board);
        return "board/view";
    }

    @GetMapping("/board/list")
    public String boardList(
            Model model,
            @RequestParam(defaultValue = "1") int page) {

        Sort sort = Sort.by(Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, 10, sort);
        Page<Board> list = boardRepository.findAll(pageable);

        int totalPage = list.getTotalPages();
        int start = (page - 1) / 10 * 10 + 1;
        int end = start + 9;
        // 10 2
        if (end > totalPage) {
            end = totalPage;
        }

        model.addAttribute("list", list);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        return "board/list";
    }

    @GetMapping("/board/exception")
    public String exception() {
        return "/board/exception";
    }

    @GetMapping("/board/write")
    public String boardWrite() {
        return "board/write";
    }

    @PostMapping("/board/write")
    @Transactional(rollbackFor = { IOException.class })
    // 메소드가 시작될 때(before) 자동 저장(auto commit) 기능을 비활성화
    // 메소드가 종료될 때(after) 수동으로 commit 실행
    // RuntimeException 계열일 때 Rollback 기능 수행
    public String boardWrite(
            @ModelAttribute Board board) {
        // 제목 또는 내용을 작성하지 않은 경우 글쓰기 기능을 실행하지 않음

        // LocalDateTime ldt = LocalDateTime.now();
        // board.setPurDate(ldt);

        Date dt = new Date();
        board.setVstDate(dt);

        boardRepository.save(board);

        if (board.getTitle().equals("") || board.getContent().equals("")) {
            return "redirect:/board/write";

        }

        /* Board 데이터 입력 - 게시글 쓰기 */
        Member member = (Member) session.getAttribute("member_info");
        board.setMember(member);
        Board savedBoard = boardRepository.save(board);

        // throw new RuntimeException();
        // System.out.println(4 / 0); // 산술연산 예외! Unchecked Exception
        // new File("").createNewFile(); // Checked Exception

        /* AtchFile 데이터 입력 - 파일 첨부 */
        // 1. 파일 저장 transferTo()

        return "redirect:/board/" + savedBoard.getId();
    }

    @GetMapping("/board/search")
    public String listSearch(@RequestParam String search, Model model) {
        log.info(search);
        List<Board> list = boardRepository.findByTitleOrContentContainingIgnoreCase(search, search);
        log.info(list.toString());
        model.addAttribute("searchResults", list);
        return "board/list";
    }

    @GetMapping("/board/sidebar/")
    public String sidebar(Model model, @PathVariable("id") long id) {
        Optional<Board> data = boardRepository.findById(id);
        Board board = data.get();
        model.addAttribute("board", board);
        return "board/sidebar";
    }

    @GetMapping("/board/list/")
    public String board(Model model) {
        // vst_date를 DB에서 가져와서 Board 객체에 설정하는 로직을 작성합니다.
        Date currentDate = new Date();
        List<Board> boards = boardRepository.findByVstDate(currentDate); // 예시일 뿐, 실제로는 적절한 쿼리를 사용하세요.

        if (!boards.isEmpty()) {
            Board board = boards.get(0); // 리스트에서 첫 번째 Board 객체를 가져옵니다.
            model.addAttribute("vstDate", board.getVstDate()); // 모델에 vst_date 값을 추가합니다.
        }

        return "board/list/"; // 적절한 HTML 파일명을 반환합니다.
    }
}
