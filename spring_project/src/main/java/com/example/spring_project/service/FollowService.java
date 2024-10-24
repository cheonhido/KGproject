package com.example.spring_project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_project.dao.FollowDAO;
import com.example.spring_project.dto.FollowDTO;
import com.example.spring_project.entity.Follow;
import com.example.spring_project.entity.UserDB;

import jakarta.transaction.Transactional;

@Service
public class FollowService {
	@Autowired
	private FollowDAO dao;

	public boolean follow(FollowDTO dto) {		
		return dao.follow(dto);
	}

	public List<Follow> followAccept(String to_user, int followcheck) {
		return dao.followAccept(to_user, followcheck);
	}

	// 맞팔
	public Follow followM(String from_user, String to_user) {
		return dao.followM(from_user, to_user);		
	}

	public List<UserDB> recomendList(String from_user) {
		return dao.recomendList(from_user);
	}

	// 팔로우 리스트 구하기
	public List<String> findFollowList(String from_user) {
		return dao.findFollowList(from_user);
	}
	
	// 팔로워 리스트 구하기
	public List<String> findFollowerList(String from_user) {
		return dao.findFollowerList(from_user);
	}

	// 언팔
	public void unFollowUser(String from_user, String to_user) {
		dao.unFollowUser(from_user, to_user);
		//System.out.println("서비스");
	}
}
