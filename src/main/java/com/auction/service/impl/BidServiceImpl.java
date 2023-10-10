package com.auction.service.impl;

import com.auction.dto.BidDto;
import com.auction.mapper.BidMapper;
import com.auction.model.Bid;
import com.auction.model.Lot;
import com.auction.model.LotStatus;
import com.auction.repository.BidRepository;
import com.auction.repository.LotRepository;
import com.auction.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final LotRepository lotRepository;
    private final BidMapper bidMapper;

    @Override
    public BidDto createBid(Long lotId, BidDto bidDto) {
        Optional<Lot> lotOptional = lotRepository.findById(lotId);

        if (lotOptional.isPresent()) {
            Lot lot = lotOptional.get();

            if (lot.getStatus() == LotStatus.STARTED) {
                Bid bid = bidMapper.toEntity(bidDto);
                bid.setLot(lot);
                return bidMapper.toDto(bidRepository.save(bid));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Лот в неверном статусе");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Лот не найден");
        }
    }

    @Override
    public BidDto getFirstBidder(Long lotId) {
        Optional<Bid> firstBid = bidRepository.findFirstByLotIdOrderByDateAsc(lotId);

        if (firstBid.isPresent()) {
            return bidMapper.toDto(firstBid.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Лот не найден");
        }
    }

    @Override
    public BidDto getMostFrequentBidder(Long lotId) {

        return null;
    }

    @Override
    public BidDto getLastBid(Long lotId) {
        Optional<Bid> lastBidOptional = bidRepository.findFirstByLotIdOrderByDateDesc(lotId);

        if (lastBidOptional.isPresent()) {
            return bidMapper.toDto(lastBidOptional.get());
        } else {
            // Если ставок нет, вернуть сообщение о том, что ставок еще не было
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ставок еще не было");
        }
    }
}
