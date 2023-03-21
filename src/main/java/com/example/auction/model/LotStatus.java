package com.example.auction.model;

public enum LotStatus {
    STARTED("STARTED"),
    STOPPED("STOPPED"),
    CREATED("CREATED");
    final String string;

    LotStatus(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public static LotStatus fromString(String s) {
        switch (s) {
            case "STARTED":
                return LotStatus.STARTED;
            case "STOPPED":
                return LotStatus.STOPPED;
            case "CREATED":
                return LotStatus.CREATED;
            default:
                return null;
        }
    }
}
