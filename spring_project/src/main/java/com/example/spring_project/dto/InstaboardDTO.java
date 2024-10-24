package com.example.spring_project.dto;

import java.util.Date;

import com.example.spring_project.dto.InstaboardDTO;
import com.example.spring_project.entity.Instaboard;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class InstaboardDTO {
	private int seq;
	private String userid;		// 작성자 ID
    private String board_content;	// 글 내용
    private String board_file;		// 파일
    private int commentCount; // 댓글 수
    private int board_re_ref;		// 관련글 번호
    private int board_re_lev;		// 답글 레벨
    private int board_re_seq;		// 관련글 중 출력 순서
    private int board_readcount;	// 조회수
    private Date board_date;		// 작성일
    private int is_liked;			// 좋아요 표시 플래그 변수
    private int like_count; 	// 게시글 좋아요 갯수
    
    public Instaboard toEntity() {
    	return new Instaboard(seq, userid, board_content, board_file, board_re_ref, board_re_lev, board_re_seq, board_readcount, board_date);
    }
    
}