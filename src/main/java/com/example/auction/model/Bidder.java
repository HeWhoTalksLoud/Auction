package com.example.auction.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Bidder {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
