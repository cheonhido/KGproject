package com.example.spring_project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.spring_project.entity.InstaComment;
import com.example.spring_project.entity.Instaboard;

import jakarta.transaction.Transactional;

public interface InstaCommentRepository extends JpaRepository<InstaComment, Integer> {
	// 이하 성찬
	List<InstaComment> findByInstaboardOrderByIdDesc(Instaboard instaboard);

	@Query(value = "select * from insta_comment where userId=:userId and id= :id", nativeQuery = true)
	InstaComment findByUserId(@Param("userId") String userId, @Param("id") int id);

	Optional<InstaComment> findById(Integer id);

	// 처음 2개의 댓글만 가져오는 쿼리
	@Query(value = "SELECT * FROM insta_comment WHERE instaboard_id = :instaboard_id ORDER BY createddate DESC FETCH FIRST 2 ROWS ONLY", nativeQuery = true)
	List<InstaComment> findTop2ByPostSeqOrderByCommentDateDesc(@Param("instaboard_id") int instaboard_id);

	// 모든 댓글을 가져오는 쿼리
	@Query(value = "select * from insta_comment where instaboard_id = :instaboard_id order by createddate desc", nativeQuery = true)
	List<InstaComment> findAllByPostSeqOrderByCommentDateDesc(@Param("instaboard_id") int instaboard_id);

	@Modifying
	@Query("DELETE FROM InstaComment c WHERE c.id = :getId AND c.userid = :userid")
	int deleteByCommentIdAndUserid(@Param("getId") int getId, @Param("userid") String userid);

	@Modifying
	@Transactional
	// Instaboard의 seq(게시글 번호)를 기준으로 댓글 삭제
	void deleteByInstaboardSeq(int instaboardSeq);

	@Query(value = "select * from insta_comment where instaboard_id=:postSeq order by id desc"
			,nativeQuery = true)
	List<InstaComment> findAllByPostSeqOrderByCommentIdDesc(@Param("postSeq") int postSeq);
}
