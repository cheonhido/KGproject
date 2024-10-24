package com.example.spring_project.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.spring_project.entity.Message;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MessageDTO {
    private String senduser;   // 보내는 사람
    private String getuser;    // 받는 사람
    private String message;    // 메시지
    private int readcheck;     // 읽음 여부 (0: false, 1: true)    
    private LocalDate logdate; // 연월일    
    private LocalDateTime logtime; // 시분초    
    private String day_of_week; // 요일
    
    public Message toEntity() {
    	return new Message(null, senduser, getuser, message, readcheck, logdate, logtime, day_of_week);
    }
}
