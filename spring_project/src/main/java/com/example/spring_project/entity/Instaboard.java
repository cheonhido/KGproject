package com.example.spring_project.entity;

import java.util.Date;
import java.util.List;

import com.example.spring_project.entity.Instaboard;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@NoArgsConstructor
public class Instaboard {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
	generator = "INSTABOARD_SEQUENCE_GENERATOR")
	@SequenceGenerator(name = "INSTABOARD_SEQUENCE_GENERATOR",
	   sequenceName = "seq", 
	   initialValue = 1, allocationSize = 1)
	private int seq;
	private String userid; // 작성자 ID
	private String board_content; // 글 내용
	private String board_file; // 파일
	private int board_re_ref; // 관련글 번호
	private int board_re_lev; // 답글 레벨
	private int board_re_seq; // 관련글 중 출력 순서
	private int board_readcount; // 조회수
	private int commentCount; // 댓글 수
	@Temporal(TemporalType.DATE)
	private Date board_date; // 작성일
	private int like_count;	// 게시글 좋아요 갯수
	private int is_liked;	// 좋아요 상태 저장 (0: 좋아요 안함, 1: 좋아요 함)
    private int is_saved;	// 저장 상태를 저장할 필드 추가 (0: 저장 안함, 1: 저장 함)
	
	// 댓글과의 관계 설정
    @JsonManagedReference
    @OneToMany(mappedBy = "instaboard", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<InstaComment> comments; // 게시글에 달린 댓글들
    
    // 좋아요와의 관계 설정
    @OneToMany(mappedBy = "instaboard", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<InstaLike> likes; // 게시글에 달린 좋아요들
    
    public Instaboard(int seq, String userid, String board_content, String board_file, int board_re_ref,
            int board_re_lev, int board_re_seq, int board_readcount, Date board_date) {
	this.seq = seq;
	this.userid = userid;
	this.board_content = board_content;
	this.board_file = board_file;
	this.board_re_ref = board_re_ref;
	this.board_re_lev = board_re_lev;
	this.board_re_seq = board_re_seq;
	this.board_readcount = board_readcount;
	this.board_date = board_date;
    }
}
