package com.example.spring_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.spring_project.entity.Message;

import jakarta.transaction.Transactional;

public interface MessageRepository extends JpaRepository<Message, Integer> {
	// 이하 석호	
	// 맞팔인 친구 리스트
    @Query(value = " SELECT u.userid, u.username, u.userpwd, u.email1, u.email2, u.gender, "
    			 + " u.PFimage, u.introduce, u.follow, u.follower, u.logtime "
    			 + " FROM UserDB u WHERE u.userid IN ( "
    			 + " SELECT f.to_user FROM Follow f "
    			 + " WHERE f.from_user=:from_user AND f.followcheck = 1 "
    			 + " UNION "
    			 + " SELECT f.from_user FROM Follow f "
    			 + " WHERE f.to_user=:from_user AND f.followcheck = 1 "
    			 + " ) "
           , nativeQuery = true)
    List<Object[]> findFriendList(@Param("from_user") String from_user);

    // 저장된 메세지 리스트(1명분) 가져오기
    @Query(value =" SELECT * FROM message "
    		+ " WHERE (senduser=:from_user AND getuser=:to_user) "
    		+ " OR (senduser=:to_user AND getuser=:from_user) "
    		+ " ORDER BY logdate ASC, logtime ASC "
    		, nativeQuery = true)
	List<Message> getMessage(@Param("from_user") String from_user, 
							 @Param("to_user")String to_user);

    // 메세지 읽었는지 확인
    @Modifying
    @Transactional
    @Query(value=" UPDATE message SET readcheck=1 WHERE senduser=:to_user AND getuser=:from_user "
    		+ " AND readcheck=0"
    		, nativeQuery = true)
	void readCheck(@Param("from_user") String from_user, 
				   @Param("to_user") String to_user);
}
