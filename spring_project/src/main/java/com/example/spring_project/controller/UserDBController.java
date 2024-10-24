package com.example.spring_project.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.spring_project.dto.UserDBDTO;
import com.example.spring_project.entity.InstaComment;
import com.example.spring_project.entity.Instaboard;
import com.example.spring_project.entity.UserDB;
import com.example.spring_project.service.FollowService;
import com.example.spring_project.service.InstaboardService;
import com.example.spring_project.service.UserDBService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserDBController {
	@Autowired
	UserDBService service;
	@Autowired
	FollowService followService;
	@Autowired
	InstaboardService boardService;

	@Value("${project.upload.path}")
	private String uploadpath;

	// 이하 은택----------------------------------------------------------------------//
	// 로그인 화면
	@GetMapping("/loginForm")
	public String loginForm() {
		return "/user/loginForm";
	}

	// 회원가입 화면
	@GetMapping("/UserWriteForm")
	public String UserWriteForm() {
		return "/user/UserWriteForm";
	}

	// 아이디 중복 검사
	@GetMapping("/checkUserId")
	@ResponseBody
	public boolean checkUserId(@RequestParam("userid") String userid) {
		return service.checkId(userid);
	}

	// 회원가입처리
	@PostMapping("/UserWrite")
	public String UserWrite(UserDBDTO dto, Model model, HttpServletRequest request) {
		// 1. 데이터 처리
		// 기본 이미지 파일 이름 및 서버에 저장할 경로 설정
		String dfaultProfile = "goonyang_gram_basic_profile_bb.png"; // 기본 이미지
		Path uploadDir = Paths.get(uploadpath);

		// 기본 이미지를 서버에 저장할 파일 경로 설정
		Path destinationPath = uploadDir.resolve(dfaultProfile);

		// DTO에 기본 이미지 파일 이름 설정
		dto.setPFimage(dfaultProfile);

		try {
			// 기본 이미지 파일을 읽어들입니다
			Resource resource = new ClassPathResource("static/img/" + dfaultProfile);
			File destinationFile = destinationPath.toFile();

			// 이미지 파일을 서버 디렉터리에 복사합니다
			if (!destinationFile.exists()) {
				try (InputStream inputStream = resource.getInputStream();
						FileOutputStream outputStream = new FileOutputStream(destinationFile)) {
					byte[] buffer = new byte[1024];
					int bytesRead;
					while ((bytesRead = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, bytesRead);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			// 에러 발생 시 기본 이미지 파일 이름 설정
			dto.setPFimage(dfaultProfile);
		}

		// 기본이미지 설정
		dto.setPFimage(dfaultProfile);
		dto.setFollow(0);
		dto.setFollower(0);
		dto.setLogtime(new Date());

		// db
		boolean result = service.UserWrite(dto);
		// 2. 데이터 공유
		model.addAttribute("result", result);

		// 3. view 파일 선택
		return "/user/UserWrite";
	}

	// 로그인
	@PostMapping("/login")
	public String login(UserDBDTO dto, Model model, HttpServletRequest request) {
		// 1. 데이터 처리
		// db
		UserDB userdb = service.login(dto);
		if (userdb != null) { // 로그인 성공
			HttpSession session = request.getSession();
			session.setAttribute("userid", userdb.getUserid());
			session.setAttribute("username", userdb.getUsername());
			// 2. 데이터 공유
			model.addAttribute("userdb", userdb);
		}
		// 3. view 파일 선택
		return "/user/loginOK";
	}

	// MyPage
	@GetMapping("/MyPage")
	public String MyPage(Model model, HttpServletRequest request, @RequestParam("pageid") String pageid) {
		//System.out.println("id = " + pageid);
		HttpSession session = request.getSession();
		String userid = (String) session.getAttribute("userid");
		UserDB loginid = service.userdbInfo(userid);

		if (userid == null) {
			return "redirect:/login";
		}

		// db
		// 로그인한 사용자의 정보 및 저장된 게시글 목록 불러오기
		UserDB user = service.userdbInfo(userid);
		List<Instaboard> savedPosts = user.getSavedPosts();
		// 작성자의 정보를 불러오기
		UserDB mydb = service.userdbInfo(pageid);
		int postCount = boardService.countPostsByUserId(pageid);
		// 사용자의 이미지 URL 목록을 가져옴
		List<String> userImages = boardService.getUserImages(pageid);
		
		// 팔로워 리스트
		List<String> followerIDList = followService.findFollowerList(userid);
		List<UserDB> followerList = new ArrayList<UserDB>();
		for(int i=0; i<followerIDList.size(); i++) {
			UserDB followerUserDB = service.userdbInfo(followerIDList.get(i));
			followerList.add(followerUserDB);
		}
		
		// 팔로우 리스트
		List<String> followIDList = followService.findFollowList(userid);
		List<UserDB> followList = new ArrayList<UserDB>();
		for(int i=0; i<followIDList.size(); i++) {
			UserDB followUserDB = service.userdbInfo(followIDList.get(i));
			followList.add(followUserDB);
		}

		// 2. 데이터 공유
		model.addAttribute("mydb", mydb);
		model.addAttribute("loginid", loginid);
		model.addAttribute("postCount", postCount);
		model.addAttribute("userImages", userImages);
		model.addAttribute("savedPosts", savedPosts);
		model.addAttribute("followerList", followerList);
		model.addAttribute("followList", followList);
		// 3. view 파일 선택
		return "/user/MyPage";
	}

	// 프로필 편집 창 열기
	@GetMapping("/UserModifyForm")
	public String UserModifyForm(Model model, HttpServletRequest request) {
		// 1. 데이터 처리
		HttpSession session = request.getSession();
		String userid = (String) session.getAttribute("userid");
		// db
		UserDB userdb = service.userdbInfo(userid);

		// 2. 데이터 공유
		model.addAttribute("userdb", userdb);
		// 3. view 파일 선택
		return "/user/UserModifyForm";
	}

	// 사진 변경 창 열기
	@GetMapping("/changePicture")
	public String changePicture(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String userid = (String) session.getAttribute("userid");

		model.addAttribute("userid", userid);

		return "/user/changePicture";
	}

	// 사진 변경 창 열기
	@GetMapping("/changePicture2")
	public String changePicture2(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String userid = (String) session.getAttribute("userid");

		model.addAttribute("userid", userid);

		return "/user/changePicture2";
	}

	// 사진 업로드
	@PostMapping("/uploadProfilePicture")
	@ResponseBody
	public Map<String, Object> handleFileUpload(@RequestParam("profilePicture") MultipartFile uploadfile,
			HttpServletRequest request) {
		Map<String, Object> response = new HashMap<>();

		// 세션에서 사용자 id 받아오기
		HttpSession session = request.getSession();
		String userid = (String) session.getAttribute("userid");

		// 사용자 정보를 데이터베이스에서 조회
		UserDBDTO dto = service.userdbFindById(userid);

		// 파일명 처리
		String fileName = uploadfile.getOriginalFilename();
		if (!fileName.isEmpty()) {
			// 파일 저장
			File file = new File(uploadpath, fileName);
			try {
				// 파일을 지정된 경로에 저장
				uploadfile.transferTo(file);

				// 사용자 프로필 사진 업데이트
				dto.setPFimage(fileName);
				boolean result = service.profilePictureUpload(dto);

				// 결과를 JSON 형식으로 반환
				response.put("result", result);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
				response.put("result", false);
			}
		} else {
			response.put("result", false);
		}

		return response; // JSON 응답 반환
	}

	// 프로필 편집 - 기존 사진 삭제
	@GetMapping("/deleteProfilePicture")
	@ResponseBody
	public Map<String, Object> deleteProfilePicture(HttpServletRequest request) {
		Map<String, Object> response = new HashMap<>();

		try {
			// 세션이나 인증 정보를 통해 현재 사용자의 ID를 가져옵니다.
			String userId = (String) request.getSession().getAttribute("userid");
			if (userId == null) {
				throw new IllegalArgumentException("User ID not found in session");
			}

			// DB에서 현재 사용자의 프로필 사진 경로를 가져옵니다.
			String profilePicturePath = service.getProfilePicturePath(userId);
			if (profilePicturePath == null) {
				throw new IllegalStateException("Profile picture path not found");
			}

			// 서버 파일 시스템에서 기존 프로필 사진을 삭제합니다.
			File profilePictureFile = new File(profilePicturePath);
			if (profilePictureFile.exists()) {
				boolean deleted = profilePictureFile.delete();
				if (!deleted) {
					throw new RuntimeException("Failed to delete profile picture file");
				}
			}

			// 기본 이미지 경로 설정
			String defaultProfilePicturePath = "goonyang_gram_basic_profile_bb.png";

			// DB에서 사용자의 프로필 사진 경로를 기본 이미지 경로로 업데이트합니다.
			boolean updateResult = service.resetProfilePictureToDefault(userId);
			if (!updateResult) {
				throw new RuntimeException("Failed to update profile picture in database");
			}

			// 작업이 성공적으로 완료되었음을 클라이언트에 알립니다.
			response.put("result", true);
		} catch (Exception e) {
			// 오류가 발생한 경우, 예외를 처리하고 실패를 알립니다.
			e.printStackTrace();
			response.put("result", false);
		}

		return response; // JSON 응답 반환
	}

	// 프로필 편집 완료 처리
	@PostMapping("/userModify")
	public String userModify(Model model, HttpServletRequest request) {
		// 1. 데이터 처리
		// String website = request.getParameter("website");
		String gender = request.getParameter("gender");
		String introduce = request.getParameter("introduce");
		// 세션에서 사용자 id 받아오기
		HttpSession session = request.getSession();
		String userid = (String) session.getAttribute("userid");

		// 사용자 정보를 데이터베이스에서 조회
		UserDBDTO dto = service.userdbFindById(userid);
		// dto.setWebsite(website);
		dto.setGender(gender);
		dto.setIntroduce(introduce);

		// db
		boolean result = service.profileModify(dto);
		// System.out.println("dto = " + dto);

		// 2.데이터 공유
		model.addAttribute("result", result);
		model.addAttribute("userid", userid);

		// 3. view 파일 선택
		return "/user/UserModify";
	}

	// 로그아웃
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		// System.out.println("logout");
		// 1. 데이터 처리
		HttpSession session = request.getSession();
		session.removeAttribute("userid");
		session.removeAttribute("username");
		// 2. 데이터 공유
		// 3. view 파일 선택
		return "/user/logout";
	}

	// 이하 석호----------------------------------------------------------------------//
	/*
	 * 로그인 ok(테스트용)
	 * 
	 * @PostMapping("/loginOK_test") public String loginOK(Model model,
	 * HttpServletRequest request) { String userid = request.getParameter("userid");
	 * 
	 * UserDB userdb = service.userdbInfo(userid);
	 * 
	 * model.addAttribute("userdb", userdb);
	 * 
	 * HttpSession session = request.getSession(); session.setAttribute("userid",
	 * userid); session.setAttribute("username", userdb.getUsername()); return
	 * "/user/loginOK_test"; }
	 */

	// 친구추천 모두보기
	@GetMapping("/People")
	public String MyPage(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String to_user = (String) session.getAttribute("userid");

		// 회원 DB에서 추천 리스트 가져오기
		List<UserDB> people_list = followService.recomendList(to_user);

		// 리스트를 섞어서 무작위로 만듬
		Collections.shuffle(people_list, new Random());

		model.addAttribute("people_list", people_list); // 전체 친구추천 리스트 공유

		return "/user/People";
	}

	// 이하 성찬
	// --------------------------------------------------------------------------//
	@PostMapping("/toggleSavePost/{postId}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> toggleSavePost(@PathVariable("postId") int postId,
			HttpServletRequest request) {
		// 로그인 아이디 받아오기
		HttpSession session = request.getSession();
		String from_user = (String) session.getAttribute("userid");

		// id, seq로 게시글 조회
		boardService.toggleSavePost(postId, from_user);

		// 상태 변경 후 게시글 다시 조회
		Instaboard updatePost = boardService.findBoard(postId);
		// 상태 정보를 클라이언트에 반환
		Map<String, Object> result = new HashMap<>();
		result.put("saved", 1); // 저장 상태 반환
		return ResponseEntity.ok(result);

	}

	// 로그인후 사용자가 저장한 게시글 버튼 목록 불러오기
	@GetMapping("/savedPosts")
	public ResponseEntity<List<Integer>> getSavedPosts(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String userid = (String) session.getAttribute("userid");

		// 사용자가 저장한 게시글 목록 조회
		List<Integer> savedPostIds = boardService.getSavedPostIds(userid);
		return ResponseEntity.ok(savedPostIds);
	}

	// 상세페이지 댓글 작업
	@PostMapping("/comment/add")
	@ResponseBody
	public ResponseEntity<?> addComment(@RequestParam("postId") int postId, @RequestParam("content") String content,
			@RequestParam("userid") String userid) {

		// 게시글을 찾아 댓글 추가
		Instaboard post = boardService.findBoard(postId);
		if (post == null) {
			return ResponseEntity.badRequest().body("게시글을 찾을 수 없습니다.");
		}

		// 댓글 저장
		InstaComment comment = boardService.insertComment(userid, content, postId);

		// 댓글 목록 가져오기
		List<InstaComment> updatedComments = boardService.getCommentByBoardSeq(postId);

		return ResponseEntity.ok(updatedComments);
	}
}
