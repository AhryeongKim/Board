package com.project.board.dto;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.project.board.entity.Board;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
public class BoardFormDto {
	
	private Long id;
	@NotBlank(message="제목은 필수 입력 값입니다.")
	private String title;
	
	@NotBlank(message="내용은 입력은 필수입니다.")
	private String content;
	
	private List<BoardImgDto> boardImgDtoList = new ArrayList<>(); // 상품 저장 후 수정할 때 상품 이미지 정보를 저장하는 리스트
	private List<Long> boardImgIds = new ArrayList<>();
	private static ModelMapper modelMapper = new ModelMapper();
	public Board createBoard(){
	return modelMapper.map(this, Board.class);
	}
	public static BoardFormDto of(Board board){
	return modelMapper.map(board, BoardFormDto.class);
	}


}
