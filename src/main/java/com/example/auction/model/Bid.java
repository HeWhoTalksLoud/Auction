package com.example.auction.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Bid {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "bidder_id")
    private Bidder bidder;
    private LocalDateTime bidDateTime;
    @OneToOne
    @JoinColumn(name = "lot_id")
    private Lot lot;
}
