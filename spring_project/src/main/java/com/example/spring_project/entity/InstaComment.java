package com.example.spring_project.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InstaComment {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_SEQUENCE_GENERATOR")
    @SequenceGenerator(name = "COMMENT_SEQUENCE_GENERATOR", sequenceName = "comment_seq", initialValue = 1, allocationSize = 1)
    private int id; // 댓글의 고유 번호

    private String content; // 댓글 내용
    private String userid;  // 댓글 작성자 ID
    private int commentCount; // 댓글 수

    @Temporal(TemporalType.DATE)
    private Date createddate; // 작성일

    @ManyToOne
    @JoinColumn(name = "instaboard_id", nullable = false)
    @JsonBackReference
    private Instaboard instaboard; // 댓글이 속한 게시글
}
