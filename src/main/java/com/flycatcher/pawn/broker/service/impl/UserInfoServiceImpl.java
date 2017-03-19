package com.flycatcher.pawn.broker.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.flycatcher.pawn.broker.model.UserInfo;
import com.flycatcher.pawn.broker.repo.UserInfoRepository;
import com.flycatcher.pawn.broker.security.JwtUserFactory;
import com.flycatcher.pawn.broker.service.UserInfoService;


/**
 * @author kumar
 *
 */

@Service
@Validated
@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
public class UserInfoServiceImpl implements UserInfoService,UserDetailsService {
	
private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoServiceImpl.class);
	
	private final UserInfoRepository userInfoRepository;
	
	@Inject
	public UserInfoServiceImpl(final UserInfoRepository userInfoRepository){
		LOGGER.info("------------------ UserInfoServiceImpl Invoked ------------------");
		this.userInfoRepository=userInfoRepository;
	}

	@Override
	public UserDetails getUserInfoByUserName(String userName) {
		LOGGER.info("--- get User Details By userName --> {} ---",userName);
		//return this.userInfoRepository.findByUserNameIgnoreCase(userName);
		
		final UserInfo user =this.userInfoRepository.findByUserNameIgnoreCase(userName);

        if (user == null) {
        	
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", userName));
        } else {
            return JwtUserFactory.create(user);
        }

	}
	
	@Override
	public UserInfo getUserInfoUserName(String userName) {
		LOGGER.info("--- get User Info By userName --> {} ---",userName);
				
		final UserInfo user =this.userInfoRepository.findByUserNameIgnoreCase(userName);

        if (user == null) {
        	
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", userName));
        } else {
            return user;
        }

	}

	@Override
	public List<UserInfo> getAllUserInfo(Sort sort) {
		LOGGER.info("--- get all user info by sort --> {} ---",sort);
		return this.userInfoRepository.findAll(sort);
	}

	@Override
	public UserInfo getUserInfoById(long userInfoId) {
		LOGGER.info("--- get user info by user info id --> {} ---",userInfoId);
		return this.userInfoRepository.findOne(userInfoId);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	public UserInfo createNewUserInfo(UserInfo userInfo) {
		LOGGER.info("--- create new user info ---> {} ---",userInfo);
		return this.userInfoRepository.saveAndFlush(userInfo);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	public UserInfo updateUserInfo(UserInfo userInfo) {
		LOGGER.info("--- update user info --> {} ---",userInfo);
		return this.userInfoRepository.saveAndFlush(userInfo);
	}

	@Override
	public Page<UserInfo> getPageOfUserInfo(String searchField,
			Pageable pageable) {
		LOGGER.info("--- get page of user info , searchField --> {} , pageable --> {} ---",searchField,pageable);
		return this.userInfoRepository.findAllUserInfoByPage(searchField, pageable);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	public void deleteUserInfoById(long userInfoId) {
		LOGGER.info("--- delete user info by userInfoId --> {} ---",userInfoId);
		this.userInfoRepository.delete(userInfoId);
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		LOGGER.info("--- load user object by username --> {} ---",username);
		UserInfo userInfo=this.userInfoRepository.findByUserNameIgnoreCase(username);
		if (userInfo == null) {
			LOGGER.info("--- No user found with username --> {} ---", username);
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
        	LOGGER.info("--- user found ---> {} ---",userInfo);
            return JwtUserFactory.create(userInfo);
        }
	}

}
