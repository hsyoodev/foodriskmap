package com.hangaramit.foodriskmap.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

// @SequenceGenerator(name = "MEMBER_SEQ", allocationSize = 1)
@Entity
@Data
public class Member {

    @Id
    // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ")
    @GeneratedValue
    private Long id;

    @Column(length = 255)
    private String email;
    @Column(nullable = false, length = 255)
    private String pwd;
}
