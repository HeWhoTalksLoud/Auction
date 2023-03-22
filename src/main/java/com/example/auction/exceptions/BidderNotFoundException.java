package com.example.auction.exceptions;

public class BidderNotFoundException extends Exception {
    public BidderNotFoundException() {
    }

    public BidderNotFoundException(String message) {
        super(message);
    }

    public BidderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
