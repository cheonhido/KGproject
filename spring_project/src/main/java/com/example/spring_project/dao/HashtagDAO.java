package com.example.spring_project.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.spring_project.entity.Hashtag;
import com.example.spring_project.entity.Instaboard;
import com.example.spring_project.entity.InstaboardHashtag;
import com.example.spring_project.repository.HashtagRepository;
import com.example.spring_project.repository.InstaboardHashtagRepository;

@Repository
public class HashtagDAO {
	@Autowired
	private HashtagRepository hashtagRepository;
	@Autowired
	private InstaboardHashtagRepository instaboardHashtagRepository;

	// 게시글에 해시태그 추가
	public void addHashtagsToPost(Instaboard instaboard, String contentWithHashtags) {
		// 게시글에서 해시태그 추출
		List<String> hashtags = extractHashtags(contentWithHashtags);
		// System.out.println(hashtags);
		for (String tag : hashtags) {
			// 해시태그가 존재하지 않으면 새로 생성
			Hashtag hashtag = hashtagRepository.findByName(tag)
					.orElseGet(() -> hashtagRepository.save(new Hashtag(null, tag)));

			// Instaboard와 Hashtag를 연결하는 중간 테이블 엔티티 생성
			InstaboardHashtag instaboardHashtag = new InstaboardHashtag(instaboard, hashtag);
			instaboardHashtagRepository.save(instaboardHashtag);
		}
	}

	// 게시글에서 해시태그 추출 (정규식 사용)
	private List<String> extractHashtags(String content) {
		Pattern pattern = Pattern.compile("#([ㄱ-ㅎ가-힣a-zA-Z0-9_]+)");
		Matcher matcher = pattern.matcher(content);
		List<String> hashtags = new ArrayList<>();
		while (matcher.find()) {
			hashtags.add(matcher.group(1)); // #을 제외한 해시태그 추가
		}
		return hashtags;
	}

	// 특정 게시글의 해시태그 조회
	public List<Hashtag> getHashtagsForPost(Instaboard instaboard) {
		List<InstaboardHashtag> instaboardHashtags = instaboardHashtagRepository.findByInstaboard(instaboard);
		return instaboardHashtags.stream().map(InstaboardHashtag::getHashtag).collect(Collectors.toList());
	}

	// 해시태그를 링크로 변환
	public String convertHashtagsToLinks(String content) {
		// 해시태그 사이의 공백 제거
	    content = content.replaceAll("\\s+", "");
	    
	    String hashtagPattern = "(#[a-zA-Z0-9ㄱ-ㅎ가-힣]+)";
	    Matcher matcher = Pattern.compile(hashtagPattern).matcher(content);

	    StringBuffer result = new StringBuffer();
	    boolean firstHashtag = true;

	    // 해시태그를 <a> 태그로 변환하며 첫 번째 해시태그 앞에 줄 바꿈을 추가
	    while (matcher.find()) {
	        if (firstHashtag) {
	            matcher.appendReplacement(result, "<br><a href='#' class='hashtag_link'>" + matcher.group(1) + "</a>");
	            firstHashtag = false;
	        } else {
	            matcher.appendReplacement(result, "<a href='#' class='hashtag_link'>" + matcher.group(1) + "</a>");
	        }
	    }
	    matcher.appendTail(result);

	    return result.toString();
	}

	public Map<String, List<Instaboard>> findPostsGroupedByHashtag(String hashtagName) {
        List<Object[]> results = instaboardHashtagRepository.findPostsByHashtagName(hashtagName);

        // 해시태그 이름을 기준으로 게시글을 그룹화
        Map<String, List<Instaboard>> postsGroupedByHashtag = new HashMap<>();

        for (Object[] result : results) {
            String tagName = (String) result[0];
            Instaboard instaboard = (Instaboard) result[1];

            postsGroupedByHashtag
                .computeIfAbsent(tagName, k -> new ArrayList<>())
                .add(instaboard);
        }

        return postsGroupedByHashtag;
    }
}
