package com.kodilla.cheapflightsearch.skyscanner.client;

import com.kodilla.cheapflightsearch.webclient.skyscanner.SkyscannerClient;
import com.kodilla.cheapflightsearch.webclient.skyscanner.SkyscannerItineraryCreateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class SkyscannerClientTestSuite {
    @Autowired
    SkyscannerClient skyscannerClient;

    @Test
    void testGetItinerariesV1() {
        //Given
        String jsonRequest = """
                {
                  "query": {
                    "market": "PL",
                    "locale": "pl-PL",
                    "currency": "PLN",
                    "adults": 1,
                    "cabinClass": "CABIN_CLASS_ECONOMY",
                    "queryLegs": [
                        {
                            "originPlaceId": {
                                    "iata": "WMI"
                            },
                            "destinationPlaceId": {
                                    "iata": "CFU"
                            },
                            "date": {
                                "year": 2023,
                                "month": 8,
                                "day": 4
                            }
                        },
                        {
                            "originPlaceId": {
                                    "iata": "CFU"
                            },
                            "destinationPlaceId": {
                                    "iata": "WMI"
                            },
                            "date": {
                                "year": 2023,
                                "month": 8,
                                "day": 6
                            }
                        }
                    ],
                    "includeCarriersIds": "ryan, wizz"
                  }
                }
                """;

        //When
        String s = skyscannerClient.getItinerariesV1("/search/create", jsonRequest);
//        ItineraryDto s = skyscannerClient.getItineraries("/search/create", jsonRequest);

        //Then
        System.out.println(s);
    }
    @Test
    void testGetItinerariesV2() {
        //Given
        String jsonRequest = """
                {
                  "query": {
                    "market": "PL",
                    "locale": "pl-PL",
                    "currency": "PLN",
                    "adults": 1,
                    "cabinClass": "CABIN_CLASS_ECONOMY",
                    "queryLegs": [
                        {
                            "originPlaceId": {
                                    "iata": "WMI"
                            },
                            "destinationPlaceId": {
                                    "iata": "CFU"
                            },
                            "date": {
                                "year": 2023,
                                "month": 8,
                                "day": 4
                            }
                        },
                        {
                            "originPlaceId": {
                                    "iata": "CFU"
                            },
                            "destinationPlaceId": {
                                    "iata": "WMI"
                            },
                            "date": {
                                "year": 2023,
                                "month": 8,
                                "day": 6
                            }
                        }
                    ],
                    "includeCarriersIds": "ryan, wizz"
                  }
                }
                """;

        //When
        SkyscannerItineraryCreateDto s = skyscannerClient.getItinerariesV2("/search/create", jsonRequest);
//        ItineraryDto s = skyscannerClient.getItineraries("/search/create", jsonRequest);

        //Then
        System.out.println(s);
    }
}