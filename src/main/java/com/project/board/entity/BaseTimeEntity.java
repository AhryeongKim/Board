package com.project.board.entity;

import java.time.LocalDate;


import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
public class BaseTimeEntity {
	
	@CreatedDate
	private LocalDate registerTime;
	
	@LastModifiedDate
	private LocalDate updateTime;
	
}
