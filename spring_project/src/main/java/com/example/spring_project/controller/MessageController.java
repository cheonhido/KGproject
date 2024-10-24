package com.example.spring_project.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.spring_project.dto.MessageDTO;
import com.example.spring_project.entity.Instaboard;
import com.example.spring_project.entity.Message;
import com.example.spring_project.entity.UserDB;
import com.example.spring_project.service.FollowService;
import com.example.spring_project.service.InstaboardService;
import com.example.spring_project.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
public class MessageController {
	@Autowired
	MessageService messageService;
	@Autowired
	FollowService followService;
	@Autowired
	InstaboardService instaboardService;

	@Value("${project.upload.path}")
	private String uploadpath;

	// 이하 석호---------------------------------------------------//
	@GetMapping("/sendMessageForm")
	public String sendMessageForm(Model model, HttpServletRequest request) {

		return "message/sendMessageForm";
	}

	@GetMapping("/sendMessage")
	@Transactional
	public String sendMessage(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String from_user = (String) session.getAttribute("userid");
		String to_user = request.getParameter("to_user");
		
		// message 데이터 가져오기
		List<Message> message_list = messageService.getMessage(from_user, to_user);
		//System.out.println("메세지 = " + message_list);
		
		// getuser가 from_user인 메세지데이터의 readcheck를 1로 변경(읽었는지 확인)
		messageService.readCheck(from_user, to_user);
		
		// 날짜별로 메시지를 그룹화
		Map<LocalDate, List<Message>> message_list_map= message_list.stream()
	            .sorted(Comparator.comparing(Message::getLogdate)) // logdate 기준으로 오름차순 정렬
	            .collect(Collectors.groupingBy(Message::getLogdate, LinkedHashMap<LocalDate, List<Message>>::new, Collectors.toList()));

		model.addAttribute("message_list", message_list);
		model.addAttribute("from_user", from_user);
		model.addAttribute("to_user", to_user);
		model.addAttribute("message_list_map", message_list_map);
		//System.out.println(message_list_map);
		return "message/sendMessage";
	}

	// 메세지 저장
	@PostMapping("/saveMessage")
	@ResponseBody
	public String saveMessage(@RequestBody MessageDTO dto, Model model, HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();

			Message message = new Message();
			message.setSenduser((String) session.getAttribute("userid"));
			message.setGetuser(dto.getGetuser());
			message.setMessage(dto.getMessage());
			message.setReadcheck(0);
			message.setLogdate(LocalDate.now());
			message.setLogtime(LocalDateTime.now());
			message.setDay_of_week(LocalDateTime.now().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN));

			messageService.saveMessage(message);
			return "메시지가 성공적으로 저장되었습니다."; // 성공 시 메시지 반환
		} catch (Exception e) {
			return "메시지 저장에 실패했습니다."; // 실패 시 메시지 반환
		}
	}

	@GetMapping("/getFriendList")
	@ResponseBody
	public String getFriendList(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String from_user = (String) session.getAttribute("userid");

		// 회원 DB에서 추천 리스트 가져오기
		List<UserDB> before_list = messageService.findFriendList(from_user);

		// 데이터 공유 (JSON으로 변환)
		ObjectMapper objectMapper = new ObjectMapper();
		String friend_list = null;
		try {
			friend_list = objectMapper.writeValueAsString(before_list);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		// System.out.println("test = " + friend_list);
		model.addAttribute("friend_list", friend_list); // 전체 친구추천 리스트 공유(맞팔 안되어 있는 사람들임)

		// 회원 DB에서 추천 리스트 가져오기
		return friend_list;
	}

	@GetMapping("/findFollower")
	public String findFollower() {
		return "message/findFollower";
	}

	// 게시글의 공유하기 => DM으로 전송하기
	@PostMapping("/sendSelectedFollowers")
	@ResponseBody
	public Map<String, Object> sendSelectedFollowers(@RequestBody Map<String, Object> data,
			HttpServletRequest request) {
		// 로그인 id가져오기
		HttpSession session = request.getSession();
		// 공유 할 user list
		List<String> selectedFollowers = (List<String>) data.get("followers");

		// 선택된 팔로워 리스트 처리 (예시로 팔로워들로 메시지 보내는 로직 구현)
		// 공유할 게시물 저장
		String sharePostSeqStr = (String) data.get("share_post");
		Integer share_post_seq = Integer.parseInt(sharePostSeqStr); // 명시적으로 String을 Integer로 변환
		Instaboard share_post = instaboardService.findBoard(share_post_seq);
		if (share_post != null) {
			for (String follower : selectedFollowers) {
				Message message = new Message();
				message.setSenduser((String) session.getAttribute("userid"));
				message.setGetuser(follower);
				message.setMessage(share_post.getBoard_file());
				message.setReadcheck(0);
				message.setLogdate(LocalDate.now());
				message.setLogtime(LocalDateTime.now());
				message.setDay_of_week(
						LocalDateTime.now().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN));

				messageService.saveMessage(message);
			}
		}
		// 메세지가 있으면 함께 전송
		String share_message = (String) data.get("share_message");
		if (share_message.trim() != null && share_message.trim() != "") {
			for (String follower : selectedFollowers) {
				Message message = new Message();
				message.setSenduser((String) session.getAttribute("userid"));
				message.setGetuser(follower);
				message.setMessage(share_message);
				message.setReadcheck(0);
				message.setLogdate(LocalDate.now());
				message.setLogtime(LocalDateTime.now());
				message.setDay_of_week(
						LocalDateTime.now().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN));

				messageService.saveMessage(message);
			}
		}

		// 성공 응답 반환
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		return response;
	}
}
