package com.example.spring_project.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.spring_project.dto.InstaboardDTO;
import com.example.spring_project.entity.Follow;
import com.example.spring_project.entity.InstaComment;
import com.example.spring_project.entity.Instaboard;
import com.example.spring_project.entity.Message;
import com.example.spring_project.entity.UserDB;
import com.example.spring_project.service.FollowService;
import com.example.spring_project.service.HashtagService;
import com.example.spring_project.service.InstaboardService;
import com.example.spring_project.service.MessageService;
import com.example.spring_project.service.UserAccountService;
import com.example.spring_project.service.UserDBService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class InstaboardController {
	@Autowired
	InstaboardService boardService;
	@Autowired
	FollowService followService;
	@Autowired
	UserDBService userService;
	@Autowired
	MessageService messageService;
	@Autowired
	HashtagService hashtagService;
	@Autowired
	UserAccountService accountService;

	@Value("${project.upload.path}")
	private String uploadpath;

	// 메인---------------------------------------------------------------//
	@GetMapping("/mainForm")
	public String mainForm(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String from_user = (String) session.getAttribute("userid");
		// System.out.println(to_user);
		// 성찬------------------------------------------------------------------------------
		List<InstaComment> instacomments = new ArrayList<>();
		// 아이디로 게시글 + 댓글 전체 받아오기
		List<Instaboard> insta_list = boardService.instaList(from_user);

		// 해당 사용자의 연결된 계정 목록 가져오기
		List<UserDB> Accounts = accountService.getAccountsList(from_user);

		// 계정 목록을 모델에 추가
		model.addAttribute("Accounts", Accounts);

		// 각 게시글에 대한 댓글들을 함께 조회하여 모델에 추가
		for (Instaboard post : insta_list) {
			instacomments = boardService.getCommentByBoardSeq(post.getSeq());
			post.setComments(instacomments != null ? instacomments : new ArrayList<>()); // Null 방지
		}

		// hashtag를 link로 변환
		for (Instaboard post : insta_list) {
			post.setBoard_content(hashtagService.convertHashtagsToLinks(post.getBoard_content()));
		}

		// 석호------------------------------------------------------------------------------
		// 전체 게시글 받아오기
		List<Instaboard> insta_list_all = boardService.instaListAll();
		// 내정보 가져오기
		UserDB mydb = userService.userdbInfo(from_user);
		// 모든 유저 db 리스트 가져오기
		List<UserDB> all_user_list = userService.getAllUser();
		// 알람리스트 가져오기
		List<Follow> follow_list = followService.followAccept(from_user, 0);
		// 회원 DB에서 추천 리스트 가져오기
		List<UserDB> recomend_list = followService.recomendList(from_user);
		// 맞팔 리스트 받아오기
		List<UserDB> followM_list = messageService.findFriendList(from_user);
		// 맞팔인 사람 메세지 리스트 받아오기
		Map<String, List<Message>> followM_message_map = messageService.findFollowMessageList(from_user, followM_list);
		// System.out.println(all_user_list);
		// 전체 회원 리스트를 섞어서 무작위로 만듬
		Collections.shuffle(recomend_list, new Random());
		// 5개의 항목을 선택
		recomend_list.stream().limit(5).collect(Collectors.toList());

		// 맞팔 리스트 섞어서 무작위로 만듬
		List<UserDB> followM_list_random = new ArrayList<>();
		Random random = new Random();
		// 임시 리스트에서 선택된 인덱스 추적을 위한 리스트
		List<Integer> selectedIndices = new ArrayList<>();

		// 원본 리스트에서 10개의 무작위 항목 선택
		while (followM_list_random.size() < 10 && followM_list_random.size() < followM_list.size()) {
			int randomIndex = random.nextInt(followM_list.size()); // 무작위 인덱스 생성

			// 이미 선택된 인덱스는 건너뛰기
			if (!selectedIndices.contains(randomIndex)) {
				followM_list_random.add(followM_list.get(randomIndex));
				selectedIndices.add(randomIndex);
			}
		}
		// System.out.println(followM_list);
		// System.out.println(followM_list_random);

		// ObjectMapper를 사용해 all_user_list를 JSON으로 변환
		ObjectMapper objectMapper = new ObjectMapper();
		String allUserListJson = null;
		try {
			allUserListJson = objectMapper.writeValueAsString(all_user_list);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		// ObjectMapper를 사용해 followM_list_random을 JSON으로 변환
		ObjectMapper objectMapper2 = new ObjectMapper();
		String followMListRandomJson = null;
		try {
			followMListRandomJson = objectMapper2.writeValueAsString(followM_list_random);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		// System.out.println(followM_list_random);

		// ObjectMapper를 사용해 insta_list_all을 JSON으로 변환
		ObjectMapper objectMapper3 = new ObjectMapper();
		String instaListAllJson = null;
		try {
			instaListAllJson = objectMapper3.writeValueAsString(insta_list_all);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		// 모든 사용자의 메시지 중에서 'getuser'가 'from_user'이면서 'readcheck'가 0인 메시지가 있는지 확인
		boolean hasUnreadMessages = followM_message_map.values().stream()
		    .flatMap(List::stream) 
		    .anyMatch(message -> message.getGetuser().equals(from_user) && message.getReadcheck() == 0); 


		// insta_list를 가지고 pfimage 정보 가져오기
		List<String> pfimage_list = new ArrayList<>();
		for (int i = 0; i < insta_list.size(); i++) {
			UserDB userdb = userService.userdbInfo(insta_list.get(i).getUserid());
			pfimage_list.add(userdb.getPFimage());
		}
		// System.out.println(pfimage_list);

		// 데이터 공유
		model.addAttribute("all_user_list", allUserListJson); // 모든 유저 리스트
		model.addAttribute("insta_list_all", instaListAllJson); // 모든 게시글 리스트
		model.addAttribute("insta_list", insta_list); // 내 게시글 공유
		model.addAttribute("mydb", mydb); // 내 정보 공유
		model.addAttribute("follow_list", follow_list); // 알림 리스트 공유
		model.addAttribute("recomend_list", recomend_list); // 친구추천 리스트 공유
		model.addAttribute("from_user", from_user); // 로그인한 아이디 받아오기
		model.addAttribute("followM_list", followM_list); // 맞팔 친구 list
		model.addAttribute("followM_list_random", followM_list_random); // 맞팔 친구 list 중 랜덤 10명
		model.addAttribute("followM_list_random_json", followMListRandomJson);
		model.addAttribute("followM_message_map", followM_message_map); // 맞팔 친구 별 메세지가 저장된 map
		model.addAttribute("hasUnreadMessages", hasUnreadMessages);     // 메세지 읽음 상태
		model.addAttribute("instacomments", instacomments);
		model.addAttribute("pfimage_list", pfimage_list);
		//System.out.println(followM_message_map);
		//System.out.println(hasUnreadMessages);
		return "mainForm";
	}

	// 이하
	// 성찬------------------------------------------------------------------------//
	// 릴스창으로 꺼내오기
	@GetMapping("/instaReels")
	public String instaReels(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String from_user = (String) session.getAttribute("userid");

		// 게시글 + 댓글 전체 받아오기
		List<Instaboard> insta_list = boardService.instaList(from_user);

		List<InstaComment> instacomments = new ArrayList<>();
		// 각 게시글에 대한 댓글들을 함께 조회하여 모델에 추가
		for (Instaboard post : insta_list) {
			instacomments = boardService.getCommentByBoardSeq(post.getSeq());
			post.setComments(instacomments != null ? instacomments : new ArrayList<>()); // Null 방지
			post.setCommentCount(instacomments != null ? instacomments.size() : 0); // 댓글 수 설정
		}

		List<Instaboard> videoPosts = insta_list.stream()
				.filter(Instaboard -> Instaboard.getBoard_file().toLowerCase().endsWith(".mp4"))
				.collect(Collectors.toList());
		//System.out.println("videoPosts = " + videoPosts);
		
		// 아이디
		List<String> postUserIdList = new ArrayList<String>(); // 아이디 저장할 리스트
		for(int i=0; i<insta_list.size(); i++) {
			postUserIdList.add(insta_list.get(i).getUserid());
		}
		// 아이디로 유저정보 가져오기
		List<UserDB> postUserList = new ArrayList<UserDB>();
		for(int i=0; i<postUserIdList.size(); i++) {
			postUserList.add(userService.userdbInfo(postUserIdList.get(i)));
		}
				
		// 데이터 공유
		model.addAttribute("insta_list", insta_list); // 게시글 공유
		model.addAttribute("instacomments", instacomments);
		model.addAttribute("videoPosts", videoPosts);
		model.addAttribute("postUserList", postUserList);

		return "instaboard/instaReels";
	}

	// 게시글작성폼 호출
	@GetMapping("/writeBoardForm")
	public String writeBoardForm() {
		return "instaboard/writeBoardForm";
	}

	@PostMapping("/writeBoardForm2")
	public String writeBoardForm2(Model model, HttpServletRequest request,
			@RequestParam("files") List<MultipartFile> list) {
		List<String> list_str = new ArrayList<>();
		// 1. 데이터 처리 및 파일 저장

		HttpSession session = request.getSession();
		// String userId = (String) session.getAttribute("userId");
		String userId = (String) session.getAttribute("userid");
		for (int i = 0; i < list.size() && i < 20; i++) {
			String fileName = list.get(i).getOriginalFilename();

			if (fileName != null && !fileName.isEmpty()) {
				File file = new File(uploadpath, fileName);

				try {
					list.get(i).transferTo(file);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				list_str.add(fileName);

				model.addAttribute("fileName" + (i + 1), fileName);

			}
		}

		// dao로 db에 저장
		Instaboard instaboard = boardService.insertImage(userId, list_str);
		// 2. 1 데이터 공유
		model.addAttribute("list_str", list_str);
		model.addAttribute("instaboard", instaboard);

		return "instaboard/writeBoardForm2";
	}

	// 게시글 작성
	@GetMapping("/writeBoard")
	public String writeBoard(Model model, HttpServletRequest request, InstaboardDTO dto) {

		// 1. 데이터 처리 및 파일 저장
		int seq = Integer.parseInt(request.getParameter("seq"));
		String files = request.getParameter("img");
		HttpSession session = request.getSession();
		// String userId = (String) session.getAttribute("userId");
		String userId = (String) session.getAttribute("userid");
		String board_content = request.getParameter("board_content");

		// 2. 데이터 저장
		dto.setBoard_content(board_content);
		dto.setBoard_date(new Date());

		Instaboard instaboard = boardService.insertContent(seq, userId, dto);

		// hashtag 처리
		hashtagService.addHashtagsToPost(instaboard, dto.getBoard_content());

		// 2. 1 데이터 공유
		model.addAttribute("userId", userId);
		model.addAttribute("instaboard", instaboard);
		model.addAttribute("files", files);

		// 3. view 처리 파일 선택
		return "instaboard/writeBoard";
	}

	// 게시글 삭제
	@GetMapping("/instaboardDelete")
	public String instaboardDelete(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		// 1. 데이터 처리
		String from_user = (String) session.getAttribute("userid");
		int seq = Integer.parseInt(request.getParameter("seq"));
		int result = boardService.instaboardDelete(seq, from_user);

		model.addAttribute("result", result);

		return "instaboard/instaboardDelete";
	}

	// 댓글 작성
	@PostMapping("/insertComment")
	public String insertComment(@RequestParam("post_seq") int post_seq, @RequestParam("content") String content,
			Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		// 로그인 유저 받아오기
		String to_user = (String) session.getAttribute("userid");
		// 댓글 추가
		boardService.insertComment(to_user, content, post_seq);

		// 댓글이 추가된 이후 해당 게시글과 댓글 리스트를 다시 불러옴
		Instaboard post = boardService.findBoard(post_seq);
		List<InstaComment> comments = boardService.getCommentByBoardSeq(post_seq);
		post.setComments(comments);
		// 내정보 가져오기
		UserDB mydb = userService.userdbInfo(to_user);

		return "redirect:/mainForm?post_seq=" + post_seq;
	}

	// 댓글 삭제
	@GetMapping("/deleteComment")
	public String deleteComment(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		String userid = request.getParameter("commentId"); // 댓글 작성자 ID
		int post_seq = Integer.parseInt(request.getParameter("post_seq")); // 게시글 ID
		int id = Integer.parseInt(request.getParameter("getId")); // 댓글 고유 ID

		// 댓글을 삭제하는 서비스 호출
		int result = boardService.deleteComment(id, userid);

		// 댓글이 삭제된 이후 해당 게시글과 댓글 리스트를 다시 불러옴
		Instaboard post = boardService.findBoard(post_seq);
		List<InstaComment> comments = boardService.getCommentByBoardSeq(post_seq);
		post.setComments(comments);

		model.addAttribute("post_seq", post_seq);
		model.addAttribute("result", result);

		return "instaboard/deleteComment";
	}

	// 처음에 2개의 댓글만 가져오는 요청
	@GetMapping("/{postSeq}/comments")
	public ResponseEntity<List<InstaComment>> getInitialComments(@PathVariable("postSeq") int postSeq) {
		List<InstaComment> comments = boardService.getInitialComments(postSeq);
		if (comments.isEmpty()) {
			// 댓글이 없을 경우 NO_CONTENT 상태를 반환
			return ResponseEntity.noContent().build();
		} else {
			// 정상적인 경우 200 OK와 함께 댓글 리스트 반환
			return ResponseEntity.ok(comments);
		}
	}

	// 댓글 모두보기 클릭 시 모든 댓글을 가져오는 요청
	@GetMapping("/{postSeq}/comments/all")
	public ResponseEntity<List<InstaComment>> getAllComments(@PathVariable("postSeq") int postSeq) {
		List<InstaComment> comments = boardService.getAllComments(postSeq);
		if (comments.isEmpty()) {
			// 댓글이 없을 경우 NO_CONTENT 상태를 반환
			return ResponseEntity.noContent().build();
		} else {
			// 정상적인 경우 200 OK와 함께 댓글 리스트 반환
			return ResponseEntity.ok(comments);
		}
	}

	// 상세보기
	@GetMapping("/instaboardView")
	public String instaboardView(@RequestParam("userid") String userid, @RequestParam("fileName") String fileName,
			@RequestParam("content") String content, @RequestParam("seq") int seq, Model model,
			HttpServletRequest request) {
		// 파일 이름을 , 로 구분하여 리스트로 변환
		Instaboard dto = boardService.findBoard(seq);
		List<String> fileNames = Arrays.asList(fileName.split(","));
		List<InstaComment> commentList = boardService.getCommentByBoardSeq(seq);
		// 로그인 된 사용자 정보를 가져옴
		HttpSession session = request.getSession();
		String from_user = (String) session.getAttribute("userid");

		// 사용자가 좋아요한 게시글 목록 조회
		List<Integer> likedPostIds = boardService.getLikedPostIds(from_user);

		// 현재 게시글이 좋아요된 상태인지 확인
		boolean isLiked = likedPostIds.contains(seq);
		
		// 게시글 유저 정보
		UserDB postUserInfo = userService.userdbInfo(userid);
		// 모델 공유
		model.addAttribute("userid", userid);
		model.addAttribute("fileNames", fileNames); // 리스트로 저장
		model.addAttribute("content", content);
		model.addAttribute("commentList", commentList);
		model.addAttribute("dto", dto); // dto를 Model에 추가
		model.addAttribute("isLiked", isLiked); // 좋아요 상태 추가
		model.addAttribute("from_user", from_user); // 로그인 유저 공유
		model.addAttribute("postUserInfo", postUserInfo); // 로그인 유저 공유
		

		return "instaboard/instaboardView";
	}

	// 좋아요 추가
	@PostMapping("/likePost")
	@ResponseBody
	public Map<String, Object> likePost(@RequestParam("post_seq") int post_seq, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String userid = (String) session.getAttribute("userid");
		// 좋아요 추가 서비스 호출
		boardService.likePost(userid, post_seq);

		// 좋아요 개수 반환
		int likeCount = boardService.getLikeCount(post_seq);

		// 결과를 JSON 형태로 반환
		Map<String, Object> result = new HashMap<>();
		result.put("likeCount", likeCount);
		result.put("status", "success");

		return result;
	}

	// 좋아요 취소
	@PostMapping("/unlikePost")
	@ResponseBody
	public Map<String, Object> unlikePost(@RequestParam("post_seq") int post_seq, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String userid = (String) session.getAttribute("userid");

		// 좋아요 취소 서비스 호출
		boardService.unlikePost(userid, post_seq);

		// 좋아요 개수 반환
		int likeCount = boardService.getLikeCount(post_seq);

		// 결과를 JSON 형태로 반환
		Map<String, Object> result = new HashMap<>();
		result.put("likeCount", likeCount);
		result.put("status", "success");

		return result;
	}

	// 로그인 후 사용자가 좋아요한 게시글 목록을 가져오는 API
	@GetMapping("/likedPosts")
	public ResponseEntity<List<Integer>> getLikedPosts(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String userid = (String) session.getAttribute("userid");

		// 사용자가 좋아요한 게시글 목록 조회
		List<Integer> likedPostIds = boardService.getLikedPostIds(userid);

		return ResponseEntity.ok(likedPostIds);
	}

	// 게시글 수정 폼 열기
	@GetMapping("/instaModifyForm")
	public String instaModifyForm(@RequestParam("seq") int seq, Model model, HttpServletRequest request) {
		Instaboard instaboard = boardService.findBoard(seq);
		HttpSession session = request.getSession();
		String userid = (String) session.getAttribute("userid");

		UserDB userDB = userService.userdbInfo(userid);

		model.addAttribute("instaboard", instaboard);
		model.addAttribute("userDB", userDB);

		return "instaboard/instaModifyForm";

	}

	// 게시글 수정하기
	@PostMapping("/instaModify")
	public String instaModify(@RequestParam("seq") int seq, @RequestParam("board_content") String content,
			HttpServletRequest request) {

		// 수정된 게시글의 내용과 이미지를 업데이트
		Instaboard modifyBoard = boardService.instaModify(seq, content, uploadpath);

		return "redirect:/mainForm";
	}

	// 이하 은택
	// ------------------------------------------------------------------------------------------------------
	@GetMapping("/discoveryPage")
	public String discoveryPage(Model model) {
		// seq값에 최소값, 최대값 구하기
		int SeqMin = boardService.instaboardSeqMin();
		int SeqMax = boardService.instaboardSeqMax();
		//System.out.println(SeqMin + "," + SeqMax);
		// 랜덤 숫자 10개 생성
		Set<Integer> randomNumbers = SeqRandomNumbers(SeqMin, SeqMax, SeqMax);
		// 랜덤 숫자들 출력검사
		//System.out.println("랜덤 숫자들: " + randomNumbers);

		List<String> discoveryFiles = boardService.getRandomBoardFiles(randomNumbers);
		//System.out.println("discoveryFiles: " + discoveryFiles);
		// 데이터 공유
		model.addAttribute("discoveryFiles", discoveryFiles);

		return "instaboard/discoveryPage";
	}

	// 랜덤한 숫자 만들기
	private Set<Integer> SeqRandomNumbers(int min, int max, int count) {
		Random random = new Random();
		Set<Integer> numbers = new HashSet<>();

		while (numbers.size() < count) {
			int randomNumber = random.nextInt((max - min) + 1) + min;
			numbers.add(randomNumber);
		}
		//System.out.println(numbers);
		return numbers;
	}

}
