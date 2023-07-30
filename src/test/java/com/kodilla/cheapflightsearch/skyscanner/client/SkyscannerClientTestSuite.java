package com.kodilla.cheapflightsearch.skyscanner.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kodilla.cheapflightsearch.domain.skyscanner.ItineraryDto;
import com.kodilla.cheapflightsearch.webclient.skyscanner.SkyscannerClient;
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

//    @Test
//    void testGetItinerariesV1() {
//        //Given
//        String jsonRequest = """
//                {
//                  "query": {
//                    "market": "PL",
//                    "locale": "pl-PL",
//                    "currency": "PLN",
//                    "adults": 1,
//                    "cabinClass": "CABIN_CLASS_ECONOMY",
//                    "queryLegs": [
//                        {
//                            "originPlaceId": {
//                                    "iata": "WMI"
//                            },
//                            "destinationPlaceId": {
//                                    "iata": "CFU"
//                            },
//                            "date": {
//                                "year": 2023,
//                                "month": 8,
//                                "day": 4
//                            }
//                        },
//                        {
//                            "originPlaceId": {
//                                    "iata": "CFU"
//                            },
//                            "destinationPlaceId": {
//                                    "iata": "WMI"
//                            },
//                            "date": {
//                                "year": 2023,
//                                "month": 8,
//                                "day": 6
//                            }
//                        }
//                    ],
//                    "includeCarriersIds": "ryan, wizz"
//                  }
//                }
//                """;
//
//        //When
//        String s = skyscannerClient.getItinerariesV1("/search/create", jsonRequest);
//
//        //Then
//        System.out.println(s);
//    }
    @Test
    void testGetItinerary() throws Exception {
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
        ItineraryDto s = skyscannerClient.getItinerary("/search/create", jsonRequest);

        //Then
        System.out.println(s);
    }
}