package com.flycatcher.pawn.broker.pojo;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author kumar
 *
 */

@EqualsAndHashCode
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestErrorInfo implements Serializable{
	private static final long serialVersionUID = 1L;
    
	private String detail;
    private String message;
    private String responseCode;
    
    public RestErrorInfo(){
    	
    }

    public RestErrorInfo(String message,String detail){
    	 this.message = message;
         this.detail = detail;
         this.responseCode=null;
    }
    public RestErrorInfo(Exception ex, String detail) {
        this.message = ex.getLocalizedMessage();
        this.detail = detail;
        this.responseCode=null;
    }
    public RestErrorInfo(Exception ex, String detail,String responseCode) {
        this.message = ex.getLocalizedMessage();
        this.detail = detail;
      
        this.responseCode=responseCode;
    }
    
    /*
     *  fully customized reponse
     */
    
    public RestErrorInfo(RestErrorInfo restErrorInfo) {
    	if(restErrorInfo!=null){
    		this.message = restErrorInfo.message;
    		this.detail = restErrorInfo.detail;
    		this.responseCode=restErrorInfo.responseCode;
    	}else{
    		this.message = "Somthing went worng ...!";
    		this.detail = "Please try after some time";
    		this.responseCode="EX101";
    	}
    }
    
}