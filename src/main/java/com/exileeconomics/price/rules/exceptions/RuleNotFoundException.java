package com.exileeconomics.price.rules.exceptions;

public class RuleNotFoundException extends Exception{
    public RuleNotFoundException() {
        super();
    }

    public RuleNotFoundException(String message) {
        super(message);
    }

    public RuleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuleNotFoundException(Throwable cause) {
        super(cause);
    }

    protected RuleNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
