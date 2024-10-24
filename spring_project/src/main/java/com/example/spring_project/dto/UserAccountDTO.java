package com.example.spring_project.dto;


import java.sql.Timestamp;

import com.example.spring_project.entity.UserAccounts;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserAccountDTO {
    private Long accountId;
    private String ownerUserid;
    private String linkedUserid;
    // Timestamp : 레코드가 삽입될 때 자동으로 현재 시간으로 설정
    private Timestamp createdAt;
    
    public UserAccounts toEntity() {
    	return new UserAccounts(accountId, ownerUserid, linkedUserid, createdAt);
    }
}
