package com.flycatcher.pawn.broker.service;

import java.util.List;

import org.springframework.data.domain.Sort;


import com.flycatcher.pawn.broker.model.AccountType;

/**
 * 
 * @author kumar
 * @version 1.0.0
 * @since 06-Apr-2017
 * 
 */
public interface AccountTypeService {

	List<AccountType> getAllAccountType(Sort sort);
	AccountType getAccountType(Long accountTypeId);

}


