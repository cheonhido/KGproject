package com.example.spring_project.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_project.dao.HashtagDAO;
import com.example.spring_project.entity.Hashtag;
import com.example.spring_project.entity.Instaboard;

@Service
public class HashtagService {
	@Autowired
	HashtagDAO dao;
	
	// 해시태그 저장
	public void addHashtagsToPost(Instaboard instaboard, String board_content) {
		dao.addHashtagsToPost(instaboard, board_content);
	}

	// 특정 게시글의 해시태그 불러오기
	public List<Hashtag> getHashtagsForPost(Instaboard instaboard) {
		return dao.getHashtagsForPost(instaboard);
	}

	public String convertHashtagsToLinks(String board_content) {
		return dao.convertHashtagsToLinks(board_content);
	}

	//
	public Map<String, List<Instaboard>> findPostsByHashtagName(String hashtagName) {
		return dao.findPostsGroupedByHashtag(hashtagName);
	}

}
