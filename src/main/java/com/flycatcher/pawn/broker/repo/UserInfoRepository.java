package com.flycatcher.pawn.broker.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.flycatcher.pawn.broker.model.UserInfo;

/**
 * 
 * @author kumar
 * @version 1.0.0
 * @since 19-Mar-2017
 * 
 */
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
	
	UserInfo findByUserNameIgnoreCase(String username);
    
    
    @Query("SELECT u FROM UserInfo u WHERE " +
            "LOWER(u.userName) LIKE LOWER(CONCAT('%',?1, '%')) ")
	Page<UserInfo> findAllUserInfoByPage(String searchTerm,Pageable pageable);
}


