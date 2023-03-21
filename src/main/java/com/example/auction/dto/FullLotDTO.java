package com.example.auction.dto;

import com.example.auction.model.Lot;
import com.example.auction.model.LotStatus;
import lombok.Data;

@Data
public class FullLotDTO {
    private Long id;
    private LotStatus status;
    private String title;
    private String description;
    private Integer startPrice;
    private Integer bidPrice;
    private Integer currentPrice;
    private BidDTO lastBid;
    public static FullLotDTO fromLot(Lot lot) {
        FullLotDTO dto = new FullLotDTO();
        dto.setId(lot.getId());
        dto.setStatus(LotStatus.fromString(lot.getStatus()));
        dto.setTitle(lot.getTitle());
        dto.setDescription(lot.getDescription());
        dto.setStartPrice(lot.getStartPrice());
        dto.setBidPrice(lot.getBidPrice());
        return dto;
    }
}
