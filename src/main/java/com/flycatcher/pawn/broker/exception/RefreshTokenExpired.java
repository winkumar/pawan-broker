package com.flycatcher.pawn.broker.exception;


/**
 * @author kumar
 *
 */

public class RefreshTokenExpired extends RuntimeException {
	   
	private static final long serialVersionUID = 1L;

	public RefreshTokenExpired() {
        super();
    }

    public RefreshTokenExpired(String message, Throwable cause) {
        super(message, cause);
    }
    
    public RefreshTokenExpired(String message,String reason) {
        super(message+" "+reason);
    }
       
    public RefreshTokenExpired(String message) {
        super(message);
    }

    public RefreshTokenExpired(Throwable cause) {
        super(cause);
    }

}