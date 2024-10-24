package com.example.spring_project.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.spring_project.entity.Instaboard;
import com.example.spring_project.service.HashtagService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HashtagController {
	@Autowired
	HashtagService hashtagService;
	
	@Value("${project.upload.path}")
	private String uploadpath;
	
	@GetMapping("/searchHashtagInfo")
	@ResponseBody
	public ResponseEntity<Map<String, List<Instaboard>>> searchHashtagInfo(@RequestParam("hashtagName") String hashtagName, Model model) {
		// 해시태그 이름과 관련된 게시글 목록 조회(태그명 + 게시글)
		Map<String, List<Instaboard>> postsGroupedByHashtag  = hashtagService.findPostsByHashtagName(hashtagName);
		
		// 게시글 정보를 JSON으로 그대로 반환
	    return ResponseEntity.ok(postsGroupedByHashtag);
	}	
	
	@PostMapping("/hashtagSearchResultForm")
	public String hashtagSearchResultForm(@RequestBody Map<String, Object> requestData, Model model) {
		String hashtag = (String) requestData.get("hashtag");
		List<Map<String, Object>> posts = (List<Map<String, Object>>) requestData.get("posts");

		// 모델에 해시태그와 관련된 데이터 추가
		model.addAttribute("hashtag", hashtag);
		model.addAttribute("posts", posts);
		//System.out.println(hashtag);
		//System.out.println(posts);

		// 클라이언트로 페이지를 렌더링하여 반환
		return "instaboard/hashtagSearchResultForm";
	}
}
