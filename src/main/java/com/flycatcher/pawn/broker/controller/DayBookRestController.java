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
import com.flycatcher.pawn.broker.exception.ResourceNotFoundException;
import com.flycatcher.pawn.broker.model.Account;
import com.flycatcher.pawn.broker.model.DayBook;
import com.flycatcher.pawn.broker.model.TransactionType;
import com.flycatcher.pawn.broker.model.UserInfo;
import com.flycatcher.pawn.broker.pojo.DayBookInfo;
import com.flycatcher.pawn.broker.pojo.DayBookPageInfo;
import com.flycatcher.pawn.broker.service.AccountService;
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
	
	@Autowired
	public DayBookRestController(final DayBookService dayBookService,final UserInfoService userInfoService,final AccountService accountService){
		LOGGER.info("--- DayBookRestController Invoked ---");
		
		this.dayBookService=dayBookService;
		this.userInfoService=userInfoService;
		this.accountService=accountService;
								
	}
	
	
	
	@RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "Get Page current account.", notes = "It will provide page of account.")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-Access-Token", required = true, dataType = "string", paramType = "header")})
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
    public    @ResponseBody
    ResponseEntity<?> getPageOfDayBook(
    		@ApiParam(value = "The page number (zero-based)", required = true)
			@RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
			@ApiParam(value = "The page size", required = true)
			@RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
			@ApiParam(value = "The daybook list sort", required = true)
			@RequestParam(value = "sort", required = true, defaultValue = DEFAULT_PAGE_SORT) Sort.Direction sort ,
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
    @ApiOperation(value = "Get all daybook.", notes = "It will provide of list of daybooks.")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-Access-Token", required = true, dataType = "string", paramType = "header")})
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
    public    @ResponseBody
    ResponseEntity<?> getAllDayBook(@ApiParam(value = "The daybook  sort by transaction date", required = true)
    								@RequestParam(value = "sort", required = true, defaultValue = DEFAULT_PAGE_SORT) Sort.Direction sortDirection ,
									HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("--- get all daybook rest controller invoked , sort -> {} ---",sortDirection);
		
		Sort sort=new Sort(sortDirection,"transactionDate");
		List<DayBook> dayBooks=this.dayBookService.getAllDayBook(sort);
		
		
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
		

		LOGGER.info("--- daybook list return successfully ---");
		return new ResponseEntity<>(dayBookInfos,HttpStatus.OK);
		
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
		
		
		this.dayBookService.createOrUpdateDayBook(dayBook);
		
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
		
		if(dayBookInfo.getTransactionDate()==null){
			dayBookInfo.setTransactionDate(new Date());
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
		
		this.dayBookService.createOrUpdateDayBook(dayBook);
		
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
		
		this.dayBookService.removeDayBookById(dayBookId);
		LOGGER.info("--- daybook removed successfully ---");
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
}


