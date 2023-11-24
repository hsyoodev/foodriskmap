package com.hangaramit.foodriskmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hangaramit.foodriskmap.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
