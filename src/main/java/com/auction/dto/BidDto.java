package com.auction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BidDto {
    @JsonProperty("bidder_name")
    private String bidderName;
    private String date;
}
