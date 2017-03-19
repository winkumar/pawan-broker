package com.flycatcher.pawn.broker.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.flycatcher.pawn.broker.model.Account;
import com.flycatcher.pawn.broker.repo.AccountRepository;
import com.flycatcher.pawn.broker.service.AccountService;

/**
 * 
 * @author kumar
 * @version 1.0.0
 * @since 19-Mar-2017
 * 
 */
@Service
@Validated
@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
public class AccountServiceImpl implements AccountService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);
 
	private final AccountRepository accountRepository;
	
	@Inject
	public AccountServiceImpl(final AccountRepository accountRepository) {
		LOGGER.info("--- Account Service Implementation ---");
		this.accountRepository=accountRepository;
	}
	
	@Override
	public Page<Account> getPageOfAccount(String search, Pageable pageable) {
		LOGGER.debug("--- get page of account by search -> {} , pageable -> {}  ---",search,pageable);
		return this.accountRepository.findAccountByPage(search,pageable);
	}
	
	@Override
	public List<Account> getAllAccount(Sort sort) {
		LOGGER.debug("--- get all account , sort -> {} ---",sort);
		return this.accountRepository.findAll(sort);
	}
	
	@Override
	public Account getAccountById(Long accountId) {
		LOGGER.debug("--- get account by id -> {} ---",accountId);
		return this.accountRepository.findOne(accountId);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	public Account createOrUpdateAccount(Account account) {
		LOGGER.debug("--- create or update , account -> {} ---",account);
		return this.accountRepository.saveAndFlush(account);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	public void removeAccountById(Long accountId) {
		LOGGER.debug("--- remove account by id -> {} ---",accountId);
		this.accountRepository.delete(accountId);
	}

}


