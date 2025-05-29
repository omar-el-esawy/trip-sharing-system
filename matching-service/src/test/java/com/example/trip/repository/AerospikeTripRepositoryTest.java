package com.example.trip.repository;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.example.trip.TripDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AerospikeTripRepositoryTest {

    private AerospikeClient mockClient;
    private AerospikeTripRepository repository;

    @BeforeEach
    void setUp() {
        mockClient = mock(AerospikeClient.class);
        repository = new AerospikeTripRepository() {
            {
                // Use reflection to set the private 'client' field for testing
                try {
                    java.lang.reflect.Field clientField = AerospikeTripRepository.class.getDeclaredField("client");
                    clientField.setAccessible(true);
                    clientField.set(this, mockClient);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @Test
    void generateTripId_shouldReturnUniqueId() {
        String id1 = repository.generateTripId();
        String id2 = repository.generateTripId();
        assertNotEquals(id1, id2);
        assertTrue(id1.startsWith("TRIP"));
        assertTrue(id2.startsWith("TRIP"));
    }

    @Test
    void save_shouldStoreTripAndReturnIt() throws Exception {
        TripDTO trip = new TripDTO();
        trip.setUserId("user1");
        trip.setUserEmail("user1@example.com");
        trip.setStartLat(1.0);
        trip.setStartLng(2.0);
        trip.setEndLat(3.0);
        trip.setEndLng(4.0);
        trip.setTravelMode("CAR");
        XMLGregorianCalendar now = DatatypeFactory.newInstance().newXMLGregorianCalendar(ZonedDateTime.now().toString());
        trip.setStartTime(now);
        trip.setMatched(false);

        TripDTO result = repository.save(trip);

        assertNotNull(result.getTripId());
        ArgumentCaptor<Key> keyCaptor = ArgumentCaptor.forClass(Key.class);
        verify(mockClient).put(any(), keyCaptor.capture(), any(Bin.class), any(Bin.class), any(Bin.class), any(Bin.class), any(Bin.class), any(Bin.class), any(Bin.class), any(Bin.class), any(Bin.class), any(Bin.class));
        assertEquals(result.getTripId(), keyCaptor.getValue().userKey.toString());
    }

    @Test
    void markAsMatched_shouldUpdateMatchedBin() {
        String tripId = "TRIP123";
        repository.markAsMatched(tripId);
        ArgumentCaptor<Key> keyCaptor = ArgumentCaptor.forClass(Key.class);
        ArgumentCaptor<Bin> binCaptor = ArgumentCaptor.forClass(Bin.class);
        verify(mockClient).put(isNull(), keyCaptor.capture(), binCaptor.capture());
        assertEquals(tripId, keyCaptor.getValue().userKey.toString());
        assertEquals("matched", binCaptor.getValue().name);
        assertEquals(true, binCaptor.getValue().value.getObject());
    }

    @Test
    void findById_shouldReturnTripIfExists() throws Exception {
        String tripId = "TRIP123";
        Record mockRecord = mock(Record.class);
        when(mockRecord.getString("userId")).thenReturn("user1");
        when(mockRecord.getString("userEmail")).thenReturn("user1@example.com");
        when(mockRecord.getDouble("startLat")).thenReturn(1.0);
        when(mockRecord.getDouble("startLng")).thenReturn(2.0);
        when(mockRecord.getDouble("endLat")).thenReturn(3.0);
        when(mockRecord.getDouble("endLng")).thenReturn(4.0);
        when(mockRecord.getString("travelMode")).thenReturn("CAR");
        when(mockRecord.getString("startTime")).thenReturn(ZonedDateTime.now().toString());
        when(mockRecord.getBoolean("matched")).thenReturn(false);

        when(mockClient.get(isNull(), any(Key.class))).thenReturn(mockRecord);

        TripDTO trip = repository.findById(tripId);

        assertNotNull(trip);
        assertEquals(tripId, trip.getTripId());
        assertEquals("user1", trip.getUserId());
        assertEquals("user1@example.com", trip.getUserEmail());
        assertEquals(1.0, trip.getStartLat());
        assertEquals(2.0, trip.getStartLng());
        assertEquals(3.0, trip.getEndLat());
        assertEquals(4.0, trip.getEndLng());
        assertEquals("CAR", trip.getTravelMode());
        assertNotNull(trip.getStartTime());
        assertFalse(trip.isMatched());
    }

    @Test
    void findById_shouldReturnNullIfNotExists() {
        when(mockClient.get(isNull(), any(Key.class))).thenReturn(null);
        TripDTO trip = repository.findById("TRIP999");
        assertNull(trip);
    }

    @Test
    void update_shouldCallSave() throws Exception {
        TripDTO trip = new TripDTO();
        trip.setUserId("user2");
        trip.setUserEmail("user2@example.com");
        trip.setStartLat(5.0);
        trip.setStartLng(6.0);
        trip.setEndLat(7.0);
        trip.setEndLng(8.0);
        trip.setTravelMode("BUS");
        GregorianCalendar gc = GregorianCalendar.from(ZonedDateTime.now());
        XMLGregorianCalendar now =
                DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);

        trip.setStartTime(now);
        trip.setMatched(true);

        TripDTO result = repository.update(trip);

        assertNotNull(result.getTripId());
        verify(mockClient).put(any(), any(Key.class), any(Bin.class), any(Bin.class), any(Bin.class), any(Bin.class), any(Bin.class), any(Bin.class), any(Bin.class), any(Bin.class), any(Bin.class), any(Bin.class));
    }

    @Test
    void delete_shouldCallClientDelete() {
        String tripId = "TRIPDEL";
        repository.delete(tripId);
        ArgumentCaptor<Key> keyCaptor = ArgumentCaptor.forClass(Key.class);
        verify(mockClient).delete(isNull(), keyCaptor.capture());
        assertEquals(tripId, keyCaptor.getValue().userKey.toString());
    }

    // Note: findAllUnmatchedExcluding is hard to test without a real Aerospike instance or advanced mocking.
    // You may use integration tests for that method if needed.
}
