package com.project.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDto {
	private Long id;
	private String title;
	private String content;
	private int readCnt;
	private String registerId;

}
