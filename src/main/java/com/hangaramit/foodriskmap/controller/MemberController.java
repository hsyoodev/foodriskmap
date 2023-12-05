package com.hangaramit.foodriskmap.controller;

import com.hangaramit.foodriskmap.config.EmailValidator;
import com.hangaramit.foodriskmap.config.SecurityConfig;
import com.hangaramit.foodriskmap.entity.Member;
import com.hangaramit.foodriskmap.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class MemberController {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    SecurityConfig securityConfig;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    HttpSession session;

    // 회원가입 시 이메일유효성 확인
    @Autowired
    EmailValidator emailValidator;

    // 회원가입
    @GetMapping("/member/signup")
    public String singup(Model model) {
        model.addAttribute("isMatch", true);
        return "member/join"; // 회원가입 페이지 html파일
    }

    // 회원가입(비밀번호 암호화 저장)
    @PostMapping("member/signup")
    public String signUpPost(@ModelAttribute Member member) {
        String memberPwd = member.getPwd();
        String encodedPwd = passwordEncoder.encode(memberPwd);
        member.setPwd(encodedPwd);
        memberRepository.save(member);
        return "redirect:/member/signin"; // 로그인 페이지 url
    }

    // 로그인(signin)
    @GetMapping("member/signin")
    public String signin() {
        return "member/login";
    }

    // 등록된 이메일확인

    // 회원가입 시 이메일유효성 확인

    @GetMapping("member/id-check")
    @ResponseBody
    public Map<String, Object> idCheck(String email) {
        Member member = memberRepository.findByEmail(email);
        Map<String, Object> result = new HashMap<>();

        if (member != null) {
            result.put("code", 200);
            result.put("msg", "사용할 수 없는 이메일입니다.");
        } else {
            boolean isVaild = emailValidator.isValidEmail(email);
            if (isVaild) {
                result.put("code", 300);
                result.put("msg", "가입 가능한 이메일입니다.");
            } else {
                result.put("code", 200);
                result.put("msg", "사용할 수 없는 이메일입니다.");

            }
        }
        return result;
    }

    // 로그인(암호화된 비번확인)
    @PostMapping("/member/signin")
    public String signInPost(@ModelAttribute Member member, Model model) {
        Member dbMember = memberRepository.findByEmail(member.getEmail());
        String dbPwd = dbMember != null ? dbMember.getPwd() : null;
        String memberPwd = member.getPwd();
        boolean isMatch = false;
        // isMatch 비번일치, 기회원확인
        if (dbPwd != null) {
            isMatch = passwordEncoder.matches(memberPwd, dbPwd);
        }
        // log.info("isMatch : {}", isMatch);
        if (isMatch) {
            session.setAttribute("member_info", dbMember);
            log.info("dbMember : {}", dbMember);
            return "redirect:/board/list"; // 게시판 페이지 url
        } else
        // log.info("isMatch : {}", isMatch);
        {
            model.addAttribute("isMatch", isMatch);
        }
        return "member/login"; // 로그인 페이지 html 모델은 redirect를 사용하지 않는다
    }

    // 로그아웃
    @GetMapping("/member/signout")
    public String signOut() {
        session.invalidate();
        return "redirect:/member/signin"; // 로그인 페이지 url
    }

}
