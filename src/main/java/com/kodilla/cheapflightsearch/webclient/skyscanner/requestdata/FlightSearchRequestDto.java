package com.kodilla.cheapflightsearch.webclient.skyscanner.requestdata;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class FlightSearchRequestDto {
    private Query query;
    public FlightSearchRequestDto(int adults, String originIata, String destinationIata, LocalDate beginDate, LocalDate endDate) {
        this.query = new Query(
                "PL",
                "pl-PL",
                "PLN",
                adults,
                "CABIN_CLASS_ECONOMY",
                List.of(
                        new QueryLeg(
                                new OriginPlaceId(originIata),
                                new DestinationPlaceId(destinationIata),
                                new Date(
                                        beginDate.getYear(),
                                        beginDate.getMonthValue(),
                                        beginDate.getDayOfMonth()
                                )
                        ),
                        new QueryLeg(
                                new OriginPlaceId(destinationIata),
                                new DestinationPlaceId(originIata),
                                new Date(
                                        endDate.getYear(),
                                        endDate.getMonthValue(),
                                        endDate.getDayOfMonth()
                                )
                        )
                ),
                "ryan, wizz"
        );
    }
}
