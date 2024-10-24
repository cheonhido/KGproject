// Instaboard와 Hashtag 간의 연결을 다루는 중간 테이블
package com.example.spring_project.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstaboardHashtag {    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "instaboard_id")
    private Instaboard instaboard;  // Instaboard와의 관계 (게시글)

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;  // Hashtag와의 관계 (해시태그)
    
    public InstaboardHashtag(Instaboard instaboard, Hashtag hashtag) {
        this.instaboard = instaboard;
        this.hashtag = hashtag;
    }
}
