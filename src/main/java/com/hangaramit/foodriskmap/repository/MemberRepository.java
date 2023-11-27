package com.hangaramit.foodriskmap.repository;

import com.hangaramit.foodriskmap.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    public Member findByEmailAndPwd(String email, String pwd);

    //사용자 정의 메소드 //이메일이 일치
    public Member findByEmail(String email);


}
