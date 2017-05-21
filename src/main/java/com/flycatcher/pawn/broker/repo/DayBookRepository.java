package com.flycatcher.pawn.broker.repo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.flycatcher.pawn.broker.model.Account;
import com.flycatcher.pawn.broker.model.DayBook;

/**
 * 
 * @author kumar
 * @version 1.0.0
 * @since 19-Mar-2017
 * 
 */
public interface DayBookRepository extends JpaRepository<DayBook, Long> {

	@Query("SELECT d FROM DayBook d WHERE d.account.accountId !=1 AND " +
            "( LOWER(d.transactionAmount) LIKE LOWER(CONCAT('%',?1, '%')) OR "
            +" LOWER(d.transactionType) LIKE LOWER(CONCAT('%',?1, '%')) OR "
            +" LOWER(d.transactionDate) LIKE LOWER(CONCAT('%',?1, '%')) OR "
            +" LOWER(d.account.accountNumber) LIKE LOWER(CONCAT('%',?1, '%')) OR "
            +" LOWER(d.createdDate) LIKE LOWER(CONCAT('%',?1, '%')))" )
	Page<DayBook> findDayBookByPage(String search,Pageable pageable);
	
	List<DayBook> findByAccount(Account account,Sort sort);
	
	@Query("SELECT d FROM DayBook d WHERE (d.transactionDate BETWEEN ?1 AND ?2) AND (d.account IN (?3))")
	List<DayBook> findDayBooks(Timestamp startDate,Timestamp endDate,Set<Account> accounts,Sort sort);
	
	
	@Query("SELECT d FROM DayBook d WHERE (d.transactionDate BETWEEN ?1 AND ?2)")
	List<DayBook> findDayBooks(Timestamp startDate,Timestamp endDate,Sort sort);
	
	@Query("SELECT d FROM DayBook d WHERE (d.transactionDate BETWEEN ?1 AND ?2) AND d.account= ?3")
	List<DayBook> findDayBooks(Timestamp startDate,Timestamp endDate,Account account,Sort sort);
}


