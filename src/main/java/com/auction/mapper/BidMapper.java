package com.auction.mapper;

import com.auction.dto.BidDto;
import com.auction.model.Bid;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
@Component
public interface BidMapper {
    @Mapping(target = "date", expression = "java(parseDate(bidDto.getDate()))")
    Bid toEntity(BidDto bidDto);

    BidDto toDto(Bid bid);

    default LocalDate parseDate(String date) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(date, df);
    }

}