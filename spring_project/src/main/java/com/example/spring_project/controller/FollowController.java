package com.example.spring_project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.spring_project.dto.FollowDTO;
import com.example.spring_project.entity.Follow;
import com.example.spring_project.entity.UserDB;
import com.example.spring_project.service.FollowService;
import com.example.spring_project.service.UserDBService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
public class FollowController {
	@Autowired
	FollowService followService;
	@Autowired
	UserDBService userDBService;

	@Value("${project.upload.path}")
	private String uploadpath;
	
	// 팔로우하기
	@PostMapping("/follow")
    public ResponseEntity<String> follow(FollowDTO dto, Model model, HttpServletRequest request, @RequestParam("to_user") String to_user) {
		HttpSession session = request.getSession();
		String from_user = (String)session.getAttribute("userid");
		
		if(!from_user.equals(to_user)) {
			dto.setFrom_user(from_user);
			dto.setTo_user(to_user);
			dto.setFollowcheck(0);
			
			try {
				followService.follow(dto);				
				// 팔로우 신청 한 사람(from_user)의 팔로우 수 수정
				UserDB userdb = userDBService.userdbInfo(from_user);
				int countFollow = followService.findFollowList(from_user).size();
				userdb.setFollow(countFollow);
				userDBService.followUpdate(userdb);
				// 팔로우 신청을 받은 사람(to_user)의 팔로우/팔로워 수 수정				
				userdb = userDBService.userdbInfo(to_user);
				int countFollower = followService.findFollowerList(to_user).size();
				userdb.setFollower(countFollower);
				userDBService.followerUpdate(userdb);
	            // 저장이 성공적으로 이루어졌다면 성공 응답을 반환
	            return ResponseEntity.ok("팔로우 성공");
	        } catch (Exception e) {
	            // 실패 시 에러 응답을 반환
	        	e.printStackTrace();
	            return ResponseEntity.status(500).body("팔로우 중 오류가 발생하였습니다.");
	        }		
		} else {
			return ResponseEntity.status(500).body("자신은 팔로우 못함");
		}
    }	
	
	// 알람(삭제)
	@GetMapping("/followAlarm")
	public String followAlarm(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String to_user = (String)session.getAttribute("userid");
		//System.out.println(to_user);
		List<Follow> list = followService.followAccept(to_user, 0);
		//System.out.println(list);
		model.addAttribute("list", list);
		
		return "/follow/followAlarm";
	}
	
	// 맞팔
	@PostMapping("/followResponse")
	public ResponseEntity<String> followResponse(Model model, HttpServletRequest request) {
		String from_user = request.getParameter("from_user");
		HttpSession session = request.getSession();
		String to_user = (String)session.getAttribute("userid");
		
		try {
			// 기존데이터 가져오기
			Follow follow = followService.followM(from_user, to_user);
			// 수정내용 저장
			FollowDTO dto = new FollowDTO();
			dto.setSeq(follow.getSeq());
			dto.setFrom_user(from_user);
			dto.setTo_user(to_user);
			dto.setFollowcheck(1);
			followService.follow(dto);
			
			// 맞팔로우 한 사람(to_user)의 팔로우 수 증가
			UserDB userdb = userDBService.userdbInfo(to_user);
			int countFollow = followService.findFollowList(to_user).size();
			userdb.setFollow(countFollow);
			userDBService.followUpdate(userdb);
			// 맞팔로우 신청을 받은 사람(from_user)의 팔로워 수 증가
			userdb = userDBService.userdbInfo(from_user);
			int countFollower = followService.findFollowerList(from_user).size();
			userdb.setFollower(countFollower);
			userDBService.followerUpdate(userdb);			
			
            // 수정이 성공적으로 이루어졌다면 성공 응답을 반환
            return ResponseEntity.ok("Followed successfully");
        } catch (Exception e) {
            // 실패 시 에러 응답을 반환
            return ResponseEntity.status(500).body("Error occurred while following user");
        }		
	}
	
	// 언팔
	@PostMapping("/unFollowUser")
	@Transactional
	public ResponseEntity<String> unFollowUser(@RequestParam("to_user") String to_user, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String from_user = (String)session.getAttribute("userid");
		//System.out.println("테스트 = " + to_user);
	    try {
	    	// 언팔하기
	        followService.unFollowUser(from_user, to_user);	        
	        // 언팔한사람(나)의 팔로우 -1
	        UserDB userdb = userDBService.userdbInfo(from_user);
			int countFollow = followService.findFollowList(from_user).size();
			userdb.setFollow(countFollow);
			userDBService.followUpdate(userdb);
	        // 상대방 팔로워 -1        
			userdb = userDBService.userdbInfo(to_user);
			int countFollower = followService.findFollowerList(to_user).size();
			userdb.setFollower(countFollower);
			userDBService.followerUpdate(userdb);		
			
	        return ResponseEntity.ok("Unfollow successful");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error unfollowing user");
	    }
	}
}
