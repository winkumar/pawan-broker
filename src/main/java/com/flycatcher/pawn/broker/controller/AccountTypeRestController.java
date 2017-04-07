package com.flycatcher.pawn.broker.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.flycatcher.pawn.broker.UrlPath;
import com.flycatcher.pawn.broker.exception.ResourceNotFoundException;
import com.flycatcher.pawn.broker.model.Account;
import com.flycatcher.pawn.broker.model.AccountType;
import com.flycatcher.pawn.broker.pojo.AccountInfo;
import com.flycatcher.pawn.broker.service.AccountService;
import com.flycatcher.pawn.broker.service.AccountTypeService;

/**
 * 
 * @author kumar
 * @version 1.0.0
 * @since 06-Apr-2017
 * 
 */
@RestController
@RequestMapping(value=UrlPath.ACCOUNT_TYPE_PATH)
public class AccountTypeRestController extends AbstractRestHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountTypeRestController.class);
	
	private final AccountTypeService accountTypeService;
	private final AccountService accountService;

	public AccountTypeRestController(final AccountTypeService accountTypeService,final AccountService accountService){
		LOGGER.debug("--- AccountType Rest Controller ---");
		this.accountTypeService=accountTypeService;		
		this.accountService=accountService;
	}
	
		
	@RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "Get all account type.", notes = "It will provide of list of account type.")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-Access-Token", required = true, dataType = "string", paramType = "header")})
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
    public    @ResponseBody
    ResponseEntity<?> getAllAccountType(@ApiParam(value = "The account type sort by priority", required = true)
    								@RequestParam(value = "sort", required = true, defaultValue = DEFAULT_PAGE_SORT) Sort.Direction sortDirection ,
									HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("--- get all account type rest controller invoked , sort -> {} ---",sortDirection);
		
		Sort sort=new Sort(sortDirection,"priority");
		List<AccountType> accountTypes=this.accountTypeService.getAllAccountType(sort);
		
		LOGGER.info("--- account type list return successfully ---");
		return new ResponseEntity<>(accountTypes,HttpStatus.OK);
	}

	
	@RequestMapping(value = "/{accountTypeId}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "Get account type.", notes = "It will provide of account type by id.")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-Access-Token", required = true, dataType = "string", paramType = "header")})
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
    public    @ResponseBody
    ResponseEntity<?> getAccountTypeById(@ApiParam(value = "The account by Id", required = true)
										@PathVariable("accountTypeId") Long accountTypeId,
										HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("--- get account type rest controller invoked , accountTypeId -> {} ---",accountTypeId);
		
		AccountType accountType=this.accountTypeService.getAccountType(accountTypeId);
		if(accountType==null){
			LOGGER.info("--- account type does not exist's ---");
			throw new ResourceNotFoundException("account type does not exist's ...!");
		}
		
		LOGGER.info("--- account type return successfully ---");
		return new ResponseEntity<>(accountType,HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value = "/{accountTypeId}/accounts",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "Get accounts by account type.", notes = "It will provide of accounts by account type by id.")
	@ApiImplicitParams({@ApiImplicitParam(name = "X-Access-Token", required = true, dataType = "string", paramType = "header")})
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
    public    @ResponseBody
    ResponseEntity<?> getAccountsByAccountType(@ApiParam(value = "The account by Id", required = true)
										@PathVariable("accountTypeId") Long accountTypeId,
										@ApiParam(value = "The account type sort by priority", required = true)
										@RequestParam(value = "sort", required = true, defaultValue = DEFAULT_PAGE_SORT) Sort.Direction sortDirection ,
										HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("--- get account type rest controller invoked , accountTypeId -> {} ---",accountTypeId);
		
		AccountType accountType=this.accountTypeService.getAccountType(accountTypeId);
		if(accountType==null){
			LOGGER.info("--- account type does not exist's ---");
			throw new ResourceNotFoundException("account type does not exist's ...!");
		}
		
		Sort sort=new Sort(sortDirection,"accountNumber","firstName","lastName","area");
		List<Account> accounts=this.accountService.getAllAccount(accountType, sort);
		
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
		
		LOGGER.info("--- account info return successfully ---");
		return new ResponseEntity<>(accountInfos,HttpStatus.OK);
	}

}


