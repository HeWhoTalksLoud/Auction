package com.example.auction.dto;

import com.example.auction.model.Bidder;
import lombok.Data;

@Data
public class BidderNameDTO {
    private String name;
    public static BidderNameDTO fromBidder(Bidder bidder) {
        BidderNameDTO dto = new BidderNameDTO();
        dto.setName(bidder.getName());
        return dto;
    }
}
