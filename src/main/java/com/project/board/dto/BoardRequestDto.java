package com.project.board.dto;

import com.project.board.entity.Board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardRequestDto {
	private Long id;
	private String title;
	private String content;
	private String registerId;
	private Long fileId;
	private String fileName;
	
	public Board toEntity() {
		return Board.builder()
			.title(title)
			.content(content)
			.registerId(registerId)
			.fileId(fileId)
			.fileName(fileName)
			.build();
	}
	
	@Builder
	public BoardRequestDto(Long id, String title, String content, String registerId, Long fileId, String fileName) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.registerId = registerId;
		this.fileId = fileId;
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "BoardRequestDto [id=" + id + ", title=" + title + ", content=" + content + ", registerId=" + registerId
				+ "]";
	}

	

}
