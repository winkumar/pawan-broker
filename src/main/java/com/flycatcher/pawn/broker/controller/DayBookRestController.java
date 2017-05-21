package com.flycatcher.pawn.broker.controller;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.flycatcher.pawn.broker.UrlPath;
import com.flycatcher.pawn.broker.exception.DataFormatException;
import com.flycatcher.pawn.broker.exception.OtherException;
import com.flycatcher.pawn.broker.exception.ResourceNotFoundException;
import com.flycatcher.pawn.broker.model.Account;
import com.flycatcher.pawn.broker.model.AccountType;
import com.flycatcher.pawn.broker.model.DayBook;
import com.flycatcher.pawn.broker.model.TransactionType;
import com.flycatcher.pawn.broker.model.UserInfo;
import com.flycatcher.pawn.broker.pojo.DayBookInfo;
import com.flycatcher.pawn.broker.pojo.DayBookPageInfo;
import com.flycatcher.pawn.broker.service.AccountService;
import com.flycatcher.pawn.broker.service.AccountTypeService;
import com.flycatcher.pawn.broker.service.DayBookService;
import com.flycatcher.pawn.broker.service.UserInfoService;

/**
 * 
 * @author kumar
 * @version 1.0.0
 * @since 23-Mar-2017
 * 
 */
@RestController
@RequestMapping(value=UrlPath.DAYBOOK_PATH)
public class DayBookRestController extends AbstractRestHandler{

	private static final Logger LOGGER = LoggerFactory.getLogger(DayBookRestController.class);
	
	private final DayBookService dayBookService;
	private final UserInfoService userInfoService;
	private final AccountService accountService;
	private final AccountTypeService accountTypeService;
	
	@Autowired
	public DayBookRestController(final DayBookService dayBookService,final UserInfoService userInfoService,final AccountService accountService,
								 final AccountTypeService accountTypeService){
		LOGGER.info("--- DayBookRestController Invoked ---");
		
		this.dayBookService=dayBookService;
		this.userInfoService=userInfoService;
		this.accountService=accountService;
	    this.accountTypeService=accountTypeService;
	}
	
	
	
	@RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "Get page current daybook.", notes = "It will provide page of daybook.")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-Access-Token", required = true, dataType = "string", paramType = "header")})
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
    public    @ResponseBody
    ResponseEntity<?> getPageOfDayBook(
    		@ApiParam(value = "The page number (zero-based)", required = true)
			@RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
			@ApiParam(value = "The page size", required = true)
			@RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
			@ApiParam(value = "The daybook list sort", required = true)
			@RequestParam(value = "sort", required = true, defaultValue = "DESC") Sort.Direction sort ,
			@ApiParam(value = "The daybook search value", required = false)
			@RequestParam(value = "search", required = false, defaultValue="") String search,
			HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("--- get page of account rest controller invoked , page -> {} , size -> {} , sort -> {} , search -> {} ---",page,size,sort,search);
		
		final Pageable pageable = new PageRequest(page, size, new Sort(sort,"transactionDate"));
	    Page<DayBook> pageDayBook =this.dayBookService.getPageOfDayBook(search, pageable);
	    List<DayBook> dayBooks=pageDayBook.getContent();
	    List<DayBookInfo> dayBookInfos=new ArrayList<DayBookInfo>();
	    if(dayBooks!=null){
	    	dayBooks.forEach(dayBook -> {
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
	    		
	    		dayBookInfos.add(dayBookInfo);
	    	});
	    }
	    
		long totalPages=pageDayBook.getTotalPages();
		long pageNumber=pageDayBook.getNumber();
		long numberOfElements=pageDayBook.getNumberOfElements();
		long size1=pageDayBook.getSize();
		long totalElements=pageDayBook.getTotalElements();
			
		Map<String,Long> pagePropertys = new ConcurrentHashMap<>();
			
		pagePropertys.put("totalPages",totalPages);
		pagePropertys.put("pageNumber",pageNumber);
		pagePropertys.put("numberOfElements",numberOfElements);
		pagePropertys.put("size",size1);
		pagePropertys.put("totalElements",totalElements);
						
		DayBookPageInfo dayBookPageInfo=new DayBookPageInfo();
		dayBookPageInfo.setPagePropertys(pagePropertys);
		dayBookPageInfo.setDayBookInfos(dayBookInfos);	
			 

		LOGGER.info("--- daybook page return successfully ---");
		return new ResponseEntity<>(dayBookPageInfo,HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value = "/all",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "Get all daybook by start date.", notes = "It will provide of list of daybooks by specific date.")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-Access-Token", required = true, dataType = "string", paramType = "header")})
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
    public    @ResponseBody
    ResponseEntity<?> getAllDayBook(@ApiParam(value = "The daybook  sort by transaction date", required = true)
    								@RequestParam(value = "sort", required = true, defaultValue = DEFAULT_PAGE_SORT) Sort.Direction sortDirection ,
    								@ApiParam(value = "The daybook transaction start date", required = false)
    								@RequestParam(value = "startDate", required = false) String startDateString ,
    								@ApiParam(value = "The daybook transaction end date", required = false)
    								@RequestParam(value = "endDate", required = false) String endDateString,
    								@ApiParam(value = "The account type", required = false)
    								@RequestParam(value = "accountType", required = false, defaultValue="-1") Long accountTypeId,
    								@ApiParam(value = "The account", required = false)
    								@RequestParam(value = "account", required = false) Long accountId,
									HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("--- get all daybook rest controller invoked , sort -> {} ---",sortDirection);
		
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
				
		Map<String, Object> dayBooks=null;
				
		
		if(accountTypeId==-1 || accountTypeId==null){
			List<DayBook> dBooks=this.dayBookService.getDayBooks(new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()), sort);
			dayBooks=getDayBookInfo(dBooks);
			
		}else{
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
				
				if(!accountType.getAccounts().contains(account)){
					LOGGER.debug("--- account does not under this account type ---");
					throw new DataFormatException("account does not under this account type ..!");
				}
								
				accounts.add(account);
								
				}else{
					accounts=accountType.getAccounts();
				}
			
			List<DayBook> dBooks=this.dayBookService.getDayBooks(accounts,new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime()), sort);
			dayBooks=getDayBookInfo(dBooks);
		}
				
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		dayBooks.put("startDate", df.format(startDate));
		dayBooks.put("endDate",  df.format(endDate));
		
		LOGGER.debug("--- daybook info object -> {}  ---",dayBooks);
		LOGGER.info("--- daybook list return successfully ---");
		return new ResponseEntity<>(dayBooks,HttpStatus.OK);
		
	}
	
	/**
	 * get daybook object return.
	 * 
	 * @param dayBooks , list of daybook object info
	 * @return , it will return Map<String,Object> 
	 */
	private Map<String, Object> getDayBookInfo(List<DayBook> dayBooks){
		
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
			
		balance=creditAmount-debitAmount;
		if(balance<=0)
			dayBook.put("isPositive", false);
		else
			dayBook.put("isPositive", true);
		
		dayBook.put("balance", Math.abs(balance));
		
		return dayBook;
	}
	
	
	@RequestMapping(value = "/{dayBookId}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "Get daybook by id.", notes = "It will provide daybook info.")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-Access-Token", required = true, dataType = "string", paramType = "header")})
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
    public    @ResponseBody
    ResponseEntity<?> getDayBookById(@ApiParam(value = "The dayabook info by Id", required = true)
									@PathVariable("dayBookId") Long dayBookId,
									HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("--- get  daybook info rest controller invoked , dayBookId -> {} ---",dayBookId);
		
		DayBook dayBook=this.dayBookService.getDayBookById(dayBookId);
		if(dayBook==null){
			LOGGER.error("--- daybook does not exist's ---");
			throw new ResourceNotFoundException("daybook doesn't exist's ...!");
		}
		
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
	    		
	    

		LOGGER.info("--- daybook info return successfully ---");
		return new ResponseEntity<>(dayBookInfo,HttpStatus.OK);
		
	}
	
	
	@RequestMapping(value = "",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "Create new daybook .", notes = "provide valid  daybook Object.")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-Access-Token", required = true, dataType = "string", paramType = "header")})
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
    public    @ResponseBody
    ResponseEntity<?> createDayBook(@ApiParam(value = "The account info object", required = true)
									@RequestBody DayBookInfo dayBookInfo,
									HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("--- create daybook rest controller invoked , dayBookInfo -> {} ---",dayBookInfo);
		
		if(dayBookInfo.getAccountId()==null){
			LOGGER.error("--- account id does not exist's ---");
			throw new ResourceNotFoundException("account doesn't exist's ...!");
		}
				
		if(dayBookInfo.getTransactionType()==null ){
			LOGGER.error("--- transaction type doesn't exist's ---");
			throw new ResourceNotFoundException("transaction type doesn't exist's ...!");
		}
		
		if(dayBookInfo.getTransactionType().equals("DEBIT") || dayBookInfo.getTransactionType().equals("CREDIT")){
			LOGGER.debug("--- valid transaction type ---");
		}else{
			LOGGER.error("--- given transaction type is not valid , type -> {} ---",dayBookInfo.getTransactionType());
			throw new ResourceNotFoundException("transaction type is not valid ...!");
		}
		
		if(dayBookInfo.getTransactionAmount()==null || dayBookInfo.getTransactionAmount()<=0){
			LOGGER.error("--- transaction amount doesn't exist's ---");
			throw new ResourceNotFoundException("transaction amount is empty (or) doesn't exist's ...!");
		}
				
		Account account =this.accountService.getAccountById(dayBookInfo.getAccountId());
		if(account==null){
			LOGGER.error("--- given account id doesn't exist's , id -> {} ---",dayBookInfo.getAccountId());
			throw new ResourceNotFoundException("account doesn't exist's ...!");
		}
		
		if(dayBookInfo.getTransactionDate()==null){
			dayBookInfo.setTransactionDate(new Date());
		}
				
		if(dayBookInfo.getTransactionDate().after(new Date())){
			LOGGER.debug("--- transaction date not allow with next day ---");
			throw new DataFormatException("transaction date not greater than current date ...!");
		}
		
		String userName=this.userInfoService.getUserNameForAthentication();
		UserInfo createdBy=this.userInfoService.getUserInfoUserName(userName);
		
		
		DayBook dayBook=new DayBook();
		dayBook.setAccount(account);
		dayBook.setCreatedBy(createdBy);
		dayBook.setModifiedBy(createdBy);
		dayBook.setModifiedDate(new Date());
		dayBook.setCreatedDate(new Date());
		dayBook.setTransactionDate(dayBookInfo.getTransactionDate());
		dayBook.setTransactionAmount(dayBookInfo.getTransactionAmount());
		if("CREDIT".equals(dayBookInfo.getTransactionType()))
			dayBook.setTransactionType(TransactionType.CREDIT);
		else if("DEBIT".equals(dayBookInfo.getTransactionType()))
			dayBook.setTransactionType(TransactionType.DEBIT);
		dayBook.setTransactionDesc(dayBookInfo.getTransactionDesc());
		
		
		DayBook createdDayBook=this.dayBookService.createOrUpdateDayBook(dayBook);
		
		//call and update cash in hand
		try {
			checkAndUpdateCashInHand(createdDayBook,createdDayBook,createdBy,false,false);
		} catch (ParseException e) {
			e.printStackTrace();
		}		
				
		
		LOGGER.info("--- daybook created successfully ---");
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	
	
	
	
	
	@RequestMapping(value = "/{dayBookId}",
            method = RequestMethod.PUT,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "update daybook .", notes = "provide valid  daybook Object.")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-Access-Token", required = true, dataType = "string", paramType = "header")})
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
    public    @ResponseBody
    ResponseEntity<?> updateDayBook(@ApiParam(value = "The dayabook info by Id", required = true)
									@PathVariable("dayBookId") Long dayBookId,
    								@ApiParam(value = "The daybook info object", required = true)
									@RequestBody DayBookInfo dayBookInfo,
									HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("--- update daybook rest controller invoked , dayBookInfo -> {} , daybookId -> {} ---",dayBookInfo,dayBookId);
		
		if(dayBookId==null){
			LOGGER.error("--- daybook id does not exist's ---");
			throw new ResourceNotFoundException("daybook is doesn't exist's ...!");
		}
		
		DayBook dayBook=this.dayBookService.getDayBookById(dayBookId);
		if(dayBook==null){
			LOGGER.error("--- daybook doesn't exist's ---");
			throw new ResourceNotFoundException("daybook doesn't exist's ...!");
		}
		
		
		if(dayBookInfo.getAccountId()==null){
			LOGGER.error("--- account id does not exist's ---");
			throw new ResourceNotFoundException("account doesn't exist's ...!");
		}
				
		if(dayBookInfo.getTransactionType()==null ){
			LOGGER.error("--- transaction type doesn't exist's ---");
			throw new ResourceNotFoundException("transaction type doesn't exist's ...!");
		}
		
		if(dayBookInfo.getTransactionType().equals("DEBIT") || dayBookInfo.getTransactionType().equals("CREDIT")){
			LOGGER.debug("--- valid transaction type ---");
		}else{
			LOGGER.error("--- given transaction type is not valid , type -> {} ---",dayBookInfo.getTransactionType());
			throw new ResourceNotFoundException("transaction type is not valid ...!");
		}
		
		if(dayBookInfo.getTransactionAmount()==null || dayBookInfo.getTransactionAmount()<=0){
			LOGGER.error("--- transaction amount doesn't exist's ---");
			throw new ResourceNotFoundException("transaction amount is empty (or) doesn't exist's ...!");
		}
				
		Account account =this.accountService.getAccountById(dayBookInfo.getAccountId());
		if(account==null){
			LOGGER.error("--- given account id doesn't exist's , id -> {} ---",dayBookInfo.getAccountId());
			throw new ResourceNotFoundException("account doesn't exist's ...!");
		}
		
		DayBook dupDayBook=new DayBook();
		dupDayBook.setAccount(dayBook.getAccount());
		dupDayBook.setCreatedBy(dayBook.getCreatedBy());
		dupDayBook.setModifiedBy(dayBook.getModifiedBy());
		dupDayBook.setModifiedDate(dayBook.getModifiedDate());
		dupDayBook.setCreatedDate(dayBook.getCreatedDate());
		dupDayBook.setTransactionDate(dayBook.getTransactionDate());
		dupDayBook.setTransactionAmount(dayBook.getTransactionAmount());
		dupDayBook.setTransactionType(dayBook.getTransactionType());
		
		dupDayBook.setTransactionDesc(dayBook.getTransactionDesc());
		
				
		
		if(dayBookInfo.getTransactionDate()==null){
			dayBookInfo.setTransactionDate(new Date());
		}
		
		if(dayBookInfo.getTransactionDate().after(new Date())){
			LOGGER.debug("--- transaction date not allow with next day ---");
			throw new DataFormatException("transaction date not greater than current date ...!");
		}
		
		String userName=this.userInfoService.getUserNameForAthentication();
		UserInfo createdBy=this.userInfoService.getUserInfoUserName(userName);
		
		
		
		dayBook.setAccount(account);
		//dayBook.setCreatedBy(createdBy);
		dayBook.setModifiedBy(createdBy);
		dayBook.setModifiedDate(new Date());
		//dayBook.setCreatedDate(new Date());
		dayBook.setTransactionDate(dayBookInfo.getTransactionDate());
		dayBook.setTransactionAmount(dayBookInfo.getTransactionAmount());
		if("CREDIT".equals(dayBookInfo.getTransactionType()))
			dayBook.setTransactionType(TransactionType.CREDIT);
		else if("DEBIT".equals(dayBookInfo.getTransactionType()))
			dayBook.setTransactionType(TransactionType.DEBIT);
		dayBook.setTransactionDesc(dayBookInfo.getTransactionDesc());
		
		DayBook createdDayBook=this.dayBookService.createOrUpdateDayBook(dayBook);
		
		//call and update cash in hand
		try {
			checkAndUpdateCashInHand(dupDayBook,createdDayBook,createdBy,true,false);
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		
		LOGGER.info("--- daybook updated successfully ---");
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
	@RequestMapping(value = "/{dayBookId}",
            method = RequestMethod.DELETE,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "delete daybook .", notes = "provide valid daybook id.")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-Access-Token", required = true, dataType = "string", paramType = "header")})
	@PreAuthorize("hasAnyRole('ADMIN')")
    public    @ResponseBody
    ResponseEntity<?> deleteDayBook(@ApiParam(value = "The dayabook Id", required = true)
									@PathVariable("dayBookId") Long dayBookId,
    								HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("--- delete daybook rest controller invoked , daybookId -> {} ---",dayBookId);
		
		if(dayBookId==null){
			LOGGER.error("--- daybook id does not exist's ---");
			throw new ResourceNotFoundException("daybook is doesn't exist's ...!");
		}
		
		DayBook dayBook=this.dayBookService.getDayBookById(dayBookId);
		if(dayBook==null){
			LOGGER.error("--- daybook doesn't exist's ---");
			throw new ResourceNotFoundException("daybook doesn't exist's ...!");
		}
		
		if(new Long(1).equals(dayBook.getAccount().getAccountId())){
			LOGGER.error("---- account not valid to remove ---");
			throw new OtherException("you can't remove cash in hand account transactions ...!");
		}
		
		
		//call and update cash in hand
		try {
			checkAndUpdateCashInHand(dayBook,dayBook,dayBook.getCreatedBy(),false,true);
		} catch (ParseException e) {
			e.printStackTrace();
		}
				
		this.dayBookService.removeDayBookById(dayBookId);
		LOGGER.info("--- daybook removed successfully ---");
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
	
	
	/**
	 * Check and Update Cash In Hand
	 * 
	 * @param oldDayBook , oldDayBook 
	 * @param dayBook , newDayBook Object
	 * @param createdBy , createdBy Object
	 * @param isUpdateTime , find out update or create
	 * @throws ParseException
	 */
	private void checkAndUpdateCashInHand(DayBook oldDayBook,DayBook dayBook,UserInfo createdBy,Boolean isUpdateTime,Boolean isDelete) throws ParseException{
		LOGGER.info("---- Inside cash in hand checking and create or update ----");
		
		Sort sort=new Sort(Sort.DEFAULT_DIRECTION,"transactionDate");
		Account account=this.accountService.getAccountById(Long.valueOf(1));
		
		
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
		String tranDateString=simpleDateFormat.format(dayBook.getTransactionDate());
		String currentDateString=simpleDateFormat.format(new Date());
		LOGGER.info("--- transaction date -> {} , current date -> {} ---",tranDateString,currentDateString);
		Date tranDate = simpleDateFormat.parse(tranDateString);
		Date currentDate = simpleDateFormat.parse(currentDateString);
		
		
		//1. check today cash in hand exist's.
		//2. if not exist's get previous day transactions do calculation "cash in hand".
		//3. get next days cash in hand transactions and modify it.
		
		
		LOGGER.info("--- old daybook -> {}  ---",oldDayBook);
		LOGGER.info("--- new daybook -> {}  ---",dayBook);
		
		
		Double difference=0.0;
		if(isUpdateTime){
			/*if(dayBook.getTransactionAmount()<0){
				difference=oldDayBook.getTransactionAmount()+dayBook.getTransactionAmount();
			}else{*/
				if(oldDayBook.getTransactionType()==dayBook.getTransactionType()){
					difference=dayBook.getTransactionAmount()-oldDayBook.getTransactionAmount();
				}else{
					LOGGER.info("--- transaction type is not same ---");
					if(dayBook.getTransactionType()==TransactionType.CREDIT && oldDayBook.getTransactionType()==TransactionType.DEBIT){
						LOGGER.info("--- credit daybook transaction type -> {} , oldbook transaction type -> {} ---",dayBook.getTransactionType(),oldDayBook.getTransactionType());
						if(oldDayBook.getTransactionAmount().equals(dayBook.getTransactionAmount())){
							difference=oldDayBook.getTransactionAmount()+dayBook.getTransactionAmount();
						}else if(oldDayBook.getTransactionAmount()<dayBook.getTransactionAmount()){
							difference=dayBook.getTransactionAmount()+oldDayBook.getTransactionAmount();
						}else{
							difference=oldDayBook.getTransactionAmount()+dayBook.getTransactionAmount();
						}
					}else{
						LOGGER.info("--- debit daybook transaction type -> {} , oldbook transaction type -> {} ---",dayBook.getTransactionType(),oldDayBook.getTransactionType());
						LOGGER.info("--- old amount -> {} , new amount -> {} ---",oldDayBook.getTransactionAmount(),dayBook.getTransactionAmount());
						if(oldDayBook.getTransactionAmount().equals(dayBook.getTransactionAmount())){
							LOGGER.info("EQUALS");
							difference=-(dayBook.getTransactionAmount()+oldDayBook.getTransactionAmount());
						/*}else if(oldDayBook.getTransactionAmount()<dayBook.getTransactionAmount()){
							LOGGER.info("LESS THAN");
							difference=oldDayBook.getTransactionAmount()-dayBook.getTransactionAmount();*/
						}else{
							LOGGER.info("GREATER THAN");
							difference=oldDayBook.getTransactionAmount()-dayBook.getTransactionAmount();
						}
						
					}
				}
				
			//}
		}else if(isDelete){
			LOGGER.info("--- Delete Operation ---");
			if(dayBook.getTransactionType()==TransactionType.DEBIT){
				LOGGER.info("========= DEBIT DELETE ========");
				difference=dayBook.getTransactionAmount();
			}else{
				LOGGER.info("========= CREDIT DELETE ========");
				difference=-dayBook.getTransactionAmount();
			}
		}else{
			if(dayBook.getTransactionType()==TransactionType.DEBIT){
				LOGGER.info("========= DEBIT ========");
				difference=-dayBook.getTransactionAmount();
			}else{
				LOGGER.info("========= CREDIT ========");
				difference=dayBook.getTransactionAmount();
			}
		}

		LOGGER.info("--- difference amount -> {} ---",difference);
		
		List<DayBook> todayCashInHands=this.dayBookService.getDayBooks(new Timestamp(tranDate.getTime()),new Timestamp(tranDate.getTime()), account, sort);
		
		if(todayCashInHands.isEmpty()){
			LOGGER.info("--- today cash in hand is empty , create new ---");
			//boolean isAvailable=false;
			Double previousCashInHand=0.0;
			for(int i=1;i<=5;i++){
				
				Date minusDate=getMinusOrPlusDate(dayBook.getTransactionDate(),-i);
				LOGGER.info("--- find previous day transaction -> {} , minusDate -> {} ---",i ,minusDate );
				List<DayBook> previousCashInHands=this.dayBookService.getDayBooks(new Timestamp(minusDate.getTime()),new Timestamp(minusDate.getTime()), account, sort);
				if(!previousCashInHands.isEmpty()){
					//isAvailable=true;
					previousCashInHand=previousCashInHands.get(0).getTransactionAmount();
					break;
				}
			}
			
			LOGGER.info("--- previous cash in hand -> {} ,  dayBook -> {} ---",previousCashInHand,dayBook);
					
			DayBook newDayBook=new DayBook();
			newDayBook.setAccount(account);
			newDayBook.setCreatedBy(createdBy);
			newDayBook.setModifiedBy(createdBy);
			newDayBook.setModifiedDate(new Date());
			newDayBook.setCreatedDate(new Date());
			newDayBook.setTransactionDate(dayBook.getTransactionDate());
			newDayBook.setTransactionType(TransactionType.CREDIT);					
			
			if(TransactionType.CREDIT==dayBook.getTransactionType()){
				LOGGER.info("--- transaction type credit true ---");
				newDayBook.setTransactionAmount(difference+previousCashInHand);
			}else{
				LOGGER.info("--- transaction type debit true ---");
				if(previousCashInHand<dayBook.getTransactionAmount()){
					newDayBook.setTransactionAmount(previousCashInHand+difference);
				}else{
					newDayBook.setTransactionAmount(previousCashInHand+difference);
				}
					
			}
						
			newDayBook.setTransactionDesc("cash in hand system generated on "+new Date());
			
			this.dayBookService.createOrUpdateDayBook(newDayBook);
			LOGGER.info("--- new daybook  ---");
		}else{
			LOGGER.info("--- get today cash in hand ---");
			DayBook cashInHand=todayCashInHands.get(0);
			if(cashInHand!=null){
				LOGGER.info("--- today cash in hand amount is not empty ---");
				
				if(dayBook.getTransactionType()==TransactionType.CREDIT){
					cashInHand.setTransactionAmount(cashInHand.getTransactionAmount()+difference);
				}else{
					cashInHand.setTransactionAmount(cashInHand.getTransactionAmount()+difference);
				}
				
				cashInHand.setTransactionDesc("cash in hand system  update update on "+new Date());
				
				this.dayBookService.createOrUpdateDayBook(cashInHand);	
			}else{
				LOGGER.info("--- cash in hand is empty ---");
			}
		}
		
		
		boolean dateDifference=oldDayBook.getTransactionDate().before(dayBook.getTransactionDate());
		Date plusDate;
		if(dateDifference){
			plusDate=getMinusOrPlusDate(oldDayBook.getTransactionDate(), 1);
		}else{
			plusDate=getMinusOrPlusDate(dayBook.getTransactionDate(), 1);
		}
		
		
		List<DayBook> nextTransactions=this.dayBookService.getDayBooks(new Timestamp(plusDate.getTime()),new Timestamp(currentDate.getTime()), account, sort);
		LOGGER.info("--- nextTransactions -> {} ---",nextTransactions);
		for(DayBook transaction:nextTransactions){
			Double balance=0.0;
		/*	if(transaction.getTransactionAmount()<=0){
				if(difference<=0){
					if(dayBook.getTransactionType()==TransactionType.DEBIT){
						if(transaction.getTransactionAmount()<difference){
							balance=difference-transaction.getTransactionAmount();
						}else{
							balance=transaction.getTransactionAmount()-difference;
						}
					}else{
						balance=transaction.getTransactionAmount()+difference;
					}
				}else{
					if(dayBook.getTransactionType()==TransactionType.DEBIT){
					if(transaction.getTransactionAmount()<difference){
							balance=difference-transaction.getTransactionAmount();		
					}else{
						balance=transaction.getTransactionAmount()-difference;
					}
					}else{
						balance=difference+transaction.getTransactionAmount();
					}
				}
			}else{
				if(difference<=0){
					if(dayBook.getTransactionType()==TransactionType.DEBIT){
						if(transaction.getTransactionAmount()<difference){
							balance=difference-transaction.getTransactionAmount();
						}else{
							balance=transaction.getTransactionAmount()-difference;
						}
					}else{
						balance=transaction.getTransactionAmount()+difference;
					}
				}else{
					if(dayBook.getTransactionType()==TransactionType.DEBIT){
						if(transaction.getTransactionAmount()<difference){
							balance=difference-transaction.getTransactionAmount();
						}else{
							balance=transaction.getTransactionAmount()-difference;
						}
					
					}else{
							balance=difference+transaction.getTransactionAmount();
					}
				}
			}*/
			
			
			balance=difference+transaction.getTransactionAmount();
			
			
			transaction.setTransactionAmount(balance);
								
			this.dayBookService.createOrUpdateDayBook(transaction);
		}
		
		
/*        		
		if(tranDate.equals(currentDate)){
			LOGGER.info("--- transaction and today date is equal ---");
			List<DayBook> dayBooks=this.dayBookService.getDayBooks(new Timestamp(dayBook.getTransactionDate().getTime()),new Timestamp(dayBook.getTransactionDate().getTime()), account, sort);
			if(dayBooks.isEmpty()){
				LOGGER.info("--- daybook is empty ---");
				Date minusDate=getMinusOrPlusDate(dayBook.getTransactionDate(),0);
				LOGGER.info("--- date get transactions -> {} ---",minusDate);
				List<DayBook> cashInHandTransactions=this.dayBookService.getDayBooks(new Timestamp(minusDate.getTime()),new Timestamp(minusDate.getTime()),account, sort);
				LOGGER.info("--- today cash in hand transactions -> {} ---",cashInHandTransactions);
				if(cashInHandTransactions.isEmpty()){
					LOGGER.info("--- today cash in hand transactions is empty ---");
					Double bal=0.0;
					Boolean isCredit=true;
					Date minus1Date=getMinusOrPlusDate(dayBook.getTransactionDate(),-1);
					List<DayBook> lastCashHandTransactions=this.dayBookService.getDayBooks(new Timestamp(minus1Date.getTime()),new Timestamp(minus1Date.getTime()),account, sort);
					if(!lastCashHandTransactions.isEmpty()){
						bal=lastCashHandTransactions.get(0).getTransactionAmount();
						
						if(TransactionType.DEBIT.equals(lastCashHandTransactions.get(0).getTransactionType()))
							isCredit=false;
					}
										
					DayBook newDayBook=new DayBook();
					newDayBook.setAccount(account);
					newDayBook.setCreatedBy(createdBy);
					newDayBook.setModifiedBy(createdBy);
					newDayBook.setModifiedDate(new Date());
					newDayBook.setCreatedDate(new Date());
					newDayBook.setTransactionDate(dayBook.getTransactionDate());
										
					
					if(isCredit){
						newDayBook.setTransactionType(TransactionType.CREDIT);
						newDayBook.setTransactionAmount(dayBook.getTransactionAmount()+bal);
					}else{
						newDayBook.setTransactionType(TransactionType.DEBIT);
						newDayBook.setTransactionAmount(dayBook.getTransactionAmount()-bal);
					}
					
					newDayBook.setTransactionDesc("cash in hand system generated on "+new Date());
					
					this.dayBookService.createOrUpdateDayBook(newDayBook);
				}else{
					LOGGER.info("--- today  cash in hand transactions is not empty ---");
								
					DayBook dayBookObject=cashInHandTransactions.get(0);
					
					Double bal=0.0;
					Boolean isCredit=true;
					Date minus1Date=getMinusOrPlusDate(dayBook.getTransactionDate(),-1);
					List<DayBook> lastCashHandTransactions=this.dayBookService.getDayBooks(new Timestamp(minus1Date.getTime()),new Timestamp(minus1Date.getTime()),account, sort);
					if(!lastCashHandTransactions.isEmpty()){
						bal=lastCashHandTransactions.get(0).getTransactionAmount();
						
						if(TransactionType.DEBIT.equals(lastCashHandTransactions.get(0).getTransactionType()))
							isCredit=false;
					}
					
					if(isCredit){
						dayBookObject.setTransactionType(TransactionType.CREDIT);
						dayBookObject.setTransactionAmount(dayBook.getTransactionAmount()+bal);
					}else{
						dayBookObject.setTransactionType(TransactionType.DEBIT);
						dayBookObject.setTransactionAmount(dayBook.getTransactionAmount()-bal);
					}
					
					DayBook createdDayBook=this.dayBookService.createOrUpdateDayBook(dayBookObject);
					
					boolean dateDifference=oldDayBook.getTransactionDate().before(dayBook.getTransactionDate());
					Date plusDate;
					if(dateDifference){
						plusDate=getMinusOrPlusDate(oldDayBook.getTransactionDate(), 0);
					}else{
						plusDate=getMinusOrPlusDate(createdDayBook.getTransactionDate(), 0);
					}
					
					List<DayBook> nextTransactions=this.dayBookService.getDayBooks(new Timestamp(plusDate.getTime()),new Timestamp(new Date().getTime()), account, sort);
					
					boolean isCreditCHeck=false;
					if(createdDayBook.getTransactionType().equals(TransactionType.CREDIT))
						isCreditCHeck=true;
					
					for(DayBook transaction:nextTransactions){
						
						if(isCreditCHeck)
							transaction.setTransactionAmount(Math.abs(transaction.getTransactionAmount()+oldDayBook.getTransactionAmount()));
						else
							transaction.setTransactionAmount(Math.abs(transaction.getTransactionAmount()-oldDayBook.getTransactionAmount()));
						
						this.dayBookService.createOrUpdateDayBook(transaction);
					}

				}
			}else{
				LOGGER.info("--- block-1 cash in hand already exist's ---");
			}
		//}else if(dateCompare<0){
		}else if(tranDate.before(currentDate)){
			LOGGER.info("--- transaction date before today ---");
			List<DayBook> dayBooks=this.dayBookService.getDayBooks(new Timestamp(dayBook.getTransactionDate().getTime()),new Timestamp(dayBook.getTransactionDate().getTime()), account, sort);
			LOGGER.info("---- dayBooks -> {} ----",dayBooks);
			if(dayBooks.isEmpty()){
				Date minusDate=getMinusOrPlusDate(dayBook.getTransactionDate(),0);
				LOGGER.info("--- minus one day from the transaction date -> {} , minus Date -> {} ---",dayBook.getTransactionDate(),minusDate);
				List<DayBook> toDayTransactions=this.dayBookService.getDayBooks(new Timestamp(minusDate.getTime()),new Timestamp(minusDate.getTime()),account, sort);
				LOGGER.info("--- today transactions -> {} ---",toDayTransactions);
				if(toDayTransactions.isEmpty()){
					LOGGER.info("--- previous transactions is empty  ---");
					LOGGER.info("---- dayBook -> {}  ----",dayBook);
					LOGGER.info("---- newDayBook -> {} ---",oldDayBook);
					Double bal=0.0;
					Boolean isCredit=true;
					Date minus1Date=getMinusOrPlusDate(dayBook.getTransactionDate(),-1);
					List<DayBook> lastCashHandTransactions=this.dayBookService.getDayBooks(new Timestamp(minus1Date.getTime()),new Timestamp(minus1Date.getTime()),account, sort);
					if(!lastCashHandTransactions.isEmpty()){
						bal=lastCashHandTransactions.get(0).getTransactionAmount();
						
						if(TransactionType.DEBIT.equals(lastCashHandTransactions.get(0).getTransactionType()))
							isCredit=false;
					}
					
					
					DayBook newDayBook=new DayBook();
					newDayBook.setAccount(account);
					newDayBook.setCreatedBy(createdBy);
					newDayBook.setModifiedBy(createdBy);
					newDayBook.setModifiedDate(new Date());
					newDayBook.setCreatedDate(new Date());
					newDayBook.setTransactionDate(dayBook.getTransactionDate());
										
					
					if(isCredit){
						newDayBook.setTransactionType(TransactionType.CREDIT);
						newDayBook.setTransactionAmount(dayBook.getTransactionAmount()+bal);
					}else{
						newDayBook.setTransactionType(TransactionType.DEBIT);
						newDayBook.setTransactionAmount(dayBook.getTransactionAmount()-bal);
					}
					
					newDayBook.setTransactionDesc("cash in hand system generated on "+new Date());
					
					DayBook createdDayBook=this.dayBookService.createOrUpdateDayBook(newDayBook);
			
					boolean dateDifference=oldDayBook.getTransactionDate().before(dayBook.getTransactionDate());
					Date plusDate;
					if(dateDifference){
						plusDate=getMinusOrPlusDate(oldDayBook.getTransactionDate(), 0);
					}else{
						plusDate=getMinusOrPlusDate(createdDayBook.getTransactionDate(), 0);
					}
					
					List<DayBook> nextTransactions=this.dayBookService.getDayBooks(new Timestamp(plusDate.getTime()),new Timestamp(new Date().getTime()), account, sort);
					
					boolean isCreditCheck=false;
					if(createdDayBook.getTransactionType().equals(TransactionType.CREDIT))
						isCredit=true;
					
					for(DayBook transaction:nextTransactions){
						if(isCreditCheck)
							transaction.setTransactionAmount(Math.abs(transaction.getTransactionAmount()+oldDayBook.getTransactionAmount()));
						else
							transaction.setTransactionAmount(Math.abs(transaction.getTransactionAmount()-oldDayBook.getTransactionAmount()));
						
						this.dayBookService.createOrUpdateDayBook(transaction);
					}

				}else{
					LOGGER.info("--- previous transactions is not empty  ---");
										
					DayBook dayBookObject=toDayTransactions.get(0);
					
					Double bal=0.0;
					Boolean isCredit=true;
					Date minus1Date=getMinusOrPlusDate(dayBook.getTransactionDate(),-1);
					List<DayBook> lastCashHandTransactions=this.dayBookService.getDayBooks(new Timestamp(minus1Date.getTime()),new Timestamp(minus1Date.getTime()),account, sort);
					if(!lastCashHandTransactions.isEmpty()){
						bal=lastCashHandTransactions.get(0).getTransactionAmount();
						
						if(TransactionType.DEBIT.equals(lastCashHandTransactions.get(0).getTransactionType()))
							isCredit=false;
					}
					
					if(isCredit){
						dayBookObject.setTransactionType(TransactionType.CREDIT);
						dayBookObject.setTransactionAmount(dayBook.getTransactionAmount()+bal);
					}else{
						dayBookObject.setTransactionType(TransactionType.DEBIT);
						dayBookObject.setTransactionAmount(dayBook.getTransactionAmount()-bal);
					}
					
					DayBook createdDayBook=this.dayBookService.createOrUpdateDayBook(dayBookObject);

					boolean dateDifference=oldDayBook.getTransactionDate().before(dayBook.getTransactionDate());
					Date plusDate;
					if(dateDifference){
						plusDate=getMinusOrPlusDate(oldDayBook.getTransactionDate(), 0);
					}else{
						plusDate=getMinusOrPlusDate(createdDayBook.getTransactionDate(), 0);
					}
					
					List<DayBook> nextTransactions=this.dayBookService.getDayBooks(new Timestamp(plusDate.getTime()),new Timestamp(new Date().getTime()), account, sort);
					
					boolean isCreditCheck=false;
					if(createdDayBook.getTransactionType().equals(TransactionType.CREDIT))
						isCredit=true;
					
					for(DayBook transaction:nextTransactions){
						
						if(isCreditCheck)
							transaction.setTransactionAmount(Math.abs(transaction.getTransactionAmount()+oldDayBook.getTransactionAmount()));
						else
							transaction.setTransactionAmount(Math.abs(transaction.getTransactionAmount()-oldDayBook.getTransactionAmount()));
						
						
						this.dayBookService.createOrUpdateDayBook(transaction);
					}
					
					
				}
			}else{
				LOGGER.info("--- cash in hand transaction going to update ---");
				
				DayBook modifiedDayBook=dayBooks.get(0);
				LOGGER.info("--- modified daybook amount -> {} , oldDayBook amount -> {} ---",modifiedDayBook.getTransactionAmount(),oldDayBook.getTransactionAmount());
				
				Double difference=0.0;
				if(dayBook.getTransactionAmount()<0){
					difference=modifiedDayBook.getTransactionAmount()+dayBook.getTransactionAmount();
				}else{
					if(modifiedDayBook.getTransactionAmount()<0){
						difference=dayBook.getTransactionAmount()-modifiedDayBook.getTransactionAmount();
					}else{
						difference=modifiedDayBook.getTransactionAmount()-dayBook.getTransactionAmount();
					}
					//dayBook.getTransactionAmount()
				}
											
				modifiedDayBook.setTransactionAmount(difference);
				
				DayBook modifiedDifferenceDayBook=this.dayBookService.createOrUpdateDayBook(modifiedDayBook);
							
								
				String oldTranDateString=simpleDateFormat.format(dayBook.getTransactionDate());
				String newTrantDateString=simpleDateFormat.format(oldDayBook.getTransactionDate());
				LOGGER.info("--- old transaction date -> {} , new transation date -> {} ---",oldTranDateString,newTrantDateString);
				Date oldTranDate = simpleDateFormat.parse(oldTranDateString);
				Date newTranDate = simpleDateFormat.parse(newTrantDateString);
				int dateDifference=oldTranDate.compareTo(newTranDate);
				Date plusDate;
				if(dateDifference<=0){
					LOGGER.info("--- minus from old transaction date ---");
					plusDate=getMinusOrPlusDate(oldDayBook.getTransactionDate(), 0);
					
				}else{
					LOGGER.info("--- minus from new transaction date ---");
					plusDate=getMinusOrPlusDate(modifiedDayBook.getTransactionDate(), 0);
				}
							
				LOGGER.info("--- after plus date -> {} ---",plusDate);
				
				List<DayBook> nextTransactions=this.dayBookService.getDayBooks(new Timestamp(plusDate.getTime()),new Timestamp(new Date().getTime()), account, sort);
				LOGGER.info("--- nextTransactions -> {} ---",nextTransactions);
				for(DayBook transaction:nextTransactions){
					Double balance=0.0;
					if(transaction.getTransactionAmount()<=0){
						if(difference<=0){
							if(dayBook.getTransactionType().equals(TransactionType.DEBIT)){
								if(transaction.getTransactionAmount()<difference)
									balance=difference-transaction.getTransactionAmount();
								else
									balance=transaction.getTransactionAmount()-difference;
							}else{
								balance=transaction.getTransactionAmount()+difference;
							}
						}else{
							if(dayBook.getTransactionType().equals(TransactionType.DEBIT))
								if(transaction.getTransactionAmount()<difference)
									balance=difference-transaction.getTransactionAmount();
								else
									balance=transaction.getTransactionAmount()-difference;
							else
								balance=difference+transaction.getTransactionAmount();
						}
					}else{
						if(difference<=0){
							if(dayBook.getTransactionType().equals(TransactionType.DEBIT)){
								if(transaction.getTransactionAmount()<difference)
									balance=difference-transaction.getTransactionAmount();
								else
									balance=transaction.getTransactionAmount()-difference;
							}else{
								balance=transaction.getTransactionAmount()+difference;
							}
						}else{
							if(dayBook.getTransactionType().equals(TransactionType.DEBIT)){
								if(transaction.getTransactionAmount()<difference){
									balance=difference-transaction.getTransactionAmount();
								}else{
									balance=transaction.getTransactionAmount()-difference;
								}
							
							}else{
								balance=difference+transaction.getTransactionAmount();
							}
						}
					}
					
					transaction.setTransactionAmount(balance);
										
					this.dayBookService.createOrUpdateDayBook(transaction);
				}
				
			}
			
			
		}else{
			LOGGER.debug("--- transaction date is not possible to greater than today ---");
		}*/
	}
	
	
	
	
	/**
	 * 
	 * @param oldDayBook
	 * @param dayBook
	 * @param createdBy
	 * @throws ParseException
	 */
/*	private void checkAndUpdateCashInHand(DayBook oldDayBook,DayBook dayBook,UserInfo createdBy) throws ParseException{
		LOGGER.info("---- Inside cash in hand checking and create or update ----");
		int dateCompare=dayBook.getTransactionDate().compareTo(new Date());
		
		Sort sort=new Sort(Sort.DEFAULT_DIRECTION,"transactionDate");
		Account account=this.accountService.getAccountById(Long.valueOf(1));
		LOGGER.info("---- date compare result is -> {} ---",dateCompare);
		
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
		String tranDateString=simpleDateFormat.format(dayBook.getTransactionDate());
		String currentDateString=simpleDateFormat.format(new Date());
		LOGGER.info("--- transaction date -> {}  ---",tranDateString);
		Date tranDate = simpleDateFormat.parse(tranDateString);
		Date currentDate = simpleDateFormat.parse(currentDateString);
        		
		//if(dateCompare==0){
		if(tranDate.equals(currentDate)){
			LOGGER.info("--- transaction and today date is equal ---");
			List<DayBook> dayBooks=this.dayBookService.getDayBooks(new Timestamp(dayBook.getTransactionDate().getTime()),new Timestamp(dayBook.getTransactionDate().getTime()), account, sort);
			if(dayBooks.isEmpty()){
				LOGGER.info("--- daybook is empty ---");
				Date minusDate=getMinusOrPlusDate(dayBook.getTransactionDate(),0);
				LOGGER.info("--- date -1 with value -> {} ---",minusDate);
				List<DayBook> previousDayTransactions=this.dayBookService.getDayBooks(new Timestamp(minusDate.getTime()),new Timestamp(minusDate.getTime()), sort);
				if(previousDayTransactions.isEmpty()){
					LOGGER.info("--- privious day transactions is empty ---");
					DayBook newDayBook=new DayBook();
					newDayBook.setAccount(account);
					newDayBook.setCreatedBy(createdBy);
					newDayBook.setModifiedBy(createdBy);
					newDayBook.setModifiedDate(new Date());
					newDayBook.setCreatedDate(new Date());
					newDayBook.setTransactionDate(dayBook.getTransactionDate());
					newDayBook.setTransactionAmount(Double.valueOf(0));
					
					newDayBook.setTransactionType(TransactionType.CREDIT);
					
					newDayBook.setTransactionDesc("cash in hand system generated on "+new Date());
					
					this.dayBookService.createOrUpdateDayBook(newDayBook);
				}else{
					LOGGER.info("--- privious day transactions is not empty ---");
					Double debitAmount=0.0,creditAmount=0.0,balance=0.0;
					
					for(DayBook transaction: previousDayTransactions){
				
						if(TransactionType.CREDIT.equals(transaction.getTransactionType())){
								creditAmount+=transaction.getTransactionAmount()!=null?transaction.getTransactionAmount():0.0;
						}else{
								debitAmount+=transaction.getTransactionAmount()!=null?transaction.getTransactionAmount():0.0;
						}
					}
					
					
					DayBook newDayBook=new DayBook();
					
					balance=creditAmount-debitAmount;
					if(balance<0)
						newDayBook.setTransactionType(TransactionType.DEBIT);
					else
						newDayBook.setTransactionType(TransactionType.CREDIT);
							
					LOGGER.info("--- privious day transactions , debitAmount -> {} , creditAmount -> {} , balance -> {} ---",debitAmount,creditAmount,balance);
					
					newDayBook.setAccount(account);
					newDayBook.setCreatedBy(createdBy);
					newDayBook.setModifiedBy(createdBy);
					newDayBook.setModifiedDate(new Date());
					newDayBook.setCreatedDate(new Date());
					newDayBook.setTransactionDate(dayBook.getTransactionDate());
					newDayBook.setTransactionAmount(balance);
															
					newDayBook.setTransactionDesc("cash in hand system generated on "+new Date());
					
					DayBook createdDayBook=this.dayBookService.createOrUpdateDayBook(newDayBook);
					LOGGER.info("--- created day book object -> {} ---",createdDayBook);					
					boolean dateDifference=oldDayBook.getTransactionDate().before(dayBook.getTransactionDate());
					Date plusDate;
					if(dateDifference){
						plusDate=getMinusOrPlusDate(oldDayBook.getTransactionDate(), 0);
					}else{
						plusDate=getMinusOrPlusDate(createdDayBook.getTransactionDate(), 0);
					}
					
					List<DayBook> nextTransactions=this.dayBookService.getDayBooks(new Timestamp(plusDate.getTime()),new Timestamp(new Date().getTime()), account, sort);
					
					boolean isCredit=false;
					if(createdDayBook.getTransactionType().equals(TransactionType.CREDIT))
						isCredit=true;
					
					for(DayBook transaction:nextTransactions){
						
						if(isCredit)
							transaction.setTransactionAmount(Math.abs(transaction.getTransactionAmount()+oldDayBook.getTransactionAmount()));
						else
							transaction.setTransactionAmount(Math.abs(transaction.getTransactionAmount()-oldDayBook.getTransactionAmount()));
						
						this.dayBookService.createOrUpdateDayBook(transaction);
					}

				}
			}else{
				LOGGER.info("--- block-1 cash in hand already exist's ---");
			}
		//}else if(dateCompare<0){
		}else if(tranDate.before(currentDate)){
			LOGGER.info("--- transaction date before today ---");
			List<DayBook> dayBooks=this.dayBookService.getDayBooks(new Timestamp(dayBook.getTransactionDate().getTime()),new Timestamp(dayBook.getTransactionDate().getTime()), account, sort);
			LOGGER.info("---- dayBooks -> {} ----",dayBooks);
			if(dayBooks.isEmpty()){
				Date minusDate=getMinusOrPlusDate(dayBook.getTransactionDate(),0);
				LOGGER.info("--- minus one day from the transaction date -> {} , minus Date -> {} ---",dayBook.getTransactionDate(),minusDate);
				List<DayBook> previousDayTransactions=this.dayBookService.getDayBooks(new Timestamp(minusDate.getTime()),new Timestamp(minusDate.getTime()),account, sort);
				LOGGER.info("--- Pervious transactions -> {} ---",previousDayTransactions);
				if(previousDayTransactions.isEmpty()){
					LOGGER.info("--- previous transactions is empty  ---");
					DayBook newDayBook=new DayBook();
					newDayBook.setAccount(account);
					newDayBook.setCreatedBy(createdBy);
					newDayBook.setModifiedBy(createdBy);
					newDayBook.setModifiedDate(new Date());
					newDayBook.setCreatedDate(new Date());
					newDayBook.setTransactionDate(dayBook.getTransactionDate());
					newDayBook.setTransactionAmount(Double.valueOf(0));
					
					newDayBook.setTransactionType(TransactionType.CREDIT);
					
					newDayBook.setTransactionDesc("cash in hand system generated on "+new Date());
					
					DayBook createdDayBook=this.dayBookService.createOrUpdateDayBook(newDayBook);
					
					boolean dateDifference=oldDayBook.getTransactionDate().before(dayBook.getTransactionDate());
					Date plusDate;
					if(dateDifference){
						plusDate=getMinusOrPlusDate(oldDayBook.getTransactionDate(), 0);
					}else{
						plusDate=getMinusOrPlusDate(createdDayBook.getTransactionDate(), 0);
					}
					
					List<DayBook> nextTransactions=this.dayBookService.getDayBooks(new Timestamp(plusDate.getTime()),new Timestamp(new Date().getTime()), account, sort);
					
					boolean isCredit=false;
					if(createdDayBook.getTransactionType().equals(TransactionType.CREDIT))
						isCredit=true;
					
					for(DayBook transaction:nextTransactions){
						if(isCredit)
							transaction.setTransactionAmount(Math.abs(transaction.getTransactionAmount()+oldDayBook.getTransactionAmount()));
						else
							transaction.setTransactionAmount(Math.abs(transaction.getTransactionAmount()-oldDayBook.getTransactionAmount()));
						
						this.dayBookService.createOrUpdateDayBook(transaction);
					}

				}else{
					LOGGER.info("--- previous transactions is not empty  ---");
					Double debitAmount=0.0,creditAmount=0.0,balance=0.0;
					
					for(DayBook transaction: previousDayTransactions){
						LOGGER.info("--- hello ---");
						if(TransactionType.CREDIT.equals(transaction.getTransactionType())){
							LOGGER.info("--- true ---");
								creditAmount+=transaction.getTransactionAmount()!=null?transaction.getTransactionAmount():0.0;
						}else{
							LOGGER.info("--- false ---");
								debitAmount+=transaction.getTransactionAmount()!=null?transaction.getTransactionAmount():0.0;
						}
					}
					
					LOGGER.info("--- credit amount -> {} , debit amount -> {} , balance -> {} ---",creditAmount,debitAmount,balance);
					DayBook newDayBook=new DayBook();
					
					balance=creditAmount-debitAmount;
					if(balance<0)
						newDayBook.setTransactionType(TransactionType.DEBIT);
					else
						newDayBook.setTransactionType(TransactionType.CREDIT);
							
					LOGGER.info("--- Balance Amount -> {} ---",balance);
					newDayBook.setAccount(account);
					newDayBook.setCreatedBy(createdBy);
					newDayBook.setModifiedBy(createdBy);
					newDayBook.setModifiedDate(new Date());
					newDayBook.setCreatedDate(new Date());
					newDayBook.setTransactionDate(dayBook.getTransactionDate());
					newDayBook.setTransactionAmount(Math.abs(balance));
															
					newDayBook.setTransactionDesc("cash in hand system generated on "+new Date());
					
					DayBook createdDayBook=this.dayBookService.createOrUpdateDayBook(newDayBook);
					
					boolean dateDifference=oldDayBook.getTransactionDate().before(dayBook.getTransactionDate());
					Date plusDate;
					if(dateDifference){
						plusDate=getMinusOrPlusDate(oldDayBook.getTransactionDate(), 0);
					}else{
						plusDate=getMinusOrPlusDate(createdDayBook.getTransactionDate(), 0);
					}
					
					List<DayBook> nextTransactions=this.dayBookService.getDayBooks(new Timestamp(plusDate.getTime()),new Timestamp(new Date().getTime()), account, sort);
					
					boolean isCredit=false;
					if(createdDayBook.getTransactionType().equals(TransactionType.CREDIT))
						isCredit=true;
					
					for(DayBook transaction:nextTransactions){
						
						if(isCredit)
							transaction.setTransactionAmount(Math.abs(transaction.getTransactionAmount()+oldDayBook.getTransactionAmount()));
						else
							transaction.setTransactionAmount(Math.abs(transaction.getTransactionAmount()-oldDayBook.getTransactionAmount()));
						
						
						this.dayBookService.createOrUpdateDayBook(transaction);
					}
					
					
				}
			}else{
				LOGGER.info("--- cash in hand transaction going to update ---");
				
				DayBook modifiedDayBook=dayBooks.get(0);
				LOGGER.info("--- modified daybook amount -> {} , oldDayBook amount -> {} ---",modifiedDayBook.getTransactionAmount(),oldDayBook.getTransactionAmount());
				
				Double difference=0.0;
				if(dayBook.getTransactionAmount()<=0){
					difference=modifiedDayBook.getTransactionAmount()+dayBook.getTransactionAmount();
				}else{
					if(modifiedDayBook.getTransactionAmount()<=0){
						difference=dayBook.getTransactionAmount()-modifiedDayBook.getTransactionAmount();
					}else{
						difference=modifiedDayBook.getTransactionAmount()-dayBook.getTransactionAmount();
					}
					//dayBook.getTransactionAmount()
				}
											
				modifiedDayBook.setTransactionAmount(difference);
				
				DayBook modifiedDifferenceDayBook=this.dayBookService.createOrUpdateDayBook(modifiedDayBook);
							
								
				String oldTranDateString=simpleDateFormat.format(dayBook.getTransactionDate());
				String newTrantDateString=simpleDateFormat.format(oldDayBook.getTransactionDate());
				LOGGER.info("--- old transaction date -> {} , new transation date -> {} ---",oldTranDateString,newTrantDateString);
				Date oldTranDate = simpleDateFormat.parse(oldTranDateString);
				Date newTranDate = simpleDateFormat.parse(newTrantDateString);
				int dateDifference=oldTranDate.compareTo(newTranDate);
				Date plusDate;
				if(dateDifference<=0){
					LOGGER.info("--- minus from old transaction date ---");
					plusDate=getMinusOrPlusDate(oldDayBook.getTransactionDate(), 0);
					
				}else{
					LOGGER.info("--- minus from new transaction date ---");
					plusDate=getMinusOrPlusDate(modifiedDayBook.getTransactionDate(), 0);
				}
							
				LOGGER.info("--- after plus date -> {} ---",plusDate);
				
				List<DayBook> nextTransactions=this.dayBookService.getDayBooks(new Timestamp(plusDate.getTime()),new Timestamp(new Date().getTime()), account, sort);
				LOGGER.info("--- nextTransactions -> {} ---",nextTransactions);
				for(DayBook transaction:nextTransactions){
					Double balance=0.0;
					if(transaction.getTransactionAmount()<=0){
						if(difference<=0){
							if(dayBook.getTransactionType().equals(TransactionType.DEBIT)){
								if(transaction.getTransactionAmount()<difference)
									balance=difference-transaction.getTransactionAmount();
								else
									balance=transaction.getTransactionAmount()-difference;
							}else{
								balance=transaction.getTransactionAmount()+difference;
							}
						}else{
							if(dayBook.getTransactionType().equals(TransactionType.DEBIT))
								if(transaction.getTransactionAmount()<difference)
									balance=difference-transaction.getTransactionAmount();
								else
									balance=transaction.getTransactionAmount()-difference;
							else
								balance=difference+transaction.getTransactionAmount();
						}
					}else{
						if(difference<=0){
							if(dayBook.getTransactionType().equals(TransactionType.DEBIT)){
								if(transaction.getTransactionAmount()<difference)
									balance=difference-transaction.getTransactionAmount();
								else
									balance=transaction.getTransactionAmount()-difference;
							}else{
								balance=transaction.getTransactionAmount()+difference;
							}
						}else{
							if(dayBook.getTransactionType().equals(TransactionType.DEBIT)){
								if(transaction.getTransactionAmount()<difference){
									balance=difference-transaction.getTransactionAmount();
								}else{
									balance=transaction.getTransactionAmount()-difference;
								}
							
							}else{
								balance=difference+transaction.getTransactionAmount();
							}
						}
					}
					
					transaction.setTransactionAmount(balance);
										
					this.dayBookService.createOrUpdateDayBook(transaction);
				}
				
			}
			
			
		}else{
			LOGGER.debug("--- transaction date is not possible to greater than today ---");
		}
	}*/
	
	
	private Date getMinusOrPlusDate(Date originalDate,int days){
		Calendar cal = Calendar.getInstance();
		cal.setTime(originalDate);
		cal.add(Calendar.DATE, days);
		Date minusDay = cal.getTime();
		return minusDay;
	}
	
	
}


