package com.example.auction.dto;

import com.example.auction.model.Bid;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BidDTO {
    private String bidderName;
    private LocalDateTime bidDateTime;
    public static BidDTO fromBid(Bid bid) {
        if (bid == null) return null;
        BidDTO dto = new BidDTO();
        dto.setBidderName(bid.getBidder().getName());
        dto.setBidDateTime(bid.getBidDateTime());
        return dto;
    }
    public Bid toBid() {
        Bid bid = new Bid();
        bid.setBidDateTime(this.getBidDateTime());
        return bid;
    }
}
