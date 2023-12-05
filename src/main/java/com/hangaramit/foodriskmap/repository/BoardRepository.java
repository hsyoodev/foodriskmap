package com.hangaramit.foodriskmap.repository;

import com.hangaramit.foodriskmap.entity.Board;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Date;

@Repository
public interface BoardRepository extends
        JpaRepository<Board, Long> {

    List<Board> findByTitleOrContentContainingIgnoreCase(String search1, String search2);

    List<Board> findByVstDate(Date vstDate);
}
