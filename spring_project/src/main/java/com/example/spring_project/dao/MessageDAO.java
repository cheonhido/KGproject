package com.example.spring_project.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.spring_project.dto.MessageDTO;
import com.example.spring_project.dto.UserDBDTO;
import com.example.spring_project.entity.Instaboard;
import com.example.spring_project.entity.Message;
import com.example.spring_project.entity.UserDB;
import com.example.spring_project.repository.MessageRepository;

@Repository
public class MessageDAO {
	@Autowired
	MessageRepository messageRepository;


	public List<UserDB> findFriendList(String from_user) {
		List<Instaboard> list = new ArrayList<>();
		List<Object[]> results = messageRepository.findFriendList(from_user);
		return results.stream()
		        .map(result -> new UserDB(
		            (String) result[0], 
		            (String) result[1], 
		            (String) result[2],
		            (String) result[3], 
		            (String) result[4], 
		            (String) result[5], 
		            (String) result[6],
		            (String) result[7], 
		            Optional.ofNullable((BigDecimal) result[8]).map(BigDecimal::intValue).orElse(0),  // follow가 null이면 0으로 설정
		            Optional.ofNullable((BigDecimal) result[9]).map(BigDecimal::intValue).orElse(0),  // follower가 null이면 0으로 설정
		            (Date) result[10],
		            list
		        ))
		        .collect(Collectors.toList());
	}


	public List<Message> getMessage(String from_user, String to_user) {
		return messageRepository.getMessage(from_user, to_user);
	}


	public void saveMessage(Message message) {
		messageRepository.save(message);
	}


	public Map<String, List<Message>> findFollowMessageList(String from_user, List<UserDB> followM_list) {
	    // to_user별로 메시지를 저장할 Map
	    Map<String, List<Message>> userMessagesMap = new HashMap<>();
	    
	    // followM_list를 순회하면서 각 to_user에 대한 메시지를 가져옴
	    for (int i = 0; i < followM_list.size(); i++) {
	        String to_user = followM_list.get(i).getUserid();
	        
	        // from_user와 to_user에 대한 메시지 리스트를 가져옴
	        List<Message> messages = (List<Message>) messageRepository.getMessage(from_user, to_user);
	        
	        // to_user를 키로 해서 메시지 리스트를 Map에 저장
	        userMessagesMap.put(to_user, messages);
	    }
	    
	    return userMessagesMap;  // to_user별로 메시지를 저장한 Map을 반환
	}

	// 메세지 읽었는지 확인
	public void readCheck(String from_user, String to_user) {
		messageRepository.readCheck(from_user, to_user);
	}
}
