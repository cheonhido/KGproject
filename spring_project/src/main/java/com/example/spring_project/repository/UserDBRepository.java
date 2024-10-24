package com.example.spring_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spring_project.entity.UserDB;

public interface UserDBRepository extends JpaRepository<UserDB, String>{
	//이하 은택--------------------------------------------------------------------------------//
	UserDB findByUseridAndUserpwd(String userid, String userpwd);
	
	// linked_userid로 UserDB에서 사용자 정보를 찾기
	UserDB findByUserid(String userid);
}
