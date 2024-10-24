package com.example.spring_project.dto;

import java.util.Date;

import com.example.spring_project.entity.InstaComment;
import com.example.spring_project.entity.Instaboard;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class InstaCommentDTO {
	private int id;
    private String content; // 댓글 내용
    private String userid;  // 댓글 작성자 ID
    private int commentCount; // 댓글 수
    private Date createddate; // 작성일
    @JsonBackReference
    private Instaboard instaboard; // 댓글이 속한 게시글
    
    
    public InstaComment toEntity() {
    	return new InstaComment(id, content, userid, commentCount, createddate, instaboard);
    }

}
