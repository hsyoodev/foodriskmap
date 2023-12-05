package com.hangaramit.foodriskmap.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.util.Date;
import lombok.Data;

@Entity
@Data
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String title;

    String content;

    Date vstDate;

    @ManyToOne
    Member member;

//    @OneToMany(mappedBy = "board")
//    List<Comments> comments = new ArrayList<>();
}