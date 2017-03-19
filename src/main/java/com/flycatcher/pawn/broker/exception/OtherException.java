package com.flycatcher.pawn.broker.exception;

/**
 * @author kumar
 *
 */

public class OtherException extends RuntimeException {
	   
	private static final long serialVersionUID = 1L;

	public OtherException() {
        super();
    }

    public OtherException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public OtherException(String message,String reason) {
        super(message+" "+reason);
    }
       
    public OtherException(String message) {
        super(message);
    }

    public OtherException(Throwable cause) {
        super(cause);
    }

}
