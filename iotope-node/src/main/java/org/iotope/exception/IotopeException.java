package org.iotope.exception;

/**
 * Trivial base class to start the IOTOPE exception hierarchy
 */
public class IotopeException extends RuntimeException {

    public IotopeException() {
        super();
    }

    public IotopeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IotopeException(String message) {
        super(message);
    }

    public IotopeException(Throwable cause) {
        super(cause);
    }
    
}
