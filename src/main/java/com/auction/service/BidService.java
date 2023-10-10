package com.auction.service;

import com.auction.dto.BidDto;

import java.util.Optional;

public interface BidService {
    // Создать ставку по лоту
    BidDto createBid(Long lotId, BidDto bidDto);

    // Получить первого ставившего на лот
    BidDto getFirstBidder(Long lotId);

    // Получить наиболее частого ставившего на лот
    BidDto getMostFrequentBidder(Long lotId);

    // Получить последнюю ставку по лоту
    BidDto getLastBid(Long lotId);
}
