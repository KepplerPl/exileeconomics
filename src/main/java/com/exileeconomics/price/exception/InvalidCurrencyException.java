package com.exileeconomics.price.exception;

public class InvalidCurrencyException extends Exception{
    public InvalidCurrencyException() {
        super();
    }

    public InvalidCurrencyException(String message) {
        super(message);
    }

    public InvalidCurrencyException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCurrencyException(Throwable cause) {
        super(cause);
    }

    protected InvalidCurrencyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
