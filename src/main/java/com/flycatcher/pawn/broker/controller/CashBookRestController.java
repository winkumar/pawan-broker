package com.flycatcher.pawn.broker.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.flycatcher.pawn.broker.UrlPath;
import com.flycatcher.pawn.broker.exception.DataFormatException;
import com.flycatcher.pawn.broker.model.DayBook;
import com.flycatcher.pawn.broker.model.TransactionType;
import com.flycatcher.pawn.broker.pojo.DayBookInfo;
import com.flycatcher.pawn.broker.service.DayBookService;


/**
 * 
 * @author kumar
 * @version 1.0.0
 * @since 25-Apr-2017
 * 
 */
@RestController
@RequestMapping(value=UrlPath.CASHBOOK_PATH)
public class CashBookRestController extends AbstractRestHandler{

	private static final Logger LOGGER = LoggerFactory.getLogger(DayBookRestController.class);
	
	private final DayBookService dayBookService;

	
	@Autowired
	public CashBookRestController(final DayBookService dayBookService){
		LOGGER.info("--- CashBookRestController Invoked ---");
		
		this.dayBookService=dayBookService;
		
	}
	
	
	
	
	@RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "Get cashbook by start date.", notes = "It will provide of list of cashbook by specific date.")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-Access-Token", required = true, dataType = "string", paramType = "header")})
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
    public    @ResponseBody
    ResponseEntity<?> getCashBook(@ApiParam(value = "The cashbook  sort by transaction date", required = true)
									@RequestParam(value = "sort", required = true, defaultValue = DEFAULT_PAGE_SORT) Sort.Direction sortDirection ,
									@ApiParam(value = "The cashboook transaction start date", required = false)
    								@RequestParam(value = "startDate", required = false) String startDateString ,
    								@ApiParam(value = "The cashboook transaction end date", required = false)
    								@RequestParam(value = "endDate", required = false) String endDateString,
    								HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("--- get cashbook rest controller invoked , startDate -> {} , endDate -> {} , sort -> {} ---",startDateString,endDateString,sortDirection);
		
		Sort sort=new Sort(Sort.Direction.DESC,"transactionDate");
		
		Date startDate,endDate;
		
		if(startDateString==null){
			LOGGER.debug("--- start date is null so start date become current date ---");
			startDate=new Date();
		}else{
		    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		    try {
		    	startDate = sdf.parse(startDateString);
			} catch (ParseException e) {
				LOGGER.debug("--- date parse exception ---");
				throw new DataFormatException("start date parse doesn't exist's ...!");
			}
		}
		
		if(endDateString==null){
			LOGGER.debug("--- end date is null so end date become current date ---");
			endDate=startDate;
		}else{
		    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		    try {
		    	endDate = sdf.parse(endDateString);
			} catch (ParseException e) {
				LOGGER.debug("--- date parse exception ---");
				throw new DataFormatException("start date parse doesn't exist's ...!");
			}
		}
		
		if(startDate.after(endDate)){
			LOGGER.debug("--- start date should be same as end date or less than end date ---");
			throw new DataFormatException("start date not less than end date ...!");
		}
				
		Map<String, Object> cashBook=new ConcurrentHashMap<String, Object>();
		
		List<Map<String,Object>> cashBooks=new ArrayList<Map<String,Object>>();
		
		Calendar cal = Calendar.getInstance(); 
		while (startDate.compareTo(endDate) <= 0) {
	        
			List<DayBook> dBooks=this.dayBookService.getDayBooks(new Timestamp(startDate.getTime()), new Timestamp(startDate.getTime()), sort);
						
			cashBooks.add(getDayBookInfo(dBooks,startDate));
			
			//increment date
			cal.setTime(startDate);
			cal.add(Calendar.DATE, 1);
			startDate = cal.getTime();
			
	    }
		
			
		cashBook.put("startDate", startDateString);
		cashBook.put("endDate",  endDateString);
		
		cashBook.put("cashBooks", cashBooks);
		
		LOGGER.debug("--- cashbook info object -> {}  ---",cashBook);
		LOGGER.info("--- cashbook list return successfully ---");
		return new ResponseEntity<>(cashBook,HttpStatus.OK);
		
	}
	
	/**
	 * 
	 * @param dayBooks
	 * @return
	 */
	private Map<String, Object> getDayBookInfo(List<DayBook> dayBooks,Date transactionDate){
		
		Double debitAmount=0.0,creditAmount=0.0,balance=0.0;
		List<DayBookInfo> transactions=new ArrayList<DayBookInfo>();
		if(dayBooks!=null){
			for(DayBook dayBook: dayBooks) {
				
				DayBookInfo dayBookInfo=new DayBookInfo();
	    		
			    dayBookInfo.setAccountId(dayBook.getAccount()!=null?dayBook.getAccount().getAccountId():null);
			    dayBookInfo.setAccountName(dayBook.getAccount()!=null?dayBook.getAccount().getFirstName():null);
			    dayBookInfo.setAccountNumber(dayBook.getAccount()!=null?dayBook.getAccount().getAccountNumber():null);
			    dayBookInfo.setAccountTypeId(dayBook.getAccount()!=null?dayBook.getAccount().getAccountType().getId():null);
			    dayBookInfo.setCreatedBy(dayBook.getCreatedBy()!=null?dayBook.getCreatedBy().getFirstName():null);
			    dayBookInfo.setCreatedDate(dayBook.getCreatedDate());
			    dayBookInfo.setDayBookId(dayBook.getDayBookId());
			    dayBookInfo.setModifiedBy(dayBook.getModifiedBy()!=null?dayBook.getModifiedBy().getFirstName():null);
			    dayBookInfo.setModifiedDate(dayBook.getModifiedDate());
			    dayBookInfo.setTransactionAmount(dayBook.getTransactionAmount());
			    dayBookInfo.setTransactionDate(dayBook.getTransactionDate());
			    dayBookInfo.setTransactionDesc(dayBook.getTransactionDesc());
			    dayBookInfo.setTransactionType(dayBook.getTransactionType()!=null?dayBook.getTransactionType().name():null);
			    				
				
				if(TransactionType.CREDIT.equals(dayBook.getTransactionType())){
					creditAmount+=dayBook.getTransactionAmount()!=null?dayBook.getTransactionAmount():0.0;
				}else{
					debitAmount+=dayBook.getTransactionAmount()!=null?dayBook.getTransactionAmount():0.0;
				}
				
				transactions.add(dayBookInfo);
			}
		}
				
		Map<String, Object> dayBook=new ConcurrentHashMap<String, Object>();
		
		dayBook.put("transactions", transactions);
		dayBook.put("credit", creditAmount);
		dayBook.put("debit", debitAmount);
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		dayBook.put("transactionDate",df.format(transactionDate));	
		balance=creditAmount-debitAmount;
		if(balance<=0)
			dayBook.put("isPositive", false);
		else
			dayBook.put("isPositive", true);
		
		dayBook.put("balance", Math.abs(balance));
		
		return dayBook;
	}

}


