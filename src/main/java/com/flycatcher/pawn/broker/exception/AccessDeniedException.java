package com.flycatcher.pawn.broker.exception;

/**
 * @author kumar
 *
 */

public class AccessDeniedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AccessDeniedException() {
        super();
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(Throwable cause) {
        super(cause);
    }
}
