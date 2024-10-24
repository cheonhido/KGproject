package com.example.spring_project.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_project.dao.MessageDAO;
import com.example.spring_project.dto.MessageDTO;
import com.example.spring_project.entity.Message;
import com.example.spring_project.entity.UserDB;

@Service
public class MessageService {
	@Autowired
	MessageDAO dao;
	
	// 이하 석호
	// 메세지 가능 친구 리스트
	public List<UserDB> findFriendList(String from_user) {
		return dao.findFriendList(from_user);
	}
	
	// 저장된 메세지 리스트
	public List<Message> getMessage(String from_user, String to_user) {
		return dao.getMessage(from_user, to_user);
	}

	public void saveMessage(Message message) {
		dao.saveMessage(message);
	}

	public Map<String, List<Message>> findFollowMessageList(String from_user, List<UserDB> followM_list) {
		return dao.findFollowMessageList(from_user, followM_list);
	}

	public void readCheck(String from_user, String to_user) {
		dao.readCheck(from_user, to_user);		
	}
}
