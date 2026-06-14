package com.celso.reserva.eventservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class EventDto {

	private UUID id;

	@NotBlank(message = "name e obrigatorio")
	private String name;

	@NotNull(message = "date e obrigatoria")
	@Future(message = "date deve ser no futuro")
	private LocalDateTime date;

	@NotNull(message = "capacity e obrigatoria")
	@Min(value = 1, message = "capacity deve ser pelo menos 1")
	private Integer capacity;

	@PositiveOrZero(message = "halfPriceLimit nao pode ser negativo")
	private Integer halfPriceLimit;

	private String details;

	@NotBlank(message = "location e obrigatorio")
	private String location;

	@NotNull(message = "ticketPrice e obrigatorio")
	@PositiveOrZero(message = "ticketPrice nao pode ser negativo")
	private BigDecimal ticketPrice;

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