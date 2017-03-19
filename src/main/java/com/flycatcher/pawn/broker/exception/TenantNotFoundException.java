package com.flycatcher.pawn.broker.exception;

/**
 * @author kumar
 *
 */

public class TenantNotFoundException extends RuntimeException {
   
	private static final long serialVersionUID = 1L;

	public TenantNotFoundException() {
        super();
    }

    public TenantNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
       
    public TenantNotFoundException(String message) {
        super(message);
    }

    public TenantNotFoundException(Throwable cause) {
        super(cause);
    }

}