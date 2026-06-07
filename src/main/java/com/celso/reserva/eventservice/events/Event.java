package com.celso.reserva.eventservice.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Event {

    private UUID id;

    private String name;

    private LocalDateTime date;

    private Integer capacity;

    private Integer halfPriceLimit;

    private String details;

    private String location;

    private BigDecimal ticketPrice;

    public Event() {
    }

    // getters / setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Integer getHalfPriceLimit() { return halfPriceLimit; }
    public void setHalfPriceLimit(Integer halfPriceLimit) { this.halfPriceLimit = halfPriceLimit; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public BigDecimal getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(BigDecimal ticketPrice) { this.ticketPrice = ticketPrice; }
}
