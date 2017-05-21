package com.flycatcher.pawn.broker.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.flycatcher.pawn.broker.pojo.CommonInfo;
import com.flycatcher.pawn.broker.pojo.DayBookInfo;
import com.flycatcher.pawn.broker.service.DayBookService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author kumar
 * @version 1.0.0
 * @since 07-Apr-2017
 * 
 */
@RestController
@RequestMapping(value=UrlPath.JOURNAL_PATH)
public class JournalRestController extends AbstractRestHandler{

	private static final Logger LOGGER = LoggerFactory.getLogger(JournalRestController.class);
	
	private final DayBookService dayBookService;
	
	@Autowired
	public JournalRestController(final DayBookService dayBookService){
		LOGGER.info("--- JournalRestController Invoked ---");
		
		this.dayBookService=dayBookService;
								
	}
	
	@RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "Get journal entries by type with date.", notes = "To get journal entries by type with date.")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-Access-Token", required = true, dataType = "string", paramType = "header")})
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
    public    @ResponseBody
    ResponseEntity<?> getAllDayBook(@ApiParam(value = "The journal  sort by transaction date", required = true)
    								@RequestParam(value = "sort", required = true, defaultValue = DEFAULT_PAGE_SORT) Sort.Direction sortDirection ,
    								@ApiParam(value = "The journal transaction start date", required = false)
    								@RequestParam(value = "startDate", required = false) String startDateString ,
    								@ApiParam(value = "The journal transaction end date", required = false)
    								@RequestParam(value = "endDate", required = false) String endDateString,
    								@ApiParam(value = "The account type", required = false)
    								@RequestParam(value = "accountType", required = false, defaultValue="-1") Long accountTypeId,
    								@ApiParam(value = "The account", required = false)
    								@RequestParam(value = "account", required = false) Long accountId,
									HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("--- get journal rest controller invoked , sort -> {} , startDate -> {} , endDate -> {} , accountTypeId -> {} , accountId -> {} ---",sortDirection,startDateString,endDateString,accountTypeId,accountId);
		
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
		
		Map<String,List<DayBookInfo>> individualAccountList = new HashMap<String,List<DayBookInfo>>();
				
		Sort sort=new Sort(sortDirection,"transactionDate");
		List<DayBook> dayBooks=this.dayBookService.getDayBooks(new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()),sort);
		
		if(dayBooks!=null){
			for(DayBook dayBook: dayBooks) {
				List<DayBookInfo> transactions=new ArrayList<DayBookInfo>();
				if(dayBook.getAccount().getAccountId() != 1){
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

				    transactions.add(dayBookInfo);
					
					if(individualAccountList.size() > 0){
						if(individualAccountList.containsKey(dayBookInfo.getAccountName())){
							List<DayBookInfo> repeatedList = individualAccountList.get(dayBookInfo.getAccountName());
							repeatedList.addAll(transactions);
							individualAccountList.put(dayBookInfo.getAccountName(), repeatedList);
						}else{
							individualAccountList.put(dayBookInfo.getAccountName(), transactions);
						}
					}else{
						individualAccountList.put(dayBookInfo.getAccountName(), transactions);
					}
				}
			}
		}
		
		Map<String,Object> accountList = new HashMap<String,Object>();
		int count = 0 ;
		for(Map.Entry<String,List<DayBookInfo>> accountMap : individualAccountList.entrySet() ){
			
			Double debitAmount=0.0,creditAmount=0.0,balance=0.0	;		
			List<DayBookInfo> dayBookInfoList = accountMap.getValue();
			
			if(dayBookInfoList!=null && !dayBookInfoList.isEmpty()){
				
				CommonInfo commonInfo = new CommonInfo();
				
				for(DayBookInfo dInfo : dayBookInfoList){
					if(TransactionType.CREDIT.toString().equalsIgnoreCase(dInfo.getTransactionType())){
						creditAmount+=dInfo.getTransactionAmount()!=null?dInfo.getTransactionAmount():0.0;
					}else{
						debitAmount+=dInfo.getTransactionAmount()!=null?dInfo.getTransactionAmount():0.0;
					}
					balance = creditAmount > debitAmount ? creditAmount - debitAmount : debitAmount - creditAmount ;
					commonInfo.setCreditTotal(creditAmount);
					commonInfo.setDebitTotal(debitAmount);
					commonInfo.setBalance(balance);
				}
				
				dayBookInfoList.get(0).setCommonInfo(commonInfo);
				accountList.put(String.valueOf(count++), dayBookInfoList);
			}
			
		}
		
		
		LOGGER.info("--- daybook list return successfully ---");
		return new ResponseEntity<>(accountList,HttpStatus.OK);
	}
}
	
//	private Map<String,Object> journal(final Sort.Direction sortDirection,final AccountType accountType,final Set<Account> accounts,final Date startDate,final Date endDate){
//				
//		/*if(accounts.isEmpty()){
//			LOGGER.error("--- no more accounts exist's ---");
//			throw new ResourceNotFoundException("no more accounts exist's this account type ...!");
//		}*/
//		
//		Sort sort=new Sort(sortDirection,"transactionDate");
//		List<DayBook> dayBooks=this.dayBookService.getDayBooks( accounts, new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()),sort);
//		
//		
//		Double debitAmount=0.0,creditAmount=0.0,balance=0.0;
//		List<DayBookInfo> transactions=new ArrayList<DayBookInfo>();
//		if(dayBooks!=null){
//			for(DayBook dayBook: dayBooks) {
//				if(dayBook.getAccount().getAccountId() != 1){
//					DayBookInfo dayBookInfo=new DayBookInfo();
//		    		
//				    dayBookInfo.setAccountId(dayBook.getAccount()!=null?dayBook.getAccount().getAccountId():null);
//				    dayBookInfo.setAccountName(dayBook.getAccount()!=null?dayBook.getAccount().getFirstName():null);
//				    dayBookInfo.setAccountNumber(dayBook.getAccount()!=null?dayBook.getAccount().getAccountNumber():null);
//				    dayBookInfo.setAccountTypeId(dayBook.getAccount()!=null?dayBook.getAccount().getAccountType().getId():null);
//				    dayBookInfo.setCreatedBy(dayBook.getCreatedBy()!=null?dayBook.getCreatedBy().getFirstName():null);
//				    dayBookInfo.setCreatedDate(dayBook.getCreatedDate());
//				    dayBookInfo.setDayBookId(dayBook.getDayBookId());
//				    dayBookInfo.setModifiedBy(dayBook.getModifiedBy()!=null?dayBook.getModifiedBy().getFirstName():null);
//				    dayBookInfo.setModifiedDate(dayBook.getModifiedDate());
//				    dayBookInfo.setTransactionAmount(dayBook.getTransactionAmount());
//				    dayBookInfo.setTransactionDate(dayBook.getTransactionDate());
//				    dayBookInfo.setTransactionDesc(dayBook.getTransactionDesc());
//				    dayBookInfo.setTransactionType(dayBook.getTransactionType()!=null?dayBook.getTransactionType().name():null);
//				    				
//					
//					if(TransactionType.CREDIT.equals(dayBook.getTransactionType())){
//						creditAmount+=dayBook.getTransactionAmount()!=null?dayBook.getTransactionAmount():0.0;
//					}else{
//						debitAmount+=dayBook.getTransactionAmount()!=null?dayBook.getTransactionAmount():0.0;
//					}
//					
//					transactions.add(dayBookInfo);
//				}
//			}
//		}
//		
//		
//		Map<String, Object> journalAccount=new ConcurrentHashMap<String, Object>();
//		
//		journalAccount.put("transactions", transactions);
//		journalAccount.put("credit", creditAmount);
//		journalAccount.put("debit", debitAmount);
//		journalAccount.put("accountType", accountType.getAccountType());
//		
//		balance=creditAmount-debitAmount;
//		if(balance<=0)
//			journalAccount.put("isPositive", false);
//		else
//			journalAccount.put("isPositive", true);
//		
//		journalAccount.put("balance", Math.abs(balance));
//		
//		
//		return journalAccount;
//	}
//}


