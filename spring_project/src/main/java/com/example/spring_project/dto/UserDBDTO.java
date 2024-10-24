package com.example.spring_project.dto;

import java.util.Date;

import com.example.spring_project.entity.UserDB;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserDBDTO {
	private String userid; // 아이디
	private String username; // 이름
	private String userpwd; // 비밀번호
	private String email1; // 이메일 @ 앞
	private String email2; // 이메일 @ 뒤
	private String gender; // 성별
	private String PFimage; // 프로필이미지
	private String introduce; // 소개
	private Integer follow; // 팔로우
	private Integer follower; // 팔로워
	private Date logtime; // 가입날자

	public UserDB toEntity() {
		return new UserDB(userid, username, userpwd, email1, email2, gender, PFimage, introduce, follow, follower,
				logtime, null);
	}
}
