package com.example.auction.repository;

import com.example.auction.model.Lot;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LotRepository extends JpaRepository<Lot, Long>,
        PagingAndSortingRepository<Lot, Long> {
    Optional<Lot> findById(Long id);
    List<Lot> findAllByStatus(String status, Pageable pageable);
    List<Lot> findAll();
}
