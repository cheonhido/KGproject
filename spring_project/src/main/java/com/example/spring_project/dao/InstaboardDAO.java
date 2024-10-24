package com.example.spring_project.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.spring_project.dto.InstaboardDTO;
import com.example.spring_project.entity.InstaComment;
import com.example.spring_project.entity.InstaLike;
import com.example.spring_project.entity.Instaboard;
import com.example.spring_project.entity.UserDB;
import com.example.spring_project.repository.InstaCommentRepository;
import com.example.spring_project.repository.InstaboardRepository;
import com.example.spring_project.repository.UserDBRepository;
import com.example.spring_project.repository.instaLikeRepository;

import jakarta.transaction.Transactional;

@Repository
public class InstaboardDAO {
	@Autowired
	InstaboardRepository instaboardRepository;
	@Autowired
	instaLikeRepository instaLikeRepository;
	@Autowired
	InstaCommentRepository instaCommentRepository;
	@Autowired
	UserDBRepository userDBRepository;

	// 이하 성찬------------------------------------------------------//
	// 기능구현
	public Instaboard insertImage(String userId, List<String> board_file) {

		Instaboard instaboard = new Instaboard();

		String joinedFileNames = String.join(",", board_file);

		instaboard.setUserid(userId);
		instaboard.setBoard_file(joinedFileNames);
		instaboard.setBoard_content("test");
		instaboard.setBoard_re_lev(0);
		instaboard.setBoard_readcount(0);
		instaboard.setBoard_date(new Date());

		// 좋아요 여부 체크

		boolean likedFlag = instaLikeRepository.existsByUseridAndInstaboardSeq(userId, instaboard.getSeq());
		instaboard.setIs_liked(likedFlag ? 1 : 0);

		// board_re_ref와 board_re_seq 설정을 위해 저장
		Instaboard saveBoard = instaboardRepository.save(instaboard);

		// 자신의 seq 값을 board_re_ref와 board_re_seq로 설정
		saveBoard.setBoard_re_ref(saveBoard.getSeq());
		saveBoard.setBoard_re_seq(0);

		return instaboardRepository.save(saveBoard);

	}

	// 기능은 수정이지만 사용은 입력으로 사용
	public Instaboard insertContent(int seq, String userId, InstaboardDTO dto) {

		Instaboard instaboard = instaboardRepository.findByUserId(userId, seq);

		instaboard.setBoard_content(dto.getBoard_content());

		return instaboardRepository.save(instaboard);
	}

	// 리스트로 전체 불러오기
	public List<Instaboard> instaList(String userid) {
		List<Instaboard> list = instaboardRepository.findByInstaList();

		for (Instaboard list_is_liked : list) {
			boolean is_liked = instaLikeRepository.existsByUseridAndInstaboardSeq(userid, list_is_liked.getSeq());
			list_is_liked.setIs_liked(is_liked ? 1 : 0); // 좋아요 여부 설정
		}

		return list;
	}

	// 게시글 삭제
	@Transactional
	public int instaboardDelete(int seq, String userid) {
		Instaboard instaboard = instaboardRepository.findById(seq).orElse(null);
		UserDB user = userDBRepository.findById(userid).orElse(null);
		int result = 0;
		if (instaboard != null) {
			// 게시글 삭제
			user.getSavedPosts().remove(instaboard);
			instaboardRepository.delete(instaboard);
			return result = 1;
		} else {
			return result;
		}
	}

	// 게시글 한개 조회

	public Instaboard findBoard(int seq) {
		return instaboardRepository.findById(seq).orElse(null);
	}

	// 댓글 달기 (댓글 entity사용)
	public InstaComment insertComment(String userId, String content, int instaboardSeq) {

		Instaboard instaboard = instaboardRepository.findById(instaboardSeq).orElse(null);

		if (instaboard == null) {
			return null; // 게시글 없을시 리턴값
		}

		InstaComment instaComment = new InstaComment();
		instaComment.setUserid(userId);
		instaComment.setContent(content);
		instaComment.setInstaboard(instaboard);
		instaComment.setCreateddate(new Date());

		return instaCommentRepository.save(instaComment);
	}

	// 특정 게시글과 관련된 모든 댓글 가져오기
	public List<InstaComment> getBoardWithComments(Instaboard instaboard) {
		return instaCommentRepository.findByInstaboardOrderByIdDesc(instaboard);
	}

	// 댓글 삭제
	public int deleteComment(int getId, String userid) {
		InstaComment instaComment = instaCommentRepository.findById(getId).orElse(null);
		int result = 0;
		if (instaComment != null) {
			instaCommentRepository.deleteByCommentIdAndUserid(getId, userid);
			return 1;
		} else {
			return result;
		}

	}

	// 처음에 2개의 댓글만 반환
	public List<InstaComment> getInitialComments(int postSeq) {
		return instaCommentRepository.findTop2ByPostSeqOrderByCommentDateDesc(postSeq);
	}

	// 모든 댓글 반환
	public List<InstaComment> getAllComments(int postSeq) {
		return instaCommentRepository.findAllByPostSeqOrderByCommentIdDesc(postSeq);
	}

	// 댓글 수 카운팅
	public int getCommentCountByPostId(int postId) {
		return instaboardRepository.countById(postId);
	}

	// 좋아요 기능 구현

	// 좋아요 추가
	public boolean likePost(String userid, int instaboardid) {
		Instaboard post = instaboardRepository.findById(instaboardid).orElse(null);

		if (post == null) {
			return false; // 게시글이 존재하지 않음
		}
		Optional<InstaLike> existingLike = instaLikeRepository.findByUseridAndInstaboard(userid, post);
		if (existingLike.isPresent()) {
			return false; // 이미 좋아요가 눌려 있는 경우
		}

		InstaLike like = new InstaLike();
		like.setUserid(userid);
		like.setInstaboard(post);
		like.setLikeddate(new Date());
		instaLikeRepository.save(like);

		// 좋아요 수 증가 호출
		updateLikeCount(post.getSeq());

		return true;

	}

	// 좋아요 취소
	public boolean unlikePost(String userid, int instaboardid) {
		Instaboard post = instaboardRepository.findById(instaboardid).orElse(null);

		if (post == null) {
			return false; // 게시글이 존재하지 않음
		}

		Optional<InstaLike> existingLike = instaLikeRepository.findByUseridAndInstaboard(userid, post);

		if (!existingLike.isPresent()) {
			return false; // 좋아요가 눌려있지 않는 경우
		}

		instaLikeRepository.deleteByUseridAndInstaboard(userid, post);

		// 좋아요 수 감소 호출

		decreaseLikeCount(post.getSeq());

		return true;
	}

	// 좋아요 수 조회
	public int getLikeCount(int instaboardid) {
		Instaboard post = instaboardRepository.findById(instaboardid).orElse(null);
		if (post == null) {
			return 0; // 게시글이 존재하지 않는 경우
		}
		return instaLikeRepository.countByInstaboard(post);
	}

	// 게시글 좋아요 수 증가
	public void updateLikeCount(int postSeq) {
		Instaboard instaboard = instaboardRepository.findById(postSeq).orElse(null);
		if (instaboard != null) {
			int currentLikeCount = instaboard.getLike_count();
			instaboard.setLike_count(currentLikeCount + 1);
			instaboardRepository.save(instaboard);
		}
	}

	// 게시글 좋아요 수 감소
	public void decreaseLikeCount(int postSeq) {
		Instaboard instaboard = instaboardRepository.findById(postSeq).orElse(null);
		if (instaboard != null && instaboard.getLike_count() > 0) { // 음수 방지
			int currentLikeCount = instaboard.getLike_count();
			instaboard.setLike_count(currentLikeCount - 1);
			instaboardRepository.save(instaboard);
		}
	}

	// 사용자가 좋아요한 게시글 목록 가져오기
	public List<Integer> getLikedPostIds(String userid) {
		return instaLikeRepository.findLikedPostIdsByUserid(userid);
	}

	// 게시글 저장하기
	public void toggleSavePost(int postSeq, String userid) {
		UserDB user = userDBRepository.findById(userid).orElse(null);
		Instaboard post = instaboardRepository.findById(postSeq).orElse(null);

		if (user != null && post != null) {
			if (user.getSavedPosts().contains(post)) {
				// 이미 저장된 게시글이면, 저장 취소
				user.getSavedPosts().remove(post);
			} else {
				// 저장되지 않은 게시글이면, 저장
				user.getSavedPosts().add(post);
			}

			// 변경 사항 저장
			userDBRepository.save(user);
			instaboardRepository.save(post);
			instaboardRepository.flush();
		}
	}

	// 사용자가 저장한 게시글 목록 조회
	public List<Integer> getSavedPostIds(String userid) {
		UserDB user = userDBRepository.findById(userid).orElse(null);
		if (user != null) {
			// 저장된 게시글 목록에서 ID(Seq)만 추출해서 반환
			return user.getSavedPosts().stream().map(Instaboard::getSeq) // 게시글 ID(seq)를 추출
					.collect(Collectors.toList());
		}
		return new ArrayList<>(); // 사용자 또는 저장된 게시글이 없으면 빈 리스트 반환
	}

	// 게시글 저장상태 객체에 저장
	public void savePostStat(Instaboard instaboard) {
		instaboardRepository.save(instaboard);
	}

	// 게시글 수정하기
	public Instaboard instaModify(int seq, String content, String uploadpath) {
		Instaboard instaboard = instaboardRepository.findById(seq).orElse(null);

		// 내용 업데이트
		instaboard.setBoard_content(content);

		return instaboardRepository.save(instaboard);

	}

	// 이하 은택------------------------------------------------------//
	// 게시물 수 구하기
	public int countPostsByUserId(String userid) {
		return instaboardRepository.countByUserId(userid);
	}

	// 게시물 리스트 구하기
	public List<String> getUserImages(String userid) {
		return instaboardRepository.findBoardfileByUserId(userid);
	}

	// seq 최소값, 최대값 구하기
	public Integer instaboardSeqMin() {
		return instaboardRepository.findMinSeq();
	}

	public Integer instaboardSeqMax() {
		return instaboardRepository.findMaxSeq();
	}

	// randomNumbers에 포함된 seq 값을 가진 board_file 목록을 조회
	public List<String> getRandomBoardFiles(Set<Integer> randomNumbers) {
		return instaboardRepository.findBoardFileBySeqIN(randomNumbers);
	}

	// 이하 석호-----------------------------------------------------//
	public List<Instaboard> instaListAll() {
		return instaboardRepository.findAll();
	}
}
