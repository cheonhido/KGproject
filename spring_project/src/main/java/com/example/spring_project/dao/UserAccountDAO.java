package com.example.spring_project.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.spring_project.dto.UserAccountDTO;
import com.example.spring_project.entity.UserAccounts;
import com.example.spring_project.entity.UserDB;
import com.example.spring_project.repository.UserAccountRepository;
import com.example.spring_project.repository.UserDBRepository;

@Repository
public class UserAccountDAO {
	
	@Autowired
	UserAccountRepository accountRepository;
	
	@Autowired
	UserDBRepository dbRepository;
	
	// 추가 계정을 저장하는 메서드
	public boolean saveNewAccount(UserAccountDTO accountDTO) {
		boolean result = false;
		UserAccounts account = accountDTO.toEntity();
		UserAccounts account_result = accountRepository.save(account);
		
		if(account_result != null) {
			result = true;	
		}

		return result;
	}
	
	// 추가 계정 리스트 불러오기
	public List<UserDB> getAccountsList(String to_user){
		// userAccounts에서 linkedUserId가져오기
		List<String> ownerUserids = accountRepository.findOwnerUseridBylinkedUserid(to_user);
		List<String> linkedUserids = accountRepository.findLinkedUseridsByOwnerUserid(to_user);
		
		// onwerUserid와 linkedUserids를 합칠 리스트 생성 중복검사를 위해 HashSet 사용
		Set<UserDB> userDBsSet = new HashSet<>();
		
		// to_user의 ownerUserids 정보 추가
		for (String ownerUserId : ownerUserids ) {
			// ownerUserid들을 리스트에저장
			UserDB ownerUserDB = dbRepository.findByUserid(ownerUserId);
			if(ownerUserDB != null) {
				userDBsSet.add(ownerUserDB);
			}
			// ownerUserids의 linkedUserIds 정보 추가
			List<String> linkedUseridsForOwner = accountRepository.findLinkedUseridsByOwnerUserid(ownerUserId);
			for (String linkedUserIdForOwner : linkedUseridsForOwner) {
			    UserDB userDB = dbRepository.findByUserid(linkedUserIdForOwner);
			    // Accountid 도 리스트에서 제외
			    if (userDB != null && !userDB.getUserid().equals(to_user)) {
			    	userDBsSet.add(userDB);
			    }
			}
		}
		
		// to_user의 linkedUserIds 정보 추가
		for (String linkedUserId : linkedUserids) {
		    UserDB userDB = dbRepository.findByUserid(linkedUserId);
		    if (userDB != null) {
		    	userDBsSet.add(userDB);
		    }
		}
		
		// List로 변환
		List<UserDB> userDBs = new ArrayList<>(userDBsSet);
		return userDBs;
	}
}
