package com.project.board.dto;

import com.project.board.entity.BoardImg;
import org.modelmapper.ModelMapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardImgDto {
	private Long id;
	private String imgName;
	private String oriImgName;
	private String imgUrl;
	private String repImgYn;
	private static ModelMapper modelMapper = new ModelMapper(); 
	
	public static BoardImgDto of(BoardImg boardImg) {
		return modelMapper.map(boardImg, BoardImgDto.class);
		}

}
