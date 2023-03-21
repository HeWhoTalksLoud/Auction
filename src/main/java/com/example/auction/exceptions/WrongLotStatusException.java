package com.example.auction.exceptions;

public class WrongLotStatusException extends Exception {
    public WrongLotStatusException() {
        super();
    }

    public WrongLotStatusException(String message) {
        super(message);
    }

    public WrongLotStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
