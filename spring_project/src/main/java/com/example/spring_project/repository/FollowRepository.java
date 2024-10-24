package com.example.spring_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.spring_project.entity.Follow;

import jakarta.transaction.Transactional;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
	// 이하 석호
	@Query(value = "select count(*) as cnt from follow where from_user=:from_user and to_user=:to_user", nativeQuery = true)
	int findFollow(@Param("from_user") String from_user, @Param("to_user") String to_user);

	@Query(value = "select * from follow where to_user=:to_user and followcheck=:followcheck", nativeQuery = true)
	List<Follow> checkFollow(@Param("to_user") String to_user, @Param("followcheck") int followcheck);

	@Query(value = "select * from follow where from_user=:from_user and to_user=:to_user", nativeQuery = true)
	Follow followM(@Param("from_user") String from_user, @Param("to_user") String to_user);

	// 친구추천 리스트
	@Query(value = " SELECT u.userid, u.username, u.userpwd, u.email1, u.email2, u.gender, "
			+ " u.PFimage, u.introduce, u.follow, u.follower, u.logtime "
			+ " FROM UserDB u WHERE u.userid NOT IN ( "
			+ " SELECT f.to_user FROM follow f WHERE f.from_user=:from_user) "
			+ " AND u.userid NOT IN (SELECT f.from_user FROM follow f WHERE f.to_user=:from_user) "
			+ " AND u.userid!=:from_user ", nativeQuery = true)
	List<Object[]> findUserData(@Param("from_user") String from_user);

	// 팔로우 리스트 구하기
	@Query(value = " SELECT to_user AS followlist FROM follow WHERE from_user=:from_user "
				 + " UNION "
				 + " SELECT from_user AS followlist FROM follow WHERE to_user=:from_user AND followcheck = 1 "
			, nativeQuery = true)
	List<String> findFollowList(@Param("from_user") String from_user);

	// 팔로워 리스트 구하기
	@Query(value = " SELECT from_user AS followerlist FROM follow WHERE to_user=:from_user "
				 + " UNION "
				 + " SELECT to_user AS followerlist FROM follow WHERE from_user=:from_user AND followcheck = 1 "
			, nativeQuery = true)
	List<String> findFollowerList(@Param("from_user") String from_user);
	
	// 언팔
	@Transactional
	@Query(value = " UPDATE follow "
			+ " SET followcheck = 0 "
			+ " WHERE "
			+ " (from_user=:from_user AND to_user=:to_user AND followcheck = 1) "
			+ " OR "
			+ " (from_user=:to_user AND to_user=:from_user AND followcheck = 1) "
			, nativeQuery = true)
	void unFollowUser(@Param("from_user") String from_user,
					  @Param("to_user") String to_user);
}

