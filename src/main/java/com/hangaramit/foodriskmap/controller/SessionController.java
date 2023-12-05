package com.hangaramit.foodriskmap.controller;


import com.hangaramit.foodriskmap.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


@Controller
public class SessionController {

    //비밀번호 암호화(자바 기본패키지 이용)
    String sha256(String pwd, String salt) {
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(pwd.getBytes());
            md.update(salt.getBytes());
            byte[] data = md.digest();
            for (byte b : data) {
                sb.append(String.format("%064x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Autowired
    MemberRepository memberRepository;

    //회원가입 시 이메일 중복 검사
    @GetMapping("joinAjax")
    @ResponseBody
    public Map<String, Object> joinAjax(@RequestParam String email) {
        boolean isExist = memberRepository.existsByEmail(email);
        Map<String, Object> result = new HashMap<>();
        result.put("result", isExist);
        return result;
    }
}
