package com.celso.reserva.eventservice.controller;

import com.celso.reserva.eventservice.dto.EventDto;
import com.celso.reserva.eventservice.service.EventService;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService service;
    private final Logger log = LoggerFactory.getLogger(EventController.class);

    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Timed(value = "events.controller.create.latency", description = "Latency of event create endpoint")
  public ResponseEntity<EventDto> create(@Valid @RequestBody EventDto dto) {
        EventDto created = service.create(dto);
        log.info("event.created.controller id={}", created.getId());
        return ResponseEntity.created(URI.create("/events/" + created.getId())).body(created);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Timed(value = "events.controller.list.latency")
    public List<EventDto> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Timed(value = "events.controller.get.latency")
    public EventDto get(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Timed(value = "events.controller.update.latency")
   public EventDto update(@PathVariable UUID id, @Valid @RequestBody EventDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Timed(value = "events.controller.delete.latency")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}


