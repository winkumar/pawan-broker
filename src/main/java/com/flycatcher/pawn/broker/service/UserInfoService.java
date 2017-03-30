package com.flycatcher.pawn.broker.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;

import com.flycatcher.pawn.broker.model.UserInfo;



/**
 * @author kumar
 *
 */

public interface UserInfoService {
	
	 UserDetails getUserInfoByUserName(String userName);
	 UserInfo getUserInfoUserName(String userName);
	 List<UserInfo> getAllUserInfo(Sort sort);
	 UserInfo getUserInfoById(long userInfoId);
	 UserInfo createNewUserInfo(UserInfo userInfo);
	 UserInfo updateUserInfo(UserInfo userInfo);
	 Page<UserInfo> getPageOfUserInfo(String search,Pageable pageable);
	 void deleteUserInfoById(long userInfoId);
	 String getUserNameForAthentication();

}
