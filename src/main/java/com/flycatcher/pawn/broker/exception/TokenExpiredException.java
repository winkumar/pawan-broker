package com.flycatcher.pawn.broker.exception;

/**
 * @author kumar
 *
 */

public class TokenExpiredException extends RuntimeException {
	   
	private static final long serialVersionUID = 1L;

	public TokenExpiredException() {
        super();
    }

    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public TokenExpiredException(String message,String reason) {
        super(message+" "+reason);
    }
       
    public TokenExpiredException(String message) {
        super(message);
    }

    public TokenExpiredException(Throwable cause) {
        super(cause);
    }

}