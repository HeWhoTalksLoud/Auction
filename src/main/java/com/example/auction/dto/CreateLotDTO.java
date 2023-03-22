package com.example.auction.dto;

import lombok.Data;

@Data
public class CreateLotDTO {
    private String title;
    private String description;
    private Integer startPrice;
    private Integer bidPrice;
}
