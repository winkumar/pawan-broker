package com.flycatcher.pawn.broker.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.flycatcher.pawn.broker.UrlPath;
import com.flycatcher.pawn.broker.exception.ResourceAlreadyExistException;
import com.flycatcher.pawn.broker.exception.ResourceNotFoundException;
import com.flycatcher.pawn.broker.model.Account;
import com.flycatcher.pawn.broker.service.AccountService;



/**
 * @author kumar
 *
 */

@RestController
@RequestMapping(value=UrlPath.ACCOUNT_PATH)
public class AccountRestController extends AbstractRestHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountRestController.class);
	
	private final AccountService accountService;
	
	@Autowired
	public AccountRestController(final AccountService accountService){
		LOGGER.info("--------- AccountRestController Invoked ----------------");
		
		this.accountService=accountService;
								
	}
	
	/*
	 * get page account 
	 */
	
	@RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "Get Page current account.", notes = "It will provide page of account.")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-Access-Token", required = true, dataType = "string", paramType = "header")})
    public    @ResponseBody
    ResponseEntity<?> getPageOfDatabase(
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
	    			
		final Page<Account> clDatabasePage=this.accountService.getPageOfAccount(search,pageable);
		LOGGER.info("--- account page return successfully ---");
		return new ResponseEntity<>(clDatabasePage,HttpStatus.OK);
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
    public    @ResponseBody
    ResponseEntity<?> getAllDatabase(@ApiParam(value = "The account  sort by account number, first name,last name,last name,area", required = true)
    								@RequestParam(value = "sort", required = true, defaultValue = DEFAULT_PAGE_SORT) Sort.Direction sortDirection ,
										HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("--- get all account rest controller invoked , sort -> {} ---",sortDirection);
		
		Sort sort=new Sort(sortDirection,"accountNumber","firstName","lastName","area");
		List<Account> accounts=this.accountService.getAllAccount(sort);
		
		LOGGER.info("--- account list return successfully ---");
		return new ResponseEntity<>(accounts,HttpStatus.OK);
	}
	
	
	/*
	 * get account by Id 
	 */
	
	@RequestMapping(value = "/{accountId}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "Get acccount by Id.", notes = "It will provide of account.")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-Access-Token", required = true, dataType = "string", paramType = "header")})
    public    @ResponseBody
    ResponseEntity<?> getDatabaseById(@ApiParam(value = "The account by Id", required = true)
										@PathVariable("accountId") Long accountId,
										HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("--- get account by Id rest controller invoked , accountId -> {} ---",accountId);
		
		Account account=this.accountService.getAccountById(accountId);
		if(account==null){
			LOGGER.info("--- account does not exist's ---");
			throw new ResourceNotFoundException("account does not exist's ...!");
		}
		
		return new ResponseEntity<>(account,HttpStatus.OK);
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
    public    @ResponseBody
    ResponseEntity<?> createDatabase(@ApiParam(value = "The account object", required = true)
									@RequestBody Account account,
									HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("--- create account rest controller invoked , account -> {} ---",account);
		
		/*if(account.==null || databaseInfo.getDatabaseName().trim().length()<=0){
			LOGGER.info("--- database name does not exist's ---");
			throw new ResourceNotFoundException("database name does not exist's ...!");
		}
		
		final List<ClDatabase> clDatabases=this.clDatabaseService.getAllClDatabaseByName(databaseInfo.getDatabaseName());
		if(clDatabases.size()>0){
			LOGGER.info("--- database name already exist's ----");
			throw new ResourceAlreadyExistException("database name already exist's ...!");
		}
		
		if(databaseInfo.getUsername()==null || databaseInfo.getUsername().trim().length()<=0){
			LOGGER.info("--- database username name does not exist's ---");
			throw new ResourceNotFoundException("database username name does not exist's ...!");
		}
		
		if(databaseInfo.getPassword()==null || databaseInfo.getPassword().trim().length()<=0){
			LOGGER.info("--- database password does not exist's ---");
			throw new ResourceNotFoundException("database password does not exist's ...!");
		}
		
		if(databaseInfo.getMinPoolSize()==null){
			LOGGER.info("--- database min pool size is null  ---");
			databaseInfo.setMinPoolSize(2);
		}
		
		if(databaseInfo.getMaxPoolSize()==null){
			LOGGER.info("--- database max pool size is null  ---");
			databaseInfo.setMaxPoolSize(4);
		}
				
		if(databaseInfo.getClDatabaseServerId()==null){
			LOGGER.info("--- database server id does not exist's ---");
			throw new ResourceNotFoundException("database server does not exist's ...!");
		}
		
		final ClDatabaseServer clDatabaseServer=this.clDatabaseServerService.getClDatabaseServerById(databaseInfo.getClDatabaseServerId());
		if(clDatabaseServer==null){
			LOGGER.info("--- database server does not exist's , this id --> {} ---",databaseInfo.getClDatabaseServerId());
			throw new ResourceNotFoundException("database server does not exist's ...!");
		}
		
		final ClDatabase clDatabase=new ClDatabase();
		clDatabase.setDatabaseName(databaseInfo.getDatabaseName());
		clDatabase.setMinPoolSize(databaseInfo.getMinPoolSize());
		clDatabase.setMaxPoolSize(databaseInfo.getMaxPoolSize());
		clDatabase.setUsername(databaseInfo.getUsername());
		clDatabase.setPassword(databaseInfo.getPassword());
		clDatabase.setClDatabaseServer(clDatabaseServer);
		
		final ClDatabase createdClDatabase=this.clDatabaseService.createNewClDatabase(clDatabase);
		
		LOGGER.info("created database  object --> {} --",createdClDatabase);
		
		response.setHeader("Location", request.getRequestURL().append("/").append(createdClDatabase.getDatabaseId()).toString());
*/				
		return new ResponseEntity<>(account,HttpStatus.CREATED);
		
		
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
    public    @ResponseBody
    ResponseEntity<?> updateDatabase(@ApiParam(value = "The account Id", required = true)
										@PathVariable("accountId") final Long accountId,
										@ApiParam(value = "The database Object", required = true)
										@RequestBody final Account account,
										HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("--- update account rest controller invoked , accountId -> {} , account -> {} ---",accountId,account);
		
		Account accountObject=this.accountService.getAccountById(accountId);
		if(accountObject==null){
			LOGGER.info("--- account does not exist's ---");
			throw new ResourceNotFoundException("account does not exist's ...!");
		}
		
		
		
				
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
    public    @ResponseBody
    ResponseEntity<?> deleteDatabaseById(@ApiParam(value = "The account by Id", required = true)
										@PathVariable("accountId") final Long accountId,
										final HttpServletRequest request,final HttpServletResponse response) {
		LOGGER.info("--- get account by Id rest controller invoked , accountId -> {} ---",accountId);
		
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
	

}
