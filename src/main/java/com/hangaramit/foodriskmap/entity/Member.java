package com.hangaramit.foodriskmap.entity;


import jakarta.persistence.*;
import lombok.Data;


@Entity
@SequenceGenerator(name = "MEMBER_SEQ", allocationSize = 1)  //    기본키 할당:Oracle은 SEQUENCE 사용
@Data
public class Member {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ")
    //@GeneratedValue를 사용하면 default값으로 AUTO가 적용되고, AUTO는 IDENTITY를 기본으로 사용
    //기본키 할당:Oracle은 SEQUENCE 사용
    private Long id;
    @Column(nullable = false, length = 255)
    private String email;
    @Column(nullable = false, length = 255)
    private String pwd;

}
