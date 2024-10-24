package com.example.spring_project.dto;

import java.util.Date;

import com.example.spring_project.entity.Instaboard;

import lombok.Data;
import lombok.ToString;

import com.example.spring_project.entity.InstaLike;

@Data
@ToString
public class instaLikeDTO {
	
	private int id;
	private String userid;
	private Instaboard instaboard;
	private Date likeddate;
	
	public InstaLike toEntity() {
		return new InstaLike(id, userid, instaboard, likeddate);
	}
	
}
