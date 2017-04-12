package com.flycatcher.pawn.broker.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import com.flycatcher.pawn.broker.exception.ResourceAlreadyExistException;
import com.flycatcher.pawn.broker.exception.ResourceNotFoundException;
import com.flycatcher.pawn.broker.model.Account;
import com.flycatcher.pawn.broker.model.AccountType;
import com.flycatcher.pawn.broker.model.DayBook;
import com.flycatcher.pawn.broker.model.UserInfo;
import com.flycatcher.pawn.broker.pojo.AccountInfo;
import com.flycatcher.pawn.broker.pojo.AccountPageInfo;
import com.flycatcher.pawn.broker.pojo.DayBookInfo;
import com.flycatcher.pawn.broker.service.AccountService;
import com.flycatcher.pawn.broker.service.AccountTypeService;
import com.flycatcher.pawn.broker.service.DayBookService;
import com.flycatcher.pawn.broker.service.UserInfoService;



/**
 * @author kumar
 *
 */

@RestController
@RequestMapping(value=UrlPath.ACCOUNT_PATH)
public class AccountRestController extends AbstractRestHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountRestController.class);
	
	private final AccountService accountService;
	private final DayBookService dayBookService;
	private final UserInfoService userInfoService;
	private final AccountTypeService accountTypeService;
	
	@Autowired
	public AccountRestController(final AccountService accountService,final UserInfoService userInfoService,
								final DayBookService dayBookService,final AccountTypeService accountTypeService){
		LOGGER.info("--- AccountRestController Invoked ---");
		
		this.accountService=accountService;
		this.userInfoService=userInfoService;
		this.dayBookService=dayBookService;
		this.accountTypeService=accountTypeService;
								
	}
	
	/*
	 * get page account 
	 */
	
	@RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "Get Page current account.", notes = "It will provide page of account.")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-Access-Token", required = true, dataType = "string", paramType = "header")})
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
    public    @ResponseBody
    ResponseEntity<?> getPageOfAccount(
    		@ApiParam(value = "The page number (zero-based)", required = true)
			@RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
			@ApiParam(value = "The page size", required = true)
			@RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
			@ApiParam(value = "The account list sort", required = true)
			@RequestParam(value = "sort", required = true, defaultValue = DEFAULT_PAGE_SORT) Sort.Direction sort ,
			@ApiParam(value = "The account search value", required = false)
			@RequestParam(value = "search", required = false, defaultValue="") String search,
			HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("--- get page of account rest controller invoked , page -> {} , size -> {} , sort -> {} , search -> {} ---",page,size,sort,search);
		
		final Pageable pageable = new PageRequest(page, size, new Sort(sort,"accountNumber","firstName","lastName","area"));
	    			
		final Page<Account> accountPage=this.accountService.getPageOfAccount(search,pageable);
		
		List<Account> accounts=accountPage.getContent();
		
		List<AccountInfo> accountInfos=new ArrayList<AccountInfo>();
		if(accounts!=null){
		accounts.forEach(account ->{
			AccountInfo accountInfo=new AccountInfo();
			
			accountInfo.setAccountId(account.getAccountId());
			accountInfo.setAccountNumber(account.getAccountNumber());
			accountInfo.setArea(account.getArea());
			accountInfo.setCity(account.getCity());
			accountInfo.setCreatedBy(account.getCreatedBy()!=null?account.getCreatedBy().getFirstName()+" "+account.getCreatedBy().getFirstName():null);
			accountInfo.setCreatedDate(account.getCreatedDate());
			accountInfo.setCurrentAddress(account.getCurrentAddress());
			accountInfo.setFatherName(account.getFatherName());
			accountInfo.setLastName(account.getLastName());
			accountInfo.setFirstName(account.getFirstName());
			accountInfo.setLastName(account.getLastName());
			accountInfo.setModifiedBy(account.getModifiedBy()!=null?account.getModifiedBy().getFirstName()+" "+account.getModifiedBy().getLastName():null);
			accountInfo.setPinCode(account.getPinCode());
			accountInfo.setPresentAddress(account.getPresentAddress());
			accountInfo.setState(account.getState());
			
			if(account.getAccountType()!=null)
				accountInfo.setAccountTypeId(account.getAccountType().getId());
			
			
			accountInfos.add(accountInfo);
		});
		}
		long totalPages=accountPage.getTotalPages();
		long pageNumber=accountPage.getNumber();
		long numberOfElements=accountPage.getNumberOfElements();
		long size1=accountPage.getSize();
		long totalElements=accountPage.getTotalElements();
			
		Map<String,Long> pagePropertys = new ConcurrentHashMap<>();
			
		pagePropertys.put("totalPages",totalPages);
		pagePropertys.put("pageNumber",pageNumber);
		pagePropertys.put("numberOfElements",numberOfElements);
		pagePropertys.put("size",size1);
		pagePropertys.put("totalElements",totalElements);
						
		AccountPageInfo accountPageInfo=new AccountPageInfo();
		accountPageInfo.setPagePropertys(pagePropertys);
		accountPageInfo.setAccountInfos(accountInfos);	
			 

		LOGGER.info("--- account page return successfully ---");
		return new ResponseEntity<>(accountPageInfo,HttpStatus.OK);
	}
	
	/*
	 * 
	 * get all account
	 * 
	 */
	
	@RequestMapping(value = "/all",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "Get all account.", notes = "It will provide of list of accounts.")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-Access-Token", required = true, dataType = "string", paramType = "header")})
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
    public    @ResponseBody
    ResponseEntity<?> getAllAccount(@ApiParam(value = "The account  sort by account number, first name,last name,last name,area", required = true)
    								@RequestParam(value = "sort", required = true, defaultValue = DEFAULT_PAGE_SORT) Sort.Direction sortDirection ,
									HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("--- get all account rest controller invoked , sort -> {} ---",sortDirection);
		
		Sort sort=new Sort(sortDirection,"accountNumber","firstName","lastName","area");
		List<Account> accounts=this.accountService.getAllAccount(sort);
		List<AccountInfo> accountInfos=new ArrayList<AccountInfo>();
		
		if(accounts!=null){
		accounts.forEach(account ->{
			AccountInfo accountInfo=new AccountInfo();
			
			accountInfo.setAccountId(account.getAccountId());
			accountInfo.setAccountNumber(account.getAccountNumber());
			accountInfo.setArea(account.getArea());
			accountInfo.setCity(account.getCity());
			accountInfo.setCreatedBy(account.getCreatedBy()!=null?account.getCreatedBy().getFirstName()+" "+account.getCreatedBy().getFirstName():null);
			accountInfo.setCreatedDate(account.getCreatedDate());
			accountInfo.setCurrentAddress(account.getCurrentAddress());
			accountInfo.setFatherName(account.getFatherName());
			accountInfo.setLastName(account.getLastName());
			accountInfo.setFirstName(account.getFirstName());
			accountInfo.setLastName(account.getLastName());
			accountInfo.setModifiedBy(account.getModifiedBy()!=null?account.getModifiedBy().getFirstName()+" "+account.getModifiedBy().getLastName():null);
			accountInfo.setPinCode(account.getPinCode());
			accountInfo.setPresentAddress(account.getPresentAddress());
			accountInfo.setState(account.getState());
			
			if(account.getAccountType()!=null)
				accountInfo.setAccountTypeId(account.getAccountType().getId());
			
			
			accountInfos.add(accountInfo);
		});
		}
		
		LOGGER.info("--- account list return successfully ---");
		return new ResponseEntity<>(accountInfos,HttpStatus.OK);
	}
	
	
	/*
	 * get account by Id 
	 */
	
	@RequestMapping(value = "/{accountId}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "Get acccount by Id.", notes = "It will provide of account.")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-Access-Token", required = true, dataType = "string", paramType = "header")})
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
    public    @ResponseBody
    ResponseEntity<?> getAccountById(@ApiParam(value = "The account by Id", required = true)
										@PathVariable("accountId") Long accountId,
										HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("--- get account by Id rest controller invoked , accountId -> {} ---",accountId);
		
		Account account=this.accountService.getAccountById(accountId);
		if(account==null){
			LOGGER.info("--- account does not exist's ---");
			throw new ResourceNotFoundException("account does not exist's ...!");
		}
		
		AccountInfo accountInfo=new AccountInfo();
		
		accountInfo.setAccountId(account.getAccountId());
		accountInfo.setAccountNumber(account.getAccountNumber());
		accountInfo.setArea(account.getArea());
		accountInfo.setCity(account.getCity());
		accountInfo.setCreatedBy(account.getCreatedBy()!=null?account.getCreatedBy().getFirstName()+" "+account.getCreatedBy().getLastName():null);
		accountInfo.setCreatedDate(account.getCreatedDate());
		accountInfo.setCurrentAddress(account.getCurrentAddress());
		accountInfo.setFatherName(account.getFatherName());
		accountInfo.setLastName(account.getLastName());
		accountInfo.setFirstName(account.getFirstName());
		accountInfo.setLastName(account.getLastName());
		accountInfo.setModifiedBy(account.getModifiedBy()!=null?account.getModifiedBy().getFirstName()+" "+account.getModifiedBy().getLastName():null);
		accountInfo.setPinCode(account.getPinCode());
		accountInfo.setPresentAddress(account.getPresentAddress());
		accountInfo.setState(account.getState());
		
		if(account.getAccountType()!=null)
			accountInfo.setAccountTypeId(account.getAccountType().getId());
		
		
		LOGGER.info("--- account info return sucessfully ---");
		return new ResponseEntity<>(accountInfo,HttpStatus.OK);
	}
	
	
	/*
	 * create new account  
	 */
	
	@RequestMapping(value = "",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "Create new account .", notes = "provide valid  account Object.")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-Access-Token", required = true, dataType = "string", paramType = "header")})
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
    public    @ResponseBody
    ResponseEntity<?> createDatabase(@ApiParam(value = "The account info object", required = true)
									@RequestBody AccountInfo accountInfo,
									HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("--- create account rest controller invoked , account -> {} ---",accountInfo);
		
		if(accountInfo.getFirstName()==null || accountInfo.getFirstName().isEmpty())
		{
			LOGGER.info("--- first name or last name is empty or null ---");
			throw new DataFormatException("first name (or) last name is doesn't exist's ...!");
		}
		
		if(accountInfo.getAccountTypeId()==null){
			LOGGER.info("--- account type does not exist's  ---");
			throw new DataFormatException("account type doesn't exist's ...!");
		}
		
		AccountType accountType=this.accountTypeService.getAccountType(accountInfo.getAccountTypeId());
		if(accountType==null){
			LOGGER.info("--- account type does not exist's , account type id -> {}  ---",accountInfo.getAccountTypeId());
			throw new DataFormatException("account type doesn't exist's ...!");
		}
		
		if(accountInfo.getFatherName()==null || accountInfo.getFatherName().isEmpty()){
			LOGGER.info("--- father name is empty or null ---");
			throw new DataFormatException("father name doesn't exist's ...!");
		}
		
		if(accountInfo.getPinCode()==null || accountInfo.getPinCode().isEmpty()){
			LOGGER.info("--- pincode is empty or null ---");
			throw new DataFormatException("pincode doesn't exist's ...!");
		}
		
		if(accountInfo.getState()==null || accountInfo.getState().isEmpty()){
			LOGGER.info("---  state is empty or null ---");
			throw new DataFormatException("state doesn't exist's ...!");
		}
		
		String userName=this.userInfoService.getUserNameForAthentication();
		UserInfo createdBy=this.userInfoService.getUserInfoUserName(userName);
		
		Account account=new Account();
				
		account.setFirstName(accountInfo.getFirstName());
		account.setLastName(accountInfo.getLastName());
		account.setFatherName(accountInfo.getFatherName());
		account.setCurrentAddress(accountInfo.getCurrentAddress());
		account.setPresentAddress(accountInfo.getPresentAddress());
		account.setPinCode(accountInfo.getPinCode());
		account.setArea(accountInfo.getArea());
		account.setCity(accountInfo.getCity());
		account.setState(accountInfo.getState());
		account.setCreatedDate(new Date());
		account.setCreatedBy(createdBy);
		account.setModifiedDate(new Date());
		account.setModifiedBy(createdBy);
		account.setAccountType(accountType);
		
		Account createdAccount=this.accountService.createOrUpdateAccount(account);		
		if(createdAccount!=null){
			createdAccount.setAccountNumber(accountType.getAccStartFrom()+createdAccount.getAccountId());
			Account updateAccount=this.accountService.createOrUpdateAccount(createdAccount);
			LOGGER.debug("--- created account object -> {}  ---",updateAccount);
		}
				
		LOGGER.info("--- account created successfully ---");
		return new ResponseEntity<>(HttpStatus.CREATED);
		
		
	}
	
	
	
	
	/*
	 * update account by Id 
	 */
	
	@RequestMapping(value = "/{accountId}",
            method = RequestMethod.PUT,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "Update new account .", notes = "provide valid  account Object.")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-Access-Token", required = true, dataType = "string", paramType = "header")})
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
    public    @ResponseBody
    ResponseEntity<?> updateDatabase(@ApiParam(value = "The account Id", required = true)
										@PathVariable("accountId") final Long accountId,
										@ApiParam(value = "The database Object", required = true)
										@RequestBody final AccountInfo accountInfo,
										HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("--- update account rest controller invoked , accountId -> {} , account -> {} ---",accountId,accountInfo);
		
		Account account=this.accountService.getAccountById(accountId);
		if(account==null){
			LOGGER.info("--- account does not exist's ---");
			throw new ResourceNotFoundException("account does not exist's ...!");
		}
		
		if(accountInfo.getFirstName()==null || accountInfo.getFirstName().isEmpty() )
		{
			LOGGER.info("--- first name or last name is empty or null ---");
			throw new DataFormatException("first name (or) last name is doesn't exist's ...!");
		}
		
		if(accountInfo.getAccountTypeId()==null){
			LOGGER.info("--- account type does not exist's  ---");
			throw new DataFormatException("account type doesn't exist's ...!");
		}
		
		AccountType accountType=this.accountTypeService.getAccountType(accountInfo.getAccountTypeId());
		if(accountType==null){
			LOGGER.info("--- account type does not exist's , account type id -> {}  ---",accountInfo.getAccountTypeId());
			throw new DataFormatException("account type doesn't exist's ...!");
		}
		
		if(accountInfo.getFatherName()==null || accountInfo.getFatherName().isEmpty()){
			LOGGER.info("--- father name is empty or null ---");
			throw new DataFormatException("father name doesn't exist's ...!");
		}
		
		if(accountInfo.getPinCode()==null || accountInfo.getPinCode().isEmpty()){
			LOGGER.info("--- pincode is empty or null ---");
			throw new DataFormatException("pincode doesn't exist's ...!");
		}
		
		if(accountInfo.getState()==null || accountInfo.getState().isEmpty()){
			LOGGER.info("---  state is empty or null ---");
			throw new DataFormatException("state doesn't exist's ...!");
		}
		
		String userName=this.userInfoService.getUserNameForAthentication();
		UserInfo modifiedBy=this.userInfoService.getUserInfoUserName(userName);
								
		account.setFirstName(accountInfo.getFirstName());
		account.setLastName(accountInfo.getLastName());
		account.setFatherName(accountInfo.getFatherName());
		account.setCurrentAddress(accountInfo.getCurrentAddress());
		account.setPresentAddress(accountInfo.getPresentAddress());
		account.setPinCode(accountInfo.getPinCode());
		account.setArea(accountInfo.getArea());
		account.setCity(accountInfo.getCity());
		account.setState(accountInfo.getState());
		account.setAccountType(accountType);
		//account.setCreatedDate(new Date());
		//account.setCreatedBy(createdBy);
		account.setModifiedDate(new Date());
		account.setModifiedBy(modifiedBy);
		
		Account updatedAccount=this.accountService.createOrUpdateAccount(account);
		LOGGER.debug("--- updated account -> {} ---",updatedAccount);
		LOGGER.info("--- account updated sucessfully ---");		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
		
	}
	
	/*
	 * delete database by Id 
	 */
	
	@RequestMapping(value = "/{accountId}",
            method = RequestMethod.DELETE,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "Delete account by Id.", notes = "provide valid account id.")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-Access-Token", required = true, dataType = "string", paramType = "header")})
	@PreAuthorize("hasAnyRole('ADMIN')")
    public    @ResponseBody
    ResponseEntity<?> deleteDatabaseById(@ApiParam(value = "The account by Id", required = true)
										@PathVariable("accountId") final Long accountId,
										final HttpServletRequest request,final HttpServletResponse response) {
		LOGGER.info("--- delete account by Id rest controller invoked , accountId -> {} ---",accountId);
		
		Account account=this.accountService.getAccountById(accountId);
		if(account==null){
			LOGGER.info("--- account does not exist's ---");
			throw new ResourceNotFoundException("account does not exist's ...!");
		}
		
		if(!account.getDayBooks().isEmpty()){
			LOGGER.info("--- the account has daybook , you can't proceed this operation ---");
			throw new ResourceAlreadyExistException("account already used some daybook ...!");
		}
		
		this.accountService.removeAccountById(accountId);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
	
	@RequestMapping(value = "/{accountId}/histories",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "Get History by account Id.", notes = "provide valid account id.")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-Access-Token", required = true, dataType = "string", paramType = "header")})
	@PreAuthorize("hasAnyRole('ADMIN')")
    public    @ResponseBody
    ResponseEntity<?> getAccountHistoriesById(@ApiParam(value = "The account by Id", required = true)
										@PathVariable("accountId") final Long accountId,
										@ApiParam(value = "The account  sort by account number, first name,last name,last name,area", required = true)
	    								@RequestParam(value = "sort", required = true, defaultValue = DEFAULT_PAGE_SORT) Sort.Direction sortDirection ,
										final HttpServletRequest request,final HttpServletResponse response) {
		LOGGER.info("--- get account history by Id rest controller invoked , accountId -> {} , sort -> {}  ---",accountId,sortDirection);
		
		Account account=this.accountService.getAccountById(accountId);
		if(account==null){
			LOGGER.info("--- account does not exist's ---");
			throw new ResourceNotFoundException("account does not exist's ...!");
		}
		
		Sort sort=new Sort(sortDirection,"transactionDate");
		List<DayBook> dayBooks=this.dayBookService.getDayBooks(account, sort);
		
		List<DayBookInfo> dayBookInfos=new ArrayList<DayBookInfo>();
		if(dayBooks!=null){
	    dayBooks.forEach(dayBook -> {
	    		DayBookInfo dayBookInfo=new DayBookInfo();
	    		
	    		dayBookInfo.setAccountId(dayBook.getAccount()!=null?dayBook.getAccount().getAccountId():null);
	    		dayBookInfo.setAccountName(dayBook.getAccount()!=null?dayBook.getAccount().getFirstName():null);
	    		dayBookInfo.setAccountNumber(dayBook.getAccount()!=null?dayBook.getAccount().getAccountNumber():null);
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
		
		LOGGER.info("--- daybook info for current account ---");
		return new ResponseEntity<>(dayBookInfos,HttpStatus.OK);
	}

}
