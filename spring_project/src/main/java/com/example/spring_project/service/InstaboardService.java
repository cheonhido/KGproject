package com.example.spring_project.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_project.dao.InstaboardDAO;
import com.example.spring_project.dto.InstaboardDTO;
import com.example.spring_project.entity.InstaComment;
import com.example.spring_project.entity.Instaboard;
import com.example.spring_project.repository.InstaboardRepository;

import jakarta.transaction.Transactional;

@Service
public class InstaboardService {
	@Autowired
	private InstaboardDAO dao;
	@Autowired
	private InstaboardRepository instaboardRepository;

	// 이하 성찬---------------------------------------------------------------//
	public Instaboard insertImage(String userId, List<String> board_file) {

		return dao.insertImage(userId, board_file);

	}

	// 기능은 수정이지만 사용은 입력으로 사용
	public Instaboard insertContent(int seq, String userId, InstaboardDTO dto) {

		return dao.insertContent(seq, userId, dto);
	}

	// 리스트로 전체 불러오기
	public List<Instaboard> instaList(String userid) {

		return dao.instaList(userid);
	}

	// 게시글 삭제
	public int instaboardDelete(int seq, String userid) {
		return dao.instaboardDelete(seq, userid);
	}

	// 게시글 한개 조회

	public Instaboard findBoard(int seq) {
		return dao.findBoard(seq);
	}

	// 댓글 달기
	public InstaComment insertComment(String userId, String content, int instaboardSeq) {

		return dao.insertComment(userId, content, instaboardSeq);
	}

	// 특정 게시글의 달린 댓글들 조회
	public List<InstaComment> getCommentByBoardSeq(int seq) {
		Instaboard instaboard = instaboardRepository.findById(seq).orElse(null);
		return dao.getBoardWithComments(instaboard);
	}

	// 댓글 삭제
	@Transactional
	public int deleteComment(int getId, String userid) {
		return dao.deleteComment(getId, userid);
	}

	// 처음에 2개의 댓글만 반환
	public List<InstaComment> getInitialComments(int postSeq) {
		return dao.getInitialComments(postSeq);
	}

	// 모든 댓글 반환
	public List<InstaComment> getAllComments(int postSeq) {
		return dao.getAllComments(postSeq);
	}

	// 댓글 수 카운팅
	public int getCommentCountByPostId(int postId) {
		return dao.getCommentCountByPostId(postId);
	}

	// 좋아요 기능 구현

	// 좋아요 추가
	@Transactional
	public boolean likePost(String userid, int instaboard) {
		return dao.likePost(userid, instaboard);
	}

	// 좋아요 취소
	@Transactional
	public boolean unlikePost(String userid, int instaboard) {
		return dao.unlikePost(userid, instaboard);
	}

	// 좋아요 수 조회
	public int getLikeCount(int instaboard) {
		return dao.getLikeCount(instaboard);
	}

	// 사용자가 좋아요한 게시글 목록 가져오기
	public List<Integer> getLikedPostIds(String userid) {
		return dao.getLikedPostIds(userid);
	}

	// 게시글 저장하기
	public void toggleSavePost(int postSeq, String userid) {
		dao.toggleSavePost(postSeq, userid);
	}

	// 게시글 저장상태 객체에 저장
	public void savePostStat(Instaboard instaboard) {
		dao.savePostStat(instaboard);
	}

	// 사용자가 저장한 게시글 목록 조회
	public List<Integer> getSavedPostIds(String userid) {
		return dao.getSavedPostIds(userid);
	}

	// 게시글 수정하기
	public Instaboard instaModify(int seq, String content, String uploadpath) {

		return dao.instaModify(seq, content, uploadpath);

	}

	// 이하 은택------------------------------------------------------//
	public int countPostsByUserId(String userid) {
		return dao.countPostsByUserId(userid);
	}

	// 게시물 리스트 구하기
	public List<String> getUserImages(String userid) {
		return dao.getUserImages(userid);
	}

	// seq 최소값, 최대값 구하기
	public Integer instaboardSeqMin() {
		return dao.instaboardSeqMin();
	}

	public Integer instaboardSeqMax() {
		return dao.instaboardSeqMax();
	}

	// randomNumbers에 포함된 seq 값을 가진 board_file 목록을 조회
	public List<String> getRandomBoardFiles(Set<Integer> randomNumbers) {
		return dao.getRandomBoardFiles(randomNumbers);
	}

	// 이하 석호---------------------------------------------------------//
	// 모든 게시글 가져오기
	public List<Instaboard> instaListAll() {
		return dao.instaListAll();
	}
}
