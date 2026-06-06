package com.celso.reserva.eventservice.controller;

import com.celso.reserva.eventservice.dto.EventDto;
import com.celso.reserva.eventservice.service.EventService;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService service;
    private final Logger log = LoggerFactory.getLogger(EventController.class);

    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping
    @Timed(value = "events.controller.create.latency", description = "Latency of event create endpoint")
    public ResponseEntity<EventDto> create(@RequestBody EventDto dto) {
        EventDto created = service.create(dto);
        log.info("event.created.controller id={}", created.getId());
        return ResponseEntity.created(URI.create("/events/" + created.getId())).body(created);
    }

    @GetMapping
    @Timed(value = "events.controller.list.latency")
    public List<EventDto> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    @Timed(value = "events.controller.get.latency")
    public EventDto get(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    @Timed(value = "events.controller.update.latency")
    public EventDto update(@PathVariable Long id, @RequestBody EventDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Timed(value = "events.controller.delete.latency")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}


