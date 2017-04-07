package com.flycatcher.pawn.broker.controller;




import io.jsonwebtoken.ExpiredJwtException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.flycatcher.pawn.broker.exception.AccessDeniedException;
import com.flycatcher.pawn.broker.exception.DataFormatException;
import com.flycatcher.pawn.broker.exception.OtherException;
import com.flycatcher.pawn.broker.exception.RefreshTokenExpired;
import com.flycatcher.pawn.broker.exception.ResourceAlreadyExistException;
import com.flycatcher.pawn.broker.exception.ResourceNotFoundException;
import com.flycatcher.pawn.broker.exception.TokenExpiredException;
import com.flycatcher.pawn.broker.exception.TokenNotFoundException;
import com.flycatcher.pawn.broker.pojo.RestErrorInfo;

import javax.servlet.http.HttpServletResponse;

/**
 * @author kumar
 *
 */

/**
 * This class is meant to be extended by all REST resource "controllers".
 * It contains exception mapping and other common REST API functionality
 */
@ControllerAdvice
public abstract class AbstractRestHandler{
	
   	
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
   
    protected static final String  DEFAULT_PAGE_SIZE = "20";
    protected static final String DEFAULT_PAGE_NUM = "0";
    protected static final String DEFAULT_PAGE_SORT="ASC";
    
    //user state
    protected static final String  DEFAULT_USER_STATE = "false";
    
    //isObserver status
    
    protected static final String  DEFAULT_IS_OBSERVER = "true";
    
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataFormatException.class)
    public @ResponseBody
    RestErrorInfo handleDataStoreException(DataFormatException ex, WebRequest request, HttpServletResponse response) {
        log.info("Converting Data Store exception to RestResponse : " + ex.getMessage());

         
        return new RestErrorInfo(ex.getLocalizedMessage(), "You messed up.");
    }
    
    
    
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ResourceAlreadyExistException.class)
    public    @ResponseBody
    RestErrorInfo handleDataAlreadyExistsException(ResourceAlreadyExistException ex, WebRequest request, HttpServletResponse response) {
        log.error("Handle Data Already Exists RestResponse : " + ex.getMessage());

             
        return new RestErrorInfo(ex.getLocalizedMessage(), "Resource Already Exists.");
    }
    

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(OtherException.class)
    public  @ResponseBody
    RestErrorInfo handleOtherExistsException(OtherException ex, WebRequest request, HttpServletResponse response) {
        log.error("Handle Other Exception : " + ex.getMessage());

               
        return new RestErrorInfo(ex.getLocalizedMessage(), "Some thing went Worng.");
    }
    
    
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public RestErrorInfo handleUnauthorizedException(AccessDeniedException ex) {
    	log.error("Unauthorized Exception : " + ex.getMessage());
    	
    	    
        return new RestErrorInfo(ex.getLocalizedMessage(), "Unauthorized Exception.");
    } 
    
    
  
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public    @ResponseBody
    RestErrorInfo handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request, HttpServletResponse response) {
        log.error("ResourceNotFoundException handler:" + ex.getMessage());
                   
                
        return new RestErrorInfo(ex.getLocalizedMessage(), "Sorry I couldn't find it.");
    }

      
   /*
    * 
    * Token Expired Exception Handle
    * 
    */
 
    @ExceptionHandler(TokenExpiredException.class)
    public    @ResponseBody
    RestErrorInfo handleTokenExppiredException(TokenExpiredException ex, WebRequest request, HttpServletResponse response) {
        log.info("TokenExpiredException handler:" + ex.getMessage());
       
        response.setStatus(499);
        
               
        return new RestErrorInfo(ex.getLocalizedMessage(), "Access Token Expired ...");
    }
    
    /*
     * 
     * TokenNotFoundException Handle
     * 
     */
  
     @ExceptionHandler(TokenNotFoundException.class)
     public    @ResponseBody
     RestErrorInfo handleTokenNotFoundException(TokenNotFoundException ex, WebRequest request, HttpServletResponse response) {
         log.error("TokenExpiredException handler:" + ex.getMessage());
                    
                
         return new RestErrorInfo("Token does not exist's", "Token not Found (or) Expired ...");
     }
    
    /*
     * 
     * DisabledException Handle
     * 
     */
    
     @ResponseStatus(HttpStatus.UNAUTHORIZED)
     @ExceptionHandler(DisabledException.class)
     public    @ResponseBody
     RestErrorInfo handleDisabledException(DisabledException ex, WebRequest request, HttpServletResponse response) {
         log.error("DisabledException handler:" + ex.getMessage());
        
         return new RestErrorInfo("Your account currently blocked contact admin", "Full authentication need to access this resource");
     }
     
     
     /*
      * 
      * DisabledException Handle
      * 
      */
     
      @ResponseStatus(HttpStatus.UNAUTHORIZED)
      @ExceptionHandler(InsufficientAuthenticationException.class)
      public    @ResponseBody
      RestErrorInfo handleInsufficientAuthenticationException(InsufficientAuthenticationException ex, WebRequest request, HttpServletResponse response) {
          log.error("InsufficientAuthenticationException handler:" + ex.getMessage());
         
          return new RestErrorInfo("Password changed because of token not valid", "Full authentication need to access this resource");
      }
      
           
     /*
      * 
      * InternalAuthenticationServiceException Handle
      * 
      */
     
      @ResponseStatus(HttpStatus.UNAUTHORIZED)
      @ExceptionHandler(InternalAuthenticationServiceException.class)
      public    @ResponseBody
      RestErrorInfo handleInternalAuthenticationServiceException(InternalAuthenticationServiceException ex, WebRequest request, HttpServletResponse response) {
          log.error("InternalAuthenticationServiceException handler:" + ex.getMessage());
         
          return new RestErrorInfo("Your account currently blocked contact admin", "Full authentication need to access this resource");
      }
     
     /*
      * 
      * InsufficientAuthenticationException Handle
      * 
      */
     
      @ResponseStatus(HttpStatus.UNAUTHORIZED)
      @ExceptionHandler(BadCredentialsException.class)
      public    @ResponseBody
      RestErrorInfo handleBadCredentialsException(BadCredentialsException ex, WebRequest request, HttpServletResponse response) {
          log.error("BadCredentialsException handler:" + ex.getMessage());
         
          return new RestErrorInfo("Username or Password is worng", "Full authentication need to access this resource");
      }
      
      /*
       * 
       * ExpiredJwtException Handle
       * 
       */
      
      @ResponseStatus(HttpStatus.UNAUTHORIZED)
      @ExceptionHandler(RefreshTokenExpired.class)
      public    @ResponseBody
      RestErrorInfo handleExpiredJwtException(RefreshTokenExpired ex, WebRequest request, HttpServletResponse response) {
           log.error("RefreshTokenExpired handler:" + ex);
          
           return new RestErrorInfo("refresh token expired", "Full authentication need to access this resource");
     }
      
    
    /*
     * 
     * Exception handler
     * 
     */
    
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(Exception.class)
    public    @ResponseBody
    RestErrorInfo handleOtherException(Exception ex, WebRequest request, HttpServletResponse response) {
        log.error("Exception handler:" + ex);
       /*
        if(ex instanceof InsufficientAuthenticationException){
        	return new RestErrorInfo("Your account currently blocked contact admin","Full authentication need to access this resource");
        }*/
      if(ex instanceof ExpiredJwtException){
    	  
    	  return new RestErrorInfo("refresh token expired", "Full authentication need to access this resource");
      }else if(ex instanceof org.springframework.security.access.AccessDeniedException){
    	  response.setStatus(401);
    	  return new RestErrorInfo("Access denied ...!", "Full authentication need to access this resource");
      }
        
        return new RestErrorInfo(ex.getLocalizedMessage(),"It is not valid request");
    }
    
       
   
  
      
}
