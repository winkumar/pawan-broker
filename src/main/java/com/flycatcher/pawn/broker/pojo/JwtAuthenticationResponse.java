package com.flycatcher.pawn.broker.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author kumar
 *
 */

public class JwtAuthenticationResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String accessToken;
    private final String refreshToken;
    private final String tokenType;
    private final Date issueTime;

    public JwtAuthenticationResponse(String accessToken,String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken=refreshToken;
        this.tokenType="flycatcher jwt token";
        this.issueTime=new Date();
    }
    
    public JwtAuthenticationResponse(String accessToken,String refreshToken,String tokenType) {
        this.accessToken = accessToken;
        this.refreshToken=refreshToken;
        this.tokenType=tokenType;
        this.issueTime=new Date();
    } 

	public String getTokenType() {
		return tokenType;
	}
	
	public String getAccessToken() {
		return accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}
	
	public Date getIssueTime(){
		return issueTime;
	}
    
    
}
