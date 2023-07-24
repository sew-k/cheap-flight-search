package com.kodilla.cheapflightsearch.mapper;

import com.kodilla.cheapflightsearch.domain.skyscanner.ItineraryDto;
import com.kodilla.cheapflightsearch.webclient.skyscanner.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SkyscannerMapperTestSuite {
    @Autowired
    SkyscannerMapper skyscannerMapper;
    @Test
    void shouldMapSkyscannerItineraryCreateDtoToItineraryDto() {
        //Given
        String json = """
                {
                     "pricingOptions": [
                         {
                             "price": {
                                 "amount": "817990",
                                 "unit": "PRICE_UNIT_MILLI",
                                 "updateStatus": "PRICE_UPDATE_STATUS_UNSPECIFIED"
                             },
                             "agentIds": [
                                 "mtpl"
                             ],
                             "items": [
                                 {
                                     "price": {
                                         "amount": "817990",
                                         "unit": "PRICE_UNIT_MILLI",
                                         "updateStatus": "PRICE_UPDATE_STATUS_UNSPECIFIED"
                                     },
                                     "agentId": "mtpl",
                                     "deepLink": "https://skyscanner.pxf.io/c/2850210/1103265/13416?u=https%3A%2F%2Fwww.skyscanner.pl%2Ftransport_deeplink%2F4.0%2FPL%2Fpl-PL%2FPLN%2Fmtpl%2F2%2F24405.10473.2023-08-04%2C10473.24405.2023-08-06%2Fair%2Ftrava%2Fflights%3Fitinerary%3Dflight%257C-31915%257C1697%257C24405%257C2023-08-04T12%253A00%257C10473%257C2023-08-04T15%253A20%257C140%257C-%257C-%257C-%252Cflight%257C-31915%257C1696%257C10473%257C2023-08-06T22%253A25%257C24405%257C2023-08-06T23%253A50%257C145%257C-%257C-%257C-%26carriers%3D-31915%26operators%3D-30821%252C-30821%26passengers%3D1%26channel%3Ddataapi%26cabin_class%3Deconomy%26fps_session_id%3D3f29096f-e5a3-4060-a68a-1c33ea01c60e%26ticket_price%3D817.99%26is_npt%3Dfalse%26is_multipart%3Dfalse%26client_id%3Dskyscanner_b2b%26request_id%3D2a38c4dd-fdbb-4dee-9437-f8bf2a402438%26q_ids%3DH4sIAAAAAAAA_-NS5GLJLSnIEWLhuLqPUYqZ42WgQsP_DcvYNBqO7l7GZsSkwAgAtWTIaCMAAAA%257C-8789201576928999815%257C2%26q_sources%3DJACQUARD%26commercial_filters%3Dfalse%26q_datetime_utc%3D2023-07-23T15%253A38%253A12%26pqid%3Dtrue%26api_logo%3Dhttps%253A%252F%252Flogos.skyscnr.com%252Fimages%252Fpartners%252Fdefault.png%26api_pbs%3Dtrue%26app_id%3D8CdRAjdoIPZ7vf%25252FMr2u7vmOc2%25252FPxu0y10PoO3nTomKERbn9Ld6H6QkxJI4f%25252BkMMp",
                                     "fares": [
                                         {
                                             "segmentId": "24405-10473-2308041200-2308041520--31915",
                                             "bookingCode": "",
                                             "fareBasisCode": ""
                                         },
                                         {
                                             "segmentId": "10473-24405-2308062225-2308062350--31915",
                                             "bookingCode": "",
                                             "fareBasisCode": ""
                                         }
                                     ]
                                 }
                             ],
                             "transferType": "TRANSFER_TYPE_MANAGED",
                             "id": "wTFUNFvHvc45",
                             "pricingOptionFare": null
                         }
                     ],
                     "legIds": [
                         "24405-2308041200--31915-0-10473-2308041520",
                         "10473-2308062225--31915-0-24405-2308062350"
                     ],
                     "sustainabilityData": "1"
                 }
                """;
        String itineraryId = "123456789";
        ContentResultsDto contentResultsDto = new ContentResultsDto();
        contentResultsDto.setItineraries(Map.of(itineraryId,json));
        SortingOptionBestDto sortingOptionBestDto = new SortingOptionBestDto();
        sortingOptionBestDto.setScore(1f);
        sortingOptionBestDto.setItineraryId("123456789");
        ContentSortingOptionsDto contentSortingOptionsDto = new ContentSortingOptionsDto();
        contentSortingOptionsDto.setBest(List.of(sortingOptionBestDto));
        CreateContentDto createContentDto = new CreateContentDto();
        createContentDto.setResults(contentResultsDto);
        createContentDto.setSortingOptions(contentSortingOptionsDto);
        SkyscannerItineraryCreateDto skyscannerItineraryCreateDto = new SkyscannerItineraryCreateDto();
        skyscannerItineraryCreateDto.setContent(createContentDto);

        //When
        ItineraryDto itineraryDto = skyscannerMapper.mapSkyscannerClientDtoToItineraryDto(skyscannerItineraryCreateDto);

        //Then
        assertEquals(itineraryId, itineraryDto.getItineraryId());
        assertEquals(817.990f, itineraryDto.getPrice());
        assertEquals("https://skyscanner.pxf.io/c/2850210/1103265/13416", itineraryDto.getPurchaseLink().substring(0,49));
    }
}
