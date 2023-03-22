package com.example.auction.dto;

import com.example.auction.model.Lot;
import com.example.auction.model.LotStatus;
import lombok.Data;

@Data
public class LotDTO {
    private Long id;
    private LotStatus status;
    private String title;
    private String description;
    private Integer startPrice;
    private Integer bidPrice;
    public static LotDTO fromLot(Lot lot) {
        LotDTO dto = new LotDTO();
        dto.setId(lot.getId());
        dto.setStatus(LotStatus.fromString(lot.getStatus()));
        dto.setTitle(lot.getTitle());
        dto.setDescription(lot.getDescription());
        dto.setStartPrice(lot.getStartPrice());
        dto.setBidPrice(lot.getBidPrice());
        return dto;
    }
    public static LotDTO fromCreateLotDTO(CreateLotDTO createLotDTO) {
        LotDTO dto = new LotDTO();
        dto.setTitle(createLotDTO.getTitle());
        dto.setDescription(createLotDTO.getDescription());
        dto.setStartPrice(createLotDTO.getStartPrice());
        dto.setBidPrice(createLotDTO.getBidPrice());
        dto.setStatus(LotStatus.CREATED);
        return dto;
    }
    public Lot toLot() {
        Lot lot = new Lot();
        lot.setId(this.getId());
        lot.setStatus(this.getStatus().getString());
        lot.setTitle(this.getTitle());
        lot.setDescription(this.getDescription());
        lot.setStartPrice(this.getStartPrice());
        lot.setBidPrice(this.getBidPrice());
        return lot;
    }
}
