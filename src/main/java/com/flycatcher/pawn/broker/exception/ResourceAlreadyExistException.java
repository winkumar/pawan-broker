package com.flycatcher.pawn.broker.exception;

/**
 * @author kumar
 *
 */

public class ResourceAlreadyExistException extends RuntimeException {
	   
		private static final long serialVersionUID = 1L;

		public ResourceAlreadyExistException() {
	        super();
	    }

	    public ResourceAlreadyExistException(String message, Throwable cause) {
	        super(message, cause);
	    }
	    
	    
	       
	    public ResourceAlreadyExistException(String message) {
	        super(message);
	    }

	    public ResourceAlreadyExistException(Throwable cause) {
	        super(cause);
	    }

	}
