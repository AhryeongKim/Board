package com.project.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.board.entity.File;

public interface FileRepository extends JpaRepository<File, Long> {

}
