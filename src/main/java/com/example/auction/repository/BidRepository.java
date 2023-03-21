package com.example.auction.repository;

import com.example.auction.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    @Query(value = "SELECT * FROM bid WHERE lot_id = :lotID ORDER BY bid_date_time ASC LIMIT 1",
            nativeQuery = true,
            countQuery = "SELECT COUNT(*) FROM bid")
    List<Bid> findFirstBid(@Param("lotID") Long lotID);

    @Query(value = "SELECT bidder_id  FROM bid\n" +
            "         WHERE lot_id = :lotID\n" +
            "         GROUP BY bidder_id\n" +
            "         ORDER BY COUNT(bidder_id) DESC;",
            nativeQuery = true,
            countQuery = "SELECT COUNT(*) FROM bid")
    List<Long> findMostFrequentBidderId(@Param("lotID") Long lotID);

    List<Bid> findByBidderIdAndLotIdOrderByBidDateTimeDesc(Long bidderID, Long lotID);
    List<Bid> findAllByLotIdOrderByBidDateTimeAsc(Long lotID);
    Integer countAllByLotId(Long lotID);
}
