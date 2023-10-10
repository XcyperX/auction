package com.auction.mapper;

import com.auction.dto.LotDto;
import com.auction.model.Lot;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = BidMapper.class)
@Component
public interface LotMapper {
    Lot toEntity(LotDto lotDto);

    @AfterMapping
    default void linkBids(@MappingTarget Lot lot) {
        if (lot.getBids() != null) {
            lot.getBids().forEach(bid -> bid.setLot(lot));
        }
    }

    LotDto toDto(Lot lot);

}