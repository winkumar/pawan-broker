package com.flycatcher.pawn.broker.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.flycatcher.pawn.broker.model.AccountType;
import com.flycatcher.pawn.broker.repo.AccountTypeRepository;
import com.flycatcher.pawn.broker.service.AccountTypeService;

/**
 * 
 * @author kumar
 * @version 1.0.0
 * @since 06-Apr-2017
 * 
 */
@Service
@Validated
@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
public class AccountTypeServiceImpl implements AccountTypeService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountTypeServiceImpl.class);
	
	private AccountTypeRepository accountTypeRepository;
	
	@Inject
	public AccountTypeServiceImpl(final AccountTypeRepository accountTypeRepository){
		LOGGER.debug("--- AccountType Service Impl ---");
		
		this.accountTypeRepository=accountTypeRepository;
	}
	@Override
	public List<AccountType> getAllAccountType(Sort sort) {
		LOGGER.debug("--- get all account type , sort -> {} ---",sort);
		return this.accountTypeRepository.findAll(sort);
	}
	
	@Override
	public AccountType getAccountType(Long accountTypeId) {
		LOGGER.debug("--- get account type by accountTypeId -> {}  ---",accountTypeId);
		return this.accountTypeRepository.findOne(accountTypeId);
	}

}


