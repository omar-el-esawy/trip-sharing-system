package com.example.trip.service;

import com.example.trip.TripDTO;
import com.example.trip.repository.AerospikeTripRepository;
import com.example.trip.kafka.KafkaProducerUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatchingAlgoServiceTest {

    private AerospikeTripRepository tripRepository;
    private MatchingAlgoService matchingAlgoService;

    @BeforeEach
    void setUp() {
        tripRepository = mock(AerospikeTripRepository.class);
        matchingAlgoService = new MatchingAlgoService(tripRepository);
    }

    private TripDTO createTrip(String tripId, double lat, double lng, LocalDateTime startTime, boolean matched, String email) throws Exception {
        TripDTO trip = new TripDTO();
        trip.setTripId(tripId);
        trip.setStartLat(lat);
        trip.setStartLng(lng);
        trip.setStartTime(toXmlGregorianCalendar(startTime));
        trip.setMatched(matched);
        trip.setUserEmail(email);
        return trip;
    }

    private XMLGregorianCalendar toXmlGregorianCalendar(LocalDateTime ldt) throws Exception {
        GregorianCalendar gc = GregorianCalendar.from(ldt.atZone(ZoneId.systemDefault()));
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
    }

    @Test
    void testIsMatch_true_whenCloseInTimeAndDistance() throws Exception {
        TripDTO a = createTrip("a", 1.0, 1.0, LocalDateTime.now(), false, "a@email.com");
        TripDTO b = createTrip("b", 1.1, 1.1, LocalDateTime.now().plusMinutes(5), false, "b@email.com");

        var method = MatchingAlgoService.class.getDeclaredMethod("isMatch", TripDTO.class, TripDTO.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(matchingAlgoService, a, b);
        assertTrue(result);
    }


    @Test
    void testIsMatch_false_whenFarInDistance() throws Exception {
        TripDTO a = createTrip("a", 1.0, 1.0, LocalDateTime.now(), false, "a@email.com");
        TripDTO b = createTrip("b", 10.0, 10.0, LocalDateTime.now().plusMinutes(5), false, "b@email.com");

        var method = MatchingAlgoService.class.getDeclaredMethod("isMatch", TripDTO.class, TripDTO.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(matchingAlgoService, a, b);
        assertFalse(result);
    }

    @Test
    void testIsMatch_false_whenFarInTime() throws Exception {
        TripDTO a = createTrip("a", 1.0, 1.0, LocalDateTime.now(), false, "a@email.com");
        TripDTO b = createTrip("b", 1.0, 1.0, LocalDateTime.now().plusHours(2), false, "b@email.com");

        var method = MatchingAlgoService.class.getDeclaredMethod("isMatch", TripDTO.class, TripDTO.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(matchingAlgoService, a, b);
        assertFalse(result);
    }

    @Test
    void testDistance() throws Exception {
        var method = MatchingAlgoService.class.getDeclaredMethod("distance", double.class, double.class, double.class, double.class);
        method.setAccessible(true);

        double d = (double) method.invoke(matchingAlgoService, 0, 0, 5, 0);



        assertEquals(5.0, d, 0.0001);
    }

    @Test
    void testMatchTrip_findsMatchAndMarksAsMatched() throws Exception {
        String tripId = "t1";
        TripDTO newTrip = createTrip(tripId, 1.0, 1.0, LocalDateTime.now(), false, "a@email.com");
        TripDTO candidate = createTrip("t2", 1.01, 1.01, LocalDateTime.now().plusMinutes(2), false, "b@email.com");

        when(tripRepository.findById(tripId)).thenReturn(newTrip);
        when(tripRepository.findAllUnmatchedExcluding(tripId)).thenReturn(Collections.singletonList(candidate));

        try (MockedStatic<KafkaProducerUtil> kafkaMock = mockStatic(KafkaProducerUtil.class)) {
            matchingAlgoService.matchTrip(tripId);

            verify(tripRepository).markAsMatched(tripId);
            verify(tripRepository).markAsMatched(candidate.getTripId());
            kafkaMock.verify(() -> KafkaProducerUtil.sendTripMatchedEvent("a@email.com"));
            kafkaMock.verify(() -> KafkaProducerUtil.sendTripMatchedEvent("b@email.com"));
        }
    }

    @Test
    void testMatchTrip_noMatchFound() throws Exception {
        String tripId = "t1";
        TripDTO newTrip = createTrip(tripId, 1.0, 1.0, LocalDateTime.now(), false, "a@email.com");
        TripDTO candidate = createTrip("t2", 10.0, 10.0, LocalDateTime.now(), false, "b@email.com");

        when(tripRepository.findById(tripId)).thenReturn(newTrip);
        when(tripRepository.findAllUnmatchedExcluding(tripId)).thenReturn(Collections.singletonList(candidate));

        try (MockedStatic<KafkaProducerUtil> kafkaMock = mockStatic(KafkaProducerUtil.class)) {
            matchingAlgoService.matchTrip(tripId);

            verify(tripRepository, never()).markAsMatched(anyString());
            kafkaMock.verifyNoInteractions();
        }
    }

    @Test
    void testMatchTrip_tripNotFoundOrAlreadyMatched() {
        String tripId = "t1";
        when(tripRepository.findById(tripId)).thenReturn(null);

        matchingAlgoService.matchTrip(tripId);
        verify(tripRepository, never()).findAllUnmatchedExcluding(anyString());

        TripDTO matchedTrip = mock(TripDTO.class);
        when(tripRepository.findById(tripId)).thenReturn(matchedTrip);
        when(matchedTrip.isMatched()).thenReturn(true);

        matchingAlgoService.matchTrip(tripId);
        verify(tripRepository, never()).findAllUnmatchedExcluding(anyString());
    }
}
