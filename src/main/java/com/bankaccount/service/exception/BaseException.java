package com.bankaccount.service.exception;

public abstract class BaseException extends RuntimeException {

    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    public static final String BACKENDERROREGENERICO = "backend.genericerror";

    private final String errorCode;

    private final String httpStatus;

    public BaseException(String errorCode, String description) {
        super(description);
        this.httpStatus = INTERNAL_SERVER_ERROR;
        this.errorCode = errorCode;
    }
    
    public BaseException(String errorCode, Throwable t) {
        super(t);
        this.httpStatus = INTERNAL_SERVER_ERROR;
        this.errorCode = errorCode;
    }

    public BaseException(String errorCode, Throwable t, String description) {
        super(description, t);
        this.httpStatus = INTERNAL_SERVER_ERROR;
        this.errorCode = errorCode;
    }

    public BaseException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = INTERNAL_SERVER_ERROR;
        this.errorCode = errorCode;
    }

    public BaseException(String errorCode) {
        this.httpStatus = INTERNAL_SERVER_ERROR;
        this.errorCode = errorCode;
    }

    public BaseException(String errorCode, final String httpStatusCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatusCode;
    }

    public BaseException(String errorCode, final String httpStatusCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatusCode;
    }

    public String getErrorCode() { return errorCode; }

    public String getHttpStatus() { return httpStatus; }
}
