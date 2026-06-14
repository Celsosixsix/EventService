package com.celso.reserva.eventservice.service;

import com.celso.reserva.eventservice.dto.EventDto;
import com.celso.reserva.eventservice.event.EventCreatedEvent;
import com.celso.reserva.eventservice.events.Event;
import com.celso.reserva.eventservice.exception.NotFoundException;
import com.celso.reserva.eventservice.repository.EventRepository;
import com.fasterxml.uuid.Generators;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository repository;
    private final MeterRegistry meterRegistry;
    private final Timer requestTimer;
    private final Counter errorCounter;
    private final Logger log = LoggerFactory.getLogger(EventService.class);
    private final SqsTemplate sqsTemplate;
    private final String queueName;

    public EventService(EventRepository repository, MeterRegistry meterRegistry, SqsTemplate sqsTemplate,
                       @Value("${aws.sqs.queue-name:fila-evento-cadastrado}") String queueName) {
        this.repository = repository;
        this.meterRegistry = meterRegistry;
        this.sqsTemplate = sqsTemplate;
        this.queueName = queueName;
        
        this.requestTimer = Timer.builder("events.requests.latency")
                .description("Latency of event service requests")
                .publishPercentileHistogram()
                .minimumExpectedValue(Duration.ofMillis(1))
                .maximumExpectedValue(Duration.ofSeconds(30))
                .register(meterRegistry);

        this.errorCounter = Counter.builder("events.requests.errors")
                .description("Number of errors in event service")
                .register(meterRegistry);
    }

    public EventDto create(EventDto dto) {
        return requestTimer.record(() -> {
            try {
                Event e = dtoToEntity(dto);
                // Generate UUID for new event (time-based epoch generator)
                e.setId(Generators.timeBasedEpochGenerator().generate());
                repository.insert(e);
                log.info("event.created id={}", e.getId());
                // publish to SQS (best-effort non-blocking)
                try {
                    publishEventCreated(e.getId());
                } catch (Exception ex) {
                    log.warn("Failed to publish event to SQS: {}", ex.getMessage());
                }
                return entityToDto(e);
            } catch (Exception ex) {
                errorCounter.increment();
                throw ex;
            }
        });
    }

    /**
     * Publishes an event creation message to SQS queue.
     * Follows the same pattern as OrderService: creates EventCreatedEvent and sends via SqsTemplate.
     */
    public void publishEventCreated(UUID eventId) {
        Event event = repository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found: " + eventId));
        
        // Build event payload
        EventCreatedEvent eventCreatedEvent = new EventCreatedEvent(
                eventId.toString(),
                event.getCapacity() == null ? 0 : event.getCapacity(),
                event.getTicketPrice().toString()
        );
        
        // Send to SQS using SqsTemplate (similar to OrderService pattern)
        sqsTemplate.send(to -> to
                .queue(queueName)
                .payload(eventCreatedEvent)
                .header("MessageGroupId", eventId.toString())
                .header("MessageDeduplicationId", eventId.toString())
        );
        
        log.info("Published EventCreatedEvent to SQS queue {}: eventId={}, capacity={}", 
                 queueName, eventId, event.getCapacity());
    }

    public EventDto getById(UUID id) {
        return requestTimer.record(() -> {
            Optional<Event> e = repository.findById(id);
            if (e.isEmpty()) {
                errorCounter.increment();
                throw new NotFoundException("Event not found: " + id);
            }
            return entityToDto(e.get());
        });
    }

    public List<EventDto> list() {
        return requestTimer.record(() ->
                repository.findAll().stream().map(this::entityToDto).collect(Collectors.toList())
        );
    }

    public EventDto update(UUID id, EventDto dto) {
        return requestTimer.record(() -> {
            Event e = repository.findById(id).orElseThrow(() -> new NotFoundException("Event not found: " + id));
            e.setName(dto.getName());
            e.setDate(dto.getDate());
            e.setCapacity(dto.getCapacity());
            e.setHalfPriceLimit(dto.getHalfPriceLimit());
            e.setDetails(dto.getDetails());
            e.setLocation(dto.getLocation());
            e.setTicketPrice(dto.getTicketPrice());
            repository.update(e);
            return entityToDto(e);
        });
    }

    public void delete(UUID id) {
        requestTimer.record(() -> {
            try {
                repository.deleteById(id);
            } catch (Exception ex) {
                errorCounter.increment();
                throw ex;
            }
        });
    }

    // helpers
    private Event dtoToEntity(EventDto dto) {
        Event e = new Event();
        e.setName(dto.getName());
        e.setDate(dto.getDate());
        e.setCapacity(dto.getCapacity());
        e.setHalfPriceLimit(dto.getHalfPriceLimit());
        e.setDetails(dto.getDetails());
        e.setLocation(dto.getLocation());
        e.setTicketPrice(dto.getTicketPrice());
        return e;
    }

    private EventDto entityToDto(Event e) {
        EventDto dto = new EventDto();
        dto.setId(e.getId());
        dto.setName(e.getName());
        dto.setDate(e.getDate());
        dto.setCapacity(e.getCapacity());
        dto.setHalfPriceLimit(e.getHalfPriceLimit());
        dto.setDetails(e.getDetails());
        dto.setLocation(e.getLocation());
        dto.setTicketPrice(e.getTicketPrice());
        return dto;
    }
}



