package com.djk.tickets;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashSet;

public class SeatHoldTest {

    @Test
    public void test() {
        HashSet<Seat> seats = new HashSet<Seat>(1);

        Seat seat = Mockito.mock(Seat.class);
        seats.add(seat);

        SeatHold hold = new SeatHold(3, seats);

        Assert.assertEquals(3, hold.getId());
        Assert.assertEquals(seats, hold.getSeats());
    }
}
