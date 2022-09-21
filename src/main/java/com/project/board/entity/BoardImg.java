package com.project.board.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="economy_img")
@Getter
@Setter
public class BoardImg extends BaseEntity{
	
	@Id
	@Column(name="economy_img_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String imgName; //이미지 파일명
	private String oriImgName; //원본 이미지 파일명
	private String imgUrl; //이미지 조회 경로
	private String repimgYn; //대표 이미지 여부
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "economy_id")
	private Board board;
	
	public void updateBoardImg(String oriImgName, String imgName, String imgUrl){
		this.oriImgName = oriImgName; 
		this.imgName = imgName;
		this.imgUrl = imgUrl;
		}




}
