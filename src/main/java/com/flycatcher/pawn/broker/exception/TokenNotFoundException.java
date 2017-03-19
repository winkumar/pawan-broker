package com.flycatcher.pawn.broker.exception;


/**
 * @author kumar
 *
 */

public class TokenNotFoundException extends RuntimeException {
	   
	private static final long serialVersionUID = 1L;

	public TokenNotFoundException() {
        super();
    }

    public TokenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public TokenNotFoundException(String message,String reason) {
        super(message+" "+reason);
    }
       
    public TokenNotFoundException(String message) {
        super(message);
    }

    public TokenNotFoundException(Throwable cause) {
        super(cause);
    }

}