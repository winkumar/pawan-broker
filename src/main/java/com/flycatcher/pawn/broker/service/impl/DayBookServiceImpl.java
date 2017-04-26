package com.flycatcher.pawn.broker.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

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
import com.flycatcher.pawn.broker.model.DayBook;
import com.flycatcher.pawn.broker.repo.DayBookRepository;
import com.flycatcher.pawn.broker.service.DayBookService;

/**
 * 
 * @author kumar
 * @version 1.0.0
 * @since 30-Mar-2017
 * 
 */
@Service
@Validated
@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
public class DayBookServiceImpl implements DayBookService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DayBookServiceImpl.class);

	private final DayBookRepository dayBookRepository;
	
	@Inject
	public DayBookServiceImpl(final DayBookRepository dayBookRepository) {
		LOGGER.debug("--- DayBook service implementation invoked ---");
		this.dayBookRepository=dayBookRepository;
	}

	@Override
	public Page<DayBook> getPageOfDayBook(String search, Pageable pageable) {
		LOGGER.debug("---get page of day book  , search -> {} , pageable -> {} ---",search,pageable);
		return this.dayBookRepository.findDayBookByPage(search, pageable);
	}

	@Override
	public List<DayBook> getAllDayBook(Sort sort) {
		LOGGER.debug("--- get all daybook , sort -> {} ---",sort);
		return this.dayBookRepository.findAll(sort);
	}

	@Override
	public DayBook getDayBookById(Long dayBookId) {
		LOGGER.debug("--- get daybook by id , dayBookId -> {} ---",dayBookId);
		return this.dayBookRepository.findOne(dayBookId);
	}

	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	public DayBook createOrUpdateDayBook(DayBook dayBook) {
		LOGGER.debug("--- create or update day book , daybook -> {} ---",dayBook);
		return this.dayBookRepository.saveAndFlush(dayBook);
	}

	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	public void removeDayBookById(Long dayBookId) {
		LOGGER.debug("--- remove daybook by dayBookId -> {} ---",dayBookId);
		this.dayBookRepository.delete(dayBookId);
	}

	@Override
	public List<DayBook> getDayBooks(Account account, Sort sort) {
		LOGGER.debug("--- get daybook by account -> {} , sort -> {} ---",account,sort);
		return this.dayBookRepository.findByAccount(account, sort);
	}

	@Override
	public List<DayBook> getDayBooks(Set<Account> accounts,
			Timestamp startDate, Timestamp endDate,Sort sort) {
		LOGGER.debug("--- get joournal page by sort -> {} , accounts -> {} ,startDate -> {} , endDate -> {} ---",sort,accounts,startDate,endDate);
		return this.dayBookRepository.findDayBooks( startDate, endDate,accounts, sort);
	}

	@Override
	public List<DayBook> getDayBooks(Timestamp startDate,
			Timestamp endDate,Sort sort) {
		LOGGER.debug("--- get day book by date , sort -> {} , startDate -> {} , endDate -> {} ---",sort,startDate,endDate);
		return this.dayBookRepository.findDayBooks(startDate, endDate, sort);
	}

	@Override
	public List<DayBook> getDayBooks(Timestamp startDate, Timestamp endDate,
			Account account, Sort sort) {
		LOGGER.debug("--- get daybook by startDate -> {} , endDate -> {} , account -> {} , sort -> {} ---",startDate,endDate,account,sort);
		return this.dayBookRepository.findDayBooks( startDate, endDate,account,sort);
	}
	
	

}


