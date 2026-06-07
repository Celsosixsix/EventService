package com.celso.reserva.eventservice.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public class EventCreatedEvent {
    
    @JsonProperty("eventId")
    private String eventId;
    
    @JsonProperty("capacity")
    private Integer capacity;
    
    @JsonProperty("name")
    private String name;
    
    public EventCreatedEvent(String eventId, Integer capacity, String name) {
        this.eventId = eventId;
        this.capacity = capacity;
        this.name = name;
    }
    
    public EventCreatedEvent() {
    }
    
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

