package com.auction.repository;

import com.auction.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    Integer countByLotId(Long lotId);

    Optional<Bid> findFirstByLotIdOrderByDateDesc(Long lotId);

    Optional<Bid> findFirstByLotIdOrderByDateAsc(Long lotId);
}