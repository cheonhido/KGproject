package com.example.spring_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.spring_project.entity.UserAccounts;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccounts, Long> {
	@Query("SELECT a.ownerUserid FROM UserAccounts a WHERE a.linkedUserid = :linkedUserid")
	List<String> findOwnerUseridBylinkedUserid(@Param("linkedUserid") String linkedUserid);

	@Query("SELECT a.linkedUserid FROM UserAccounts a WHERE a.ownerUserid = :ownerUserid")
	List<String> findLinkedUseridsByOwnerUserid(@Param("ownerUserid") String ownerUserid);

}
