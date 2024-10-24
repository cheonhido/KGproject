package com.example.spring_project.dto;

import com.example.spring_project.entity.Follow;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class FollowDTO {
	private int seq;
	private String from_user;    //요청하는자
    private String to_user;		 //요청받는자
    private int followcheck;     //수락여부
	
	public Follow toEntity() {
		return new Follow(seq, from_user, to_user, followcheck);
	}
}
