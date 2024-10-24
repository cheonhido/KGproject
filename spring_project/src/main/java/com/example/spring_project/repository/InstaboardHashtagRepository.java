package com.example.spring_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.spring_project.entity.Instaboard;
import com.example.spring_project.entity.InstaboardHashtag;

@Repository
public interface InstaboardHashtagRepository extends JpaRepository<InstaboardHashtag, Long> {
    List<InstaboardHashtag> findByInstaboard(Instaboard instaboard);
    // 이하 석호------------------------------------------------------------------------------
    // 특정 해시태그 Name이 포함된 모든 게시글을 조회하는 쿼리
    @Query("SELECT ih.hashtag.name, ih.instaboard FROM InstaboardHashtag ih WHERE ih.hashtag.name LIKE %:hashtagName%")
    List<Object[]> findPostsByHashtagName(@Param("hashtagName") String hashtagName);
    
}
