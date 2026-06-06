package com.celso.reserva.eventservice.repository;

import com.celso.reserva.eventservice.events.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}

