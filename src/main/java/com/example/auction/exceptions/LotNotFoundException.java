package com.example.auction.exceptions;

public class LotNotFoundException extends  Exception {
    public LotNotFoundException() {
        super();
    }

    public LotNotFoundException(String message) {
        super(message);
    }

    public LotNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
