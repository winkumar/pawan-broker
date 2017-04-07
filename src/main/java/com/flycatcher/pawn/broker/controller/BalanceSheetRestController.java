package com.flycatcher.pawn.broker.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.flycatcher.pawn.broker.exception.ResourceNotFoundException;
import com.flycatcher.pawn.broker.model.Account;
import com.flycatcher.pawn.broker.model.AccountType;
import com.flycatcher.pawn.broker.model.DayBook;
import com.flycatcher.pawn.broker.model.TransactionType;
import com.flycatcher.pawn.broker.pojo.DayBookInfo;
import com.flycatcher.pawn.broker.service.AccountService;
import com.flycatcher.pawn.broker.service.AccountTypeService;
import com.flycatcher.pawn.broker.service.DayBookService;

/**
 * 
 * @author kumar
 * @version 1.0.0
 * @since 08-Apr-2017
 * 
 */
@RestController
@RequestMapping(value=UrlPath.BALANCESHEET_PATH)
public class BalanceSheetRestController extends AbstractRestHandler{

	private static final Logger LOGGER = LoggerFactory.getLogger(JournalRestController.class);
	
	private final DayBookService dayBookService;
	private final AccountService accountService;
	private final AccountTypeService accountTypeService;
	
	@Autowired
	public BalanceSheetRestController(final DayBookService dayBookService,final AccountService accountService,final AccountTypeService accountTypeService){
		LOGGER.info("--- BalanceSheetRestController Invoked ---");
		
		this.dayBookService=dayBookService;
		this.accountService=accountService;
		this.accountTypeService=accountTypeService;
								
	}
	
	@RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "Get balace sheet by date.", notes = "To get balace sheet by date.")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-Access-Token", required = true, dataType = "string", paramType = "header")})
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
    public    @ResponseBody
    ResponseEntity<?> getAllDayBook(@ApiParam(value = "The balance sheet sort by transaction date", required = true)
    								@RequestParam(value = "sort", required = true, defaultValue = DEFAULT_PAGE_SORT) Sort.Direction sortDirection ,
    								@ApiParam(value = "The balance sheet transaction start date", required = false)
    								@RequestParam(value = "startDate", required = false) String startDateString ,
    								@ApiParam(value = "The balance sheet transaction end date", required = false)
    								@RequestParam(value = "endDate", required = false) String endDateString,
    								@ApiParam(value = "The account type", required = true)
    								@RequestParam(value = "accountType", required = true) Long accountTypeId,
    								@ApiParam(value = "The account", required = false)
    								@RequestParam(value = "account", required = false) Long accountId,
									HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("--- get balance sheet rest controller invoked , sort -> {} , startDate -> {} , endDate -> {} , accountTypeId -> {} , accountId -> {} ---",sortDirection,startDateString,endDateString,accountTypeId,accountId);
		
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
			endDate=new Date();
		}else{
		    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		    try {
		    	endDate = sdf.parse(startDateString);
			} catch (ParseException e) {
				LOGGER.debug("--- date parse exception ---");
				throw new DataFormatException("start date parse doesn't exist's ...!");
			}
		}
		
		if(startDate.after(endDate)){
			LOGGER.debug("--- start date should be same as end date or less than end date ---");
			throw new DataFormatException("start date not less than end date ...!");
		}
		
		if(accountTypeId==null){
			LOGGER.error("--- account type id is null ---");
			throw new DataFormatException("account type doesn't exist's ...!");
		}
		AccountType accountType=this.accountTypeService.getAccountType(accountTypeId);
		if(accountType==null){
			LOGGER.error("--- account type doesn't exist's , account type -> {} ---",accountTypeId);
			throw new ResourceNotFoundException("account type doesn't exist's ...!");
		}
		
		
		Set<Account> accounts=new HashSet<Account>();
		if(accountId!=null){
			Account account=this.accountService.getAccountById(accountId);
			if(account==null){
				LOGGER.error("--- account does not exist's , accountId -> {} ---",accountId);
				throw new ResourceNotFoundException("account doesn't exist's ...!");
			}
			accounts.add(account);
		}else{
			accounts=accountType.getAccounts();
		}
		
		if(accounts.isEmpty()){
			LOGGER.error("--- no more accounts exist's ---");
			throw new ResourceNotFoundException("no more accounts exist's this account type ...!");
		}
		
		Sort sort=new Sort(sortDirection,"transactionDate");
		List<DayBook> dayBooks=this.dayBookService.getJournal(sort, accounts, new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()));
		
		
		Double debitAmount=0.0,creditAmount=0.0,balance=0.0;
		List<DayBookInfo> transactions=new ArrayList<DayBookInfo>();
		if(dayBooks!=null){
			for(DayBook dayBook: dayBooks) {
				
				if(TransactionType.CREDIT.equals(dayBook.getTransactionType())){
					creditAmount+=dayBook.getTransactionAmount()!=null?dayBook.getTransactionAmount():0.0;
				}else{
					debitAmount+=dayBook.getTransactionAmount()!=null?dayBook.getTransactionAmount():0.0;
				}
				
				
			}
		}
		
		
		Map<String, Object> journals=new ConcurrentHashMap<String, Object>();
		
		journals.put("transactions", transactions);
		journals.put("credit", creditAmount);
		journals.put("debit", debitAmount);
		
		balance=debitAmount-creditAmount;
		if(balance<=0)
			journals.put("isPositive", false);
		else
			journals.put("isPositive", true);
		
		journals.put("balance", Math.abs(balance));
		
		LOGGER.info("--- daybook list return successfully ---");
		return new ResponseEntity<>(dayBooks,HttpStatus.OK);
	}


}


