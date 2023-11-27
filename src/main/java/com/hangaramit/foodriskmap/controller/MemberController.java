package com.hangaramit.foodriskmap.controller;

import com.hangaramit.foodriskmap.config.SecurityConfig;
import com.hangaramit.foodriskmap.entity.Member;
import com.hangaramit.foodriskmap.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {

    @Autowired
    MemberRepository memberRepository;

    @GetMapping("member/signin")
    public String signin() {
        return "member/signin";
    }

    @Autowired
    SecurityConfig securityConfig;

    @PostMapping("/member/signin")
    public String memberLogin(@ModelAttribute Member member) {
        Member joinMember = memberRepository.findByEmail(member.getEmail());
        if (joinMember != null) {
            String joinPwd = member.getPwd();
            String memberPwd = member.getPwd();
            // boolean isMatch = passwordEncoder.matches(memberPwd, joinPwd);
            // session.setAtrribute("member_info", member);
        }
        return "redirect:/";
    }

    // @PostMapping("/join")
    // {
    // 회원 가입
    // }

    // //회원 로그인은 가입 후 확인가능 잠시보류
    // @PostMapping("/member/login")
    // public String memberLogin(@RequestParam String email, String pwd, Model
    // model) {
    // model.addAttribute(email, pwd);
    // return "";
    // }

}
