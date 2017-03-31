package com.flycatcher.pawn.broker.service;

import java.util.List;

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
	DayBook getDayBookById(Long dayBookId);
	DayBook createOrUpdateDayBook(DayBook dayBook);
	void removeDayBookById(Long dayBookId);
	List<DayBook> getAllDayBookByAccount(Account account,Sort sort);

}


