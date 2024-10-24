package com.example.spring_project.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.spring_project.dto.UserDBDTO;
import com.example.spring_project.entity.UserDB;
import com.example.spring_project.repository.UserDBRepository;

import jakarta.transaction.Transactional;

@Repository
public class UserDBDAO {
	@Autowired
	UserDBRepository userRepository;

	// 이하 은택------------------------------------------------------------------//
	// 아이디 존재 확인
	public boolean checkId(String userid) {
		return userRepository.existsById(userid);
	}

	// 회원가입
	public boolean UserWrite(UserDBDTO dto) {
		// 1. 기존 데이터 존재 확인
		boolean is_user = userRepository.existsById(dto.getUserid());

		boolean result = false;

		if (!is_user) { // id가 존재하지 않으면
			UserDB userdb = dto.toEntity();
			// System.out.println("userdb = " + userdb);
			UserDB userdb_result = userRepository.save(userdb);
			// System.out.println(userdb_result);
			if (userdb_result != null)
				result = true;
		}

		return result;
	}

	// 로그인
	public UserDB login(UserDBDTO dto) {
		return userRepository.findByUseridAndUserpwd(dto.getUserid(), dto.getUserpwd());
	}

	// 회원 정보
	public UserDB userdbInfo(String userid) {
		return userRepository.findById(userid).orElse(null);
	}

	// dto로 바꿀 entity 가져오기
	public UserDBDTO userdbFindById(String userid) {
		UserDB userDate = userRepository.findById(userid).orElse(null);
		return usertDate(userDate);
	}

	// entity 를 dto로 변경하기
	public UserDBDTO usertDate(UserDB userDate) {
		if (userDate == null) {
			return null;
		}
		UserDBDTO dto = new UserDBDTO();
		dto.setUserid(userDate.getUserid());
		dto.setUsername(userDate.getUsername());
		dto.setUserpwd(userDate.getUserpwd());
		dto.setEmail1(userDate.getEmail1());
		dto.setEmail2(userDate.getEmail2());
		dto.setGender(userDate.getGender());
		dto.setIntroduce(userDate.getIntroduce());
		dto.setFollow(userDate.getFollow());
		dto.setFollower(userDate.getFollower());
		dto.setPFimage(userDate.getPFimage());
		dto.setLogtime(userDate.getLogtime());
		return dto;
	}

	// 프로필 사진 저장
	public boolean profilePictureUpload(UserDBDTO dto) {
		UserDB userdb = dto.toEntity();
		return userRepository.save(userdb) != null;
	}

	// 프로필 편집
	public boolean profileModify(UserDBDTO dto) {
		UserDB userdb = dto.toEntity();
		return userRepository.save(userdb) != null;
	}

	// 사용자의 프로필 사진 경로를 가져옵니다.
	public String getProfilePicturePath(String userid) {
		UserDB user = userRepository.findById(userid).orElse(null);
		return (user != null) ? user.getPFimage() : null;
	}

	@Transactional
	public boolean resetProfilePictureToDefault(String userid) {
		UserDB user = userRepository.findById(userid).orElse(null);
		if (user != null) {
			user.setPFimage("goonyang_gram_basic_profile_bb.png");
			userRepository.save(user); // 변경된 프로필 사진을 저장
			return true;
		}
		return false;
	}

	// 이하 석호------------------------------------------------------------------//
	// 팔로우증가
	public void followUpdate(UserDB userdb) {
		userRepository.save(userdb);
	}

	// 팔로워증가
	public void followerUpdate(UserDB userdb) {
		userRepository.save(userdb);
	}

	// 모든 유저 데이터 가져오기
	public List<UserDB> getAllUser() {
		return userRepository.findAll();
	}
}
