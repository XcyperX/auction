package com.auction.controller;

import com.auction.dto.BidDto;
import com.auction.dto.LotDto;
import com.auction.model.Bid;
import com.auction.model.Lot;
import com.auction.service.BidService;
import com.auction.service.LotService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lot")
public class AuctionController {

    private final BidService bidService;
    private final LotService lotService;

    @GetMapping("/{id}/first")
    public ResponseEntity<BidDto> getFirstBidder(@PathVariable Long id) {
        return ResponseEntity.ok(bidService.getFirstBidder(id));
    }

    @GetMapping("/{id}/frequent")
    public ResponseEntity<BidDto> getMostFrequentBidder(@PathVariable Long id) {
        return ResponseEntity.ok(bidService.getMostFrequentBidder(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LotDto> getFullLot(@PathVariable Long id) {
        return ResponseEntity.ok(lotService.getLotById(id));
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<String> startAuction(@PathVariable Long id) {
        lotService.startAuction(id);
        return ResponseEntity.ok("Лот переведен в статус начато");
    }

    @PostMapping("/{id}/bid")
    public ResponseEntity<BidDto> createBid(@PathVariable Long id, @RequestBody BidDto bidDto) {
        return ResponseEntity.ok(bidService.createBid(id, bidDto));
    }

    @PostMapping("/{id}/stop")
    public ResponseEntity<String> stopAuction(@PathVariable Long id) {
        lotService.stopAuction(id);
        return ResponseEntity.ok("Лот переведен в статус остановлен");
    }

    @PostMapping
    public ResponseEntity<LotDto> createLot(@RequestBody LotDto createLot) {
        return ResponseEntity.ok(lotService.createLot(createLot));
    }

    @GetMapping
    public ResponseEntity<List<LotDto>> findLots(@RequestParam(required = false) String status, @RequestParam(required = false, defaultValue = "0") Integer page) {
        return ResponseEntity.ok(lotService.getLotsByStatusAndPage(status, page));
    }

    @GetMapping("/export")
    public void exportLotsToCSV(HttpServletResponse response) {
        lotService.exportLotsToCSV(response);
    }
}
