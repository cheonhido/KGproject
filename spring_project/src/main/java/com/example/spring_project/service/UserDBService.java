package com.example.spring_project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_project.dao.UserDBDAO;
import com.example.spring_project.dto.UserDBDTO;
import com.example.spring_project.entity.UserDB;

import jakarta.transaction.Transactional;

@Service
public class UserDBService {
	@Autowired
	private UserDBDAO dao;

	// 이하 은택---------------------------------------------------------------//
	// 아이디 존재 확인
	public boolean checkId(String userid) {
		return dao.checkId(userid);
	}

	// 회원가입
	public boolean UserWrite(UserDBDTO dto) {
		return dao.UserWrite(dto);
	}

	// 로그인
	public UserDB login(UserDBDTO dto) {
		return dao.login(dto);
	}

	// 회원 정보
	public UserDB userdbInfo(String userid) {
		return dao.userdbInfo(userid);
	}

	// id로 해당 유저 데이터 dto로 불러오기
	public UserDBDTO userdbFindById(String userid) {
		return dao.userdbFindById(userid);
	}

	// 프로필 사진 저장
	public boolean profilePictureUpload(UserDBDTO dto) {
		return dao.profilePictureUpload(dto);
	}

	// 프로필 편집
	public boolean profileModify(UserDBDTO dto) {
		return dao.profileModify(dto);
	}

	// 사용자의 프로필 사진 경로를 가져옵니다.
	public String getProfilePicturePath(String userid) {
		return dao.getProfilePicturePath(userid);
	}

	// 사용자의 프로필 사진을 기본 이미지로 변경합니다.
	@Transactional
	public boolean resetProfilePictureToDefault(String userid) {
		return dao.resetProfilePictureToDefault(userid);
	}

	// 이하 석호---------------------------------------------------------------//
	public void followUpdate(UserDB userdb) {
		dao.followUpdate(userdb);
	}

	public void followerUpdate(UserDB userdb) {
		dao.followerUpdate(userdb);
	}

	// 모든 유저 데이터 가져오기
	public List<UserDB> getAllUser() {
		return dao.getAllUser();
	}
}
