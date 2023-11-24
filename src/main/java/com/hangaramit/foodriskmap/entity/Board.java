package com.hangaramit.foodriskmap.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Board {
    @Id
    @GeneratedValue
    long id;
    // (strategy = GenerationType.AUTO)

    String title;
    String content;
    // private String userId;

    // @ManyToOne
    // String user; // user_id 로 생성

    // @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval =
    // true)
    // List<Comment> comments = new ArrayList<>();

}