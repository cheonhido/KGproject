package com.example.spring_project.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.spring_project.entity.Instaboard;

public interface InstaboardRepository extends JpaRepository<Instaboard, Integer> {
	// 이하 성찬//
	@Query(value = "select * from instaboard order by seq desc", nativeQuery = true)
	List<Instaboard> findByInstaList();

	@Query(value = "select * from instaboard where userId=:userId and seq=:seq", nativeQuery = true)
	Instaboard findByUserId(@Param("userId") String userId, @Param("seq") int seq);

	@Query(value = "select * from instaboard where userId=:userId", nativeQuery = true)
	Instaboard findByUserIdForImage(@Param("userId") String userId);

	// 사용자가 저장한 게시글 목록 조회
	@Query(value = "select seq from instaboard where userId= :userId and is_saved = 1", nativeQuery = true)
	List<Integer> findSavedPostIdsByUser(@Param("userId") String userId);

	// 게시글 저장 여부 확인
	@Query(value = "select is_saved from instaboard where seq = :seq", nativeQuery = true)
	int isPostSaved(@Param("seq") int seq);

	// 게시글 ID로 댓글 수를 조회
	@Query(value = "SELECT COUNT(c) FROM insta_comment c WHERE c.seq = :seq", nativeQuery = true)
	int countById(@Param("seq") int seq);

	// 이하 은택------------------------------------------------------------------------
	// 사용자 ID에 따라 게시글 수를 카운트하는 메서드
	@Query("SELECT COUNT(i) FROM Instaboard i WHERE i.userid = :userid")
	int countByUserId(@Param("userid") String userid);

	// 내 게시물 리스트 만들기
	@Query("SELECT board_file FROM Instaboard WHERE userid = :userid ORDER BY seq DESC")
	List<String> findBoardfileByUserId(@Param("userid") String userid);

	// seq 최소값 구하기
	@Query("SELECT MIN(seq) FROM Instaboard")
	Integer findMinSeq();

	// seq 최대값 구하기
	@Query("SELECT MAX(seq) FROM Instaboard")
	Integer findMaxSeq();

	// randomNumbers에 포함된 seq 값을 가진 board_file 목록을 조회
	@Query("SELECT board_file FROM Instaboard WHERE seq IN :seqList")
	List<String> findBoardFileBySeqIN(@Param("seqList") Set<Integer> seqList);

}
