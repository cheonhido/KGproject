package com.example.spring_project.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.spring_project.dto.FollowDTO;
import com.example.spring_project.entity.Follow;
import com.example.spring_project.entity.Instaboard;
import com.example.spring_project.entity.UserDB;
import com.example.spring_project.repository.FollowRepository;
import com.example.spring_project.repository.MessageRepository;

@Repository
public class FollowDAO {
	@Autowired
	FollowRepository followRepository;
	@Autowired
	MessageRepository messageRepository;

	//이하 석호----------------------------------------------------------
	public boolean follow(FollowDTO dto) {
		//System.out.println("dto = " + dto);
		boolean result = false;
		int followCheck = followRepository.findFollow(dto.getFrom_user(), dto.getFrom_user());
		if (followCheck == 0) {
			Follow follow = dto.toEntity();
			//System.out.println(follow);
			if (follow != null) {
				followRepository.save(follow);
				result = true;
			}
		}
		return result;
	}

	public List<Follow> followAccept(String to_user, int followcheck) {
		return followRepository.checkFollow(to_user, followcheck);
	}

	// 맞팔
	public Follow followM(String from_user, String to_user) {
		return followRepository.followM(from_user, to_user);
	}

	public List<UserDB> recomendList(String from_user) {
		List<Instaboard> list = new ArrayList<>();
	    List<Object[]> results = followRepository.findUserData(from_user);
	    return results.stream()
	        .map(result -> new UserDB(
	            (String) result[0], 
	            (String) result[1], 
	            (String) result[2],
	            (String) result[3], 
	            (String) result[4], 
	            (String) result[5], 
	            (String) result[6],
	            (String) result[7], 
	            Optional.ofNullable((BigDecimal) result[8]).map(BigDecimal::intValue).orElse(0),  // follow가 null이면 0으로 설정
	            Optional.ofNullable((BigDecimal) result[9]).map(BigDecimal::intValue).orElse(0),  // follower가 null이면 0으로 설정
	            (Date) result[10],
	            list
	        ))
	        .collect(Collectors.toList());
	}

	// follow list 구하기
	public List<String> findFollowList(String from_user) {
		return followRepository.findFollowList(from_user);
	}

	// 팔로워 리스트 구하기
	public List<String> findFollowerList(String from_user) {
		return followRepository.findFollowerList(from_user);
	}

	public void unFollowUser(String from_user, String to_user) {
		followRepository.unFollowUser(from_user, to_user);
	}

}
