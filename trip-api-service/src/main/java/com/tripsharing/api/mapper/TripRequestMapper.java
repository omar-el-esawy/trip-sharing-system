package com.tripsharing.api.mapper;

import com.client.generated.TripDTO;
import com.tripsharing.api.dto.TripRequestDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;

@Mapper
public interface TripRequestMapper {

    TripRequestMapper INSTANCE = Mappers.getMapper(TripRequestMapper.class);

    @Mapping(target = "startTime", expression = "java(toXmlGregorianCalendar(dto.getStartTime()))")
    TripDTO toTripDTO(TripRequestDTO dto);

    // Helper for LocalDateTime -> XMLGregorianCalendar
    default XMLGregorianCalendar toXmlGregorianCalendar(java.time.LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        GregorianCalendar gc = GregorianCalendar.from(zdt);
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
