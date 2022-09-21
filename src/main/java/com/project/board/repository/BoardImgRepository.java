package com.project.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.board.entity.BoardImg;

public interface BoardImgRepository extends JpaRepository<BoardImg, Long> {
	List<BoardImg> findByBoardIdOrderByIdAsc(Long boardId);


}
