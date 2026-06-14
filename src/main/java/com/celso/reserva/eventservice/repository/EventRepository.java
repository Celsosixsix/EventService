package com.celso.reserva.eventservice.repository;

import com.celso.reserva.eventservice.events.Event;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface EventRepository {
    int insert(Event event);

    Optional<Event> findById(UUID id);

    List<Event> findAll();

    int update(Event event);

    int deleteById(UUID id);
}

