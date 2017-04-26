package com.flycatcher.pawn.broker.repo;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.flycatcher.pawn.broker.model.AccountType;

/**
 * 
 * @author kumar
 * @version 1.0.0
 * @since 06-Apr-2017
 * 
 */
public interface AccountTypeRepository extends JpaRepository<AccountType, Long> {
	
	@Query("SELECT a FROM AccountType a WHERE a.isOnlyForCashbook=?1")
	List<AccountType> findAll(Boolean withCashBook,Sort sort);
	
	@Query("SELECT a FROM AccountType a")
	List<AccountType> findAll(Sort sort);
	


}


