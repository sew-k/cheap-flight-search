package com.kodilla.cheapflightsearch.skyscanner.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ExtendWith(SpringExtension.class)
class SkyscannerClientTestSuite {
    @Autowired
    SkyscannerClient skyscannerClient;

    @Test
    void testGetItineraries() {
        String s = skyscannerClient.getItineraries();
        System.out.println(s);
    }
}