package com.auction.service;

import com.auction.dto.LotDto;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Optional;

public interface LotService {
    // Создание нового лота
    LotDto createLot(LotDto createLotDTO);

    // Получение лота по ID
    LotDto getLotById(Long lotId);

    // Начать торги по лоту
    void startAuction(Long lotId);

    // Остановить торги по лоту
    void stopAuction(Long lotId);

    // Получить текущую цену лота
    Integer getCurrentPrice(Long lotId);

    // Получить всех лотов с фильтрацией по статусу и пагинацией
    List<LotDto> getLotsByStatusAndPage(String status, int page);

    // Экспортировать все лоты в CSV файл
    void exportLotsToCSV(HttpServletResponse response);
}
