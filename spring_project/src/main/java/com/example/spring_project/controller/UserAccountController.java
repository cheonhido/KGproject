package com.example.spring_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.spring_project.dto.UserAccountDTO;
import com.example.spring_project.dto.UserDBDTO;
import com.example.spring_project.entity.UserDB;
import com.example.spring_project.service.UserAccountService;
import com.example.spring_project.service.UserDBService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

@Controller
public class UserAccountController {
	@Autowired
	UserAccountService accountService;
	
	@Autowired
	UserDBService dbService;

	// 계정 추가 창
	@GetMapping("/addAccountLoginForm")
	public String loginChange() {
		// 아이디로 db에서 정보 가져오기
		// 데이터 공유
		// 3. view 파일 선택
		return "/account/addAccountLoginForm";
	}
	
	@PostMapping("/ChangeLogin")
	public String modalLogin(HttpSession session, UserDBDTO dto, Model model, HttpServletRequest request) {
		// 1. 데이터 처리
		 // 기존 세션에서 주 계정 정보 가져오기
		String ownerUserid = (String)session.getAttribute("userid");

		// DB에서 로그인 처리
		UserDB userdb = dbService.login(dto);
	
		if (userdb != null) {// 로그인 성공
			// 추가 계정 저장
			UserAccountDTO accountDTO = new UserAccountDTO();
			accountDTO.setOwnerUserid(ownerUserid);
			accountDTO.setLinkedUserid(userdb.getUserid());
			
			boolean isSaved = accountService.saveNewAccount(accountDTO);
			
			if (isSaved) {
				// 세션 갱신
				session.setAttribute("userid", userdb.getUserid());
				session.setAttribute("username", userdb.getUsername());
				
				 // 데이터 공유
	            model.addAttribute("userdb", userdb);
			}
			
		}
		model.addAttribute("error", "로그인 실패");
	    return "/account/loginChangeOk";
	}
	
	@GetMapping("/AccountChange")
	public String AccountChange(HttpServletRequest request, Model model) {
		// 1. 데이터 처리
		String accountId = request.getParameter("accountId");
		
		UserDB userdb = dbService.userdbInfo(accountId);
		//System.out.println("userdb =" + userdb);
		HttpSession session = request.getSession();
		session.setAttribute("userid", userdb.getUserid());
		session.setAttribute("username", userdb.getUsername());
		// 2. 데이터 공유
		model.addAttribute("userdb",userdb);
		// 3. viwe 파일 처리 
		return "/user/loginOk";
	}
}
