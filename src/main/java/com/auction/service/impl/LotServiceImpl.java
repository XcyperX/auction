package com.auction.service.impl;

import com.auction.dto.LotDto;
import com.auction.mapper.LotMapper;
import com.auction.model.Lot;
import com.auction.model.LotStatus;
import com.auction.repository.BidRepository;
import com.auction.repository.LotRepository;
import com.auction.service.BidService;
import com.auction.service.LotService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LotServiceImpl implements LotService {

    private final LotRepository lotRepository;
    private final BidRepository bidRepository;
    private final BidService bidService;
    private final LotMapper lotMapper;

    @Override
    public LotDto createLot(LotDto createLotDTO) {
        Lot lot = lotMapper.toEntity(createLotDTO);
        lot.setStatus(LotStatus.CREATED);
        return lotMapper.toDto(lotRepository.save(lot));
    }

    @Override
    public LotDto getLotById(Long lotId) {
        if (Objects.isNull(lotId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lot id = null");
        } else {
            Optional<Lot> lotOptional = lotRepository.findById(lotId);
            if (lotOptional.isPresent()) {
                return lotMapper.toDto(lotOptional.get());
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Лот не найден");
            }
        }
    }

    @Override
    public void startAuction(Long lotId) {
        if (Objects.isNull(lotId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lot id = null");
        } else {
            Optional<Lot> lotOptional = lotRepository.findById(lotId);
            if (lotOptional.isPresent()) {
                Lot lot = lotOptional.get();
                lot.setStatus(LotStatus.STARTED);
                lotRepository.save(lot);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Лот не найден");
            }
        }
    }

    @Override
    public void stopAuction(Long lotId) {
        if (Objects.isNull(lotId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lot id = null");
        } else {
            Optional<Lot> lotOptional = lotRepository.findById(lotId);
            if (lotOptional.isPresent()) {
                Lot lot = lotOptional.get();
                lot.setStatus(LotStatus.STOPPED);
                lotRepository.save(lot);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Лот не найден");
            }
        }
    }

    @Override
    public Integer getCurrentPrice(Long lotId) {
        if (Objects.isNull(lotId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lot id = null");
        } else {
            Optional<Lot> lotOptional = lotRepository.findById(lotId);
            if (lotOptional.isPresent()) {
                Lot lot = lotOptional.get();
                Integer bidCount = bidRepository.countByLotId(lotId);
                return bidCount * lot.getBidPrice() + lot.getStartPrice();
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Лот не найден");
            }
        }
    }

    @Override
    public List<LotDto> getLotsByStatusAndPage(String status, int page) {
        Sort sort = Sort.by("id").ascending();
        PageRequest pageRequest = PageRequest.of(page, 10, sort);

        Page<Lot> lotPage = lotRepository.findByStatus(LotStatus.valueOf(status), pageRequest);
        return lotPage.getContent().stream()
                .map(lotMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public void exportLotsToCSV(HttpServletResponse response) {
        // Получите список лотов, которые вы хотите экспортировать
        List<Lot> lots = lotRepository.findAll();

        if (lots.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Нет лотов для экспорта.");
        }

        try {
            // Установите заголовки для ответа, указав тип файла и заголовок
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=lots.csv");

            // Получите объект PrintWriter для записи данных в ответ
            PrintWriter writer = response.getWriter();

            // Запишите заголовки CSV файла
            writer.println("id,title,status,lastBidder,currentPrice");

            // Запишите каждый лот в формате CSV
            for (Lot Lot : lots) {
                String currentPrice = String.valueOf(getCurrentPrice(Lot.getId())); // Вычислите текущую цену
                String lastBidder = bidService.getLastBid(Lot.getId()).getBidderName(); // Получите последнего ставившего

                writer.println(
                        Lot.getId() + "," +
                        Lot.getTitle() + "," +
                        Lot.getStatus().name() + "," +
                        lastBidder + "," +
                        currentPrice
                );
            }

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка экспорта csv файла.", e);
        }
    }
}
