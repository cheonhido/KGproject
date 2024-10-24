package com.example.spring_project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_project.dao.UserAccountDAO;
import com.example.spring_project.dto.UserAccountDTO;
import com.example.spring_project.entity.UserDB;

@Service
public class UserAccountService {
	@Autowired
	UserAccountDAO accountDAO;

	// 계정을 저장하는 서비스 메서드
	public boolean saveNewAccount(UserAccountDTO accountDTO) {
		return accountDAO.saveNewAccount(accountDTO);
	}

	// 추가 계정 리스트 불러오기
	public List<UserDB> getAccountsList(String to_user) {
		return accountDAO.getAccountsList(to_user);
	}
}
