package com.example.spring_project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.spring_project.entity.InstaLike;
import com.example.spring_project.entity.Instaboard;

import jakarta.transaction.Transactional;

public interface instaLikeRepository extends JpaRepository<InstaLike, Integer> {
	// 특정 게시글에 대해 사용자가 이미 좋아요를 눌렀는지 확인하는 메서드
	Optional<InstaLike> findByUseridAndInstaboard(String userid, Instaboard instaboard);

	// 특정 게시글에 대해 좋아요 수를 가져오는 메서드
	int countByInstaboard(Instaboard instaboard);

	// 특정 사용자가 좋아요를 취소하는 메서드
	void deleteByUseridAndInstaboard(String userid, Instaboard instaboard);

	// 특정 사용자가 해당 게시글에 좋아요를 눌렀는지 확인하는 메서드
	boolean existsByUseridAndInstaboardSeq(String userid, int instaboardSeq);

	// 사용자가 좋아요한 게시글의 ID 목록을 가져오는 메서드
	@Query("SELECT il.instaboard.seq FROM InstaLike il WHERE il.userid = :userid")
	List<Integer> findLikedPostIdsByUserid(@Param("userid") String userid);

	// Instaboard의 seq(게시글 번호)를 기준으로 좋아요 삭제
	@Modifying
	@Transactional
	void deleteByInstaboardSeq(int instaboardSeq);

}
