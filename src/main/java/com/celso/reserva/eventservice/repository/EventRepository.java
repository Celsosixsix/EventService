package com.celso.reserva.eventservice.repository;

import com.celso.reserva.eventservice.events.Event;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface EventRepository {

    @Insert("INSERT INTO events (id, name, date, capacity, half_price_limit, details, location, ticket_price) " +
            "VALUES (#{id}, #{name}, #{date}, #{capacity}, #{halfPriceLimit}, #{details}, #{location}, #{ticketPrice})")
    int insert(Event event);

    @Select("SELECT id, name, date, capacity, half_price_limit as halfPriceLimit, details, location, ticket_price as ticketPrice " +
            "FROM events WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "date", column = "date"),
        @Result(property = "capacity", column = "capacity"),
        @Result(property = "halfPriceLimit", column = "half_price_limit"),
        @Result(property = "details", column = "details"),
        @Result(property = "location", column = "location"),
        @Result(property = "ticketPrice", column = "ticket_price")
    })
    Optional<Event> findById(UUID id);

    @Select("SELECT id, name, date, capacity, half_price_limit as halfPriceLimit, details, location, ticket_price as ticketPrice " +
            "FROM events")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "date", column = "date"),
        @Result(property = "capacity", column = "capacity"),
        @Result(property = "halfPriceLimit", column = "half_price_limit"),
        @Result(property = "details", column = "details"),
        @Result(property = "location", column = "location"),
        @Result(property = "ticketPrice", column = "ticket_price")
    })
    List<Event> findAll();

    @Update("UPDATE events SET name = #{name}, date = #{date}, capacity = #{capacity}, " +
            "half_price_limit = #{halfPriceLimit}, details = #{details}, location = #{location}, " +
            "ticket_price = #{ticketPrice} WHERE id = #{id}")
    int update(Event event);

    @Delete("DELETE FROM events WHERE id = #{id}")
    int deleteById(UUID id);
}

