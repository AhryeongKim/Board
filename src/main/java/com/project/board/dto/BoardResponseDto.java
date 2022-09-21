package com.project.board.dto;

import java.time.LocalDate;

import com.project.board.entity.Board;

import lombok.Getter;

@Getter
public class BoardResponseDto {
	private Long id;
	private String title;
	private String content;
	private int readCnt;
	private String registerId;
	private LocalDate registerTime;
	private Long fileId;
	private String fileName;
	
	public BoardResponseDto(Board entity) {
		this.id = entity.getId();
		this.title = entity.getTitle();
		this.content = entity.getContent();
		this.readCnt = entity.getReadCnt();
		this.registerId = entity.getRegisterId();
		this.registerTime = entity.getRegisterTime();
		this.fileId = entity.getFileId();
		this.fileName = entity.getFileName();
	}
	
	@Override
	public String toString() {
		return "BoardResponseDto [id=" + id + ", title=" + title + ", content=" + content + ", readCnt=" + readCnt
						+ ", registerId=" + registerId + ", registerTime=" + registerTime + ",fileId=" + fileId + "]";
	}
}
