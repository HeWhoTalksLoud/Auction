package com.example.auction.service;

import com.example.auction.dto.*;
import com.example.auction.exceptions.LotNotFoundException;
import com.example.auction.exceptions.WrongLotStatusException;
import com.example.auction.model.LotStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LotService {
    BidDTO getFirstBidder(Long lotID);
    BidDTO getMostFrequentBidder(Long lotID);
    FullLotDTO getLotInfo(Long lotID);
    void startBidding(Long lotID) throws LotNotFoundException;
    void makeBid(Long lotID, BidderNameDTO bidderNameDTO) throws LotNotFoundException, WrongLotStatusException;
    void stopBidding(Long lotID) throws LotNotFoundException;
    LotDTO createLot(CreateLotDTO createLotDTO);
    List<LotDTO> getLotsPage(LotStatus lotStatus, Integer page);

    List<FullLotDTO> getAllFullLots();
    Integer getCurrentLotPrice(Long lotID);
}
