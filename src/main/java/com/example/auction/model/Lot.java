package com.example.auction.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Lot {
    @Id
    @GeneratedValue
    private Long id;
    private String status;
    private String title;
    private String description;
    private Integer startPrice;
    private Integer bidPrice;
}
