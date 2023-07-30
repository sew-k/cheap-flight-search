package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.controller.ItineraryNotFoundException;
import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;
import com.kodilla.cheapflightsearch.domain.trip.Airport;

import com.kodilla.cheapflightsearch.repository.ItineraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItineraryService {
    private final ItineraryRepository itineraryRepository;
    public List<Itinerary> getRoutes() {
        return itineraryRepository.findAll();
    }
    public Itinerary getRoute(Long id) throws ItineraryNotFoundException {
        return itineraryRepository.findById(id).orElseThrow(ItineraryNotFoundException::new);
    }
    public void deleteItinerary(Long id) throws ItineraryNotFoundException {
        if(itineraryRepository.findById(id).isPresent()) {
            itineraryRepository.deleteById(id);
        } else {
            throw new ItineraryNotFoundException();
        }
    }
    public Itinerary updateItinerary(Long id, Itinerary itinerary) throws ItineraryNotFoundException {
        if(itineraryRepository.findById(id).isPresent()) {
            return itineraryRepository.save(itinerary);
        } else {
            throw new ItineraryNotFoundException();
        }
    }
    public Itinerary createItinerary(Itinerary itinerary) {
        return itineraryRepository.save(itinerary);
    }

    public List<Itinerary> getPurchasedItineraries() {
        return itineraryRepository.findAll().stream()
                .filter(Itinerary::isPurchased)
                .collect(Collectors.toList());
    }
}
