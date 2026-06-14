package com.celso.reserva.eventservice.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public class EventCreatedEvent {
    
    @JsonProperty("eventId")
    private String eventId;
    
    @JsonProperty("capacity")
    private Integer capacity;
    
    @JsonProperty("ticketPrice")
    private String ticketPrice;
    
    public EventCreatedEvent(String eventId, Integer capacity, String ticketPrice) {
        this.eventId = eventId;
        this.capacity = capacity;
        this.ticketPrice = ticketPrice;
    }
    
    public EventCreatedEvent() {
    }
    
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    
    public String getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(String ticketPrice) { this.ticketPrice = ticketPrice; }
}

