package com.example.spring_project.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
public class UserDB {
	@Id    
    @Column(name = "userid")
    private String userid;   //아이디
    private String username;   // 이름
    private String userpwd;   //비밀번호
    private String email1;   //이메일 @ 앞
    private String email2;   //이메일 @ 뒤
    private String gender;   //성별
    private String PFimage;   //프로필이미지
    private String introduce;   //소개
    private Integer follow;   //팔로우
    private Integer follower;   //팔로워
    @Temporal(TemporalType.DATE)
    private Date logtime;   //가입날자
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(
        name = "user_saved_posts",  // 중간 테이블 이름
        joinColumns = @JoinColumn(name = "userid"),
        inverseJoinColumns = @JoinColumn(name = "seq")
    )
    @ToString.Exclude
    private List<Instaboard> savedPosts = new ArrayList<>();
    
    
}
