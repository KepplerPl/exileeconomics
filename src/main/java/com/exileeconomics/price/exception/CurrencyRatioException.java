package com.exileeconomics.price.exception;

public class CurrencyRatioException extends Exception{
    public CurrencyRatioException() {
        super();
    }

    public CurrencyRatioException(String message) {
        super(message);
    }

    public CurrencyRatioException(String message, Throwable cause) {
        super(message, cause);
    }

    public CurrencyRatioException(Throwable cause) {
        super(cause);
    }

    protected CurrencyRatioException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
