package com.exileeconomics.price.exception;

public class AveragePriceCalculationException extends Exception{
    public AveragePriceCalculationException() {
        super();
    }

    public AveragePriceCalculationException(String message) {
        super(message);
    }

    public AveragePriceCalculationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AveragePriceCalculationException(Throwable cause) {
        super(cause);
    }

    protected AveragePriceCalculationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
