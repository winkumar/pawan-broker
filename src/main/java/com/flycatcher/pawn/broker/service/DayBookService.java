package com.flycatcher.pawn.broker.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.flycatcher.pawn.broker.model.Account;
import com.flycatcher.pawn.broker.model.DayBook;

/**
 * 
 * @author kumar
 * @version 1.0.0
 * @since 23-Mar-2017
 * 
 */
public interface DayBookService {
	
	Page<DayBook> getPageOfDayBook(String search,Pageable pageable);
	List<DayBook> getAllDayBook(Sort sort);
	List<DayBook> getDayBooks(Set<Account> accounts,Timestamp startDate,Timestamp endDate,Sort sort);
	List<DayBook> getDayBooks(Timestamp startDate,Timestamp endDate,Sort sort);
	List<DayBook> getDayBooks(Account account,Sort sort);
	List<DayBook> getDayBooks(Timestamp startDate,Timestamp endDate,Account account,Sort sort);
	DayBook getDayBookById(Long dayBookId);
	DayBook createOrUpdateDayBook(DayBook dayBook);
	void removeDayBookById(Long dayBookId);
	

}


