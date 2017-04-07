package com.flycatcher.pawn.broker.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.flycatcher.pawn.broker.model.Account;
import com.flycatcher.pawn.broker.model.AccountType;

/**
 * 
 * @author kumar
 * @version 1.0.0
 * @since 19-Mar-2017
 * 
 */
public interface AccountService {
	
	Page<Account> getPageOfAccount(String search,Pageable pageable);
	List<Account> getAllAccount(Sort sort);
	List<Account> getAllAccount(AccountType accountType,Sort sort);
	Account getAccountById(Long accountId);
	Account createOrUpdateAccount(Account account);
	void removeAccountById(Long accountId);
}


