package com.djk.tickets;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SeatHold {
    private int id;
    private long expiration;
    private Set<Seat> seats = new HashSet<Seat>();

    public SeatHold(int id, Set<Seat> seats) {
        this.id = id;
        this.seats.addAll(seats);
    }

    public int getId() {
        return id;
    }

    public Set<Seat> getSeats() {
        return Collections.unmodifiableSet(seats);
    }
}
