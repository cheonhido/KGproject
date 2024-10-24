package com.example.spring_project.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MESSAGE_SEQUENCE_GENERATOR")
    @SequenceGenerator(name = "MESSAGE_SEQUENCE_GENERATOR", sequenceName = "SEQ_MESSAGE", initialValue = 1, allocationSize = 1)
    private Long seq_message;   // 키
    
    private String senduser;   // 보내는 사람
    private String getuser;    // 받는 사람
    private String message;    // 메시지
    private int readcheck;     // 읽음 여부 (0: false, 1: true)
    
    private LocalDate logdate; // 연월일
    
    private LocalDateTime logtime; // 시분초
    
    private String day_of_week; // 요일
}
