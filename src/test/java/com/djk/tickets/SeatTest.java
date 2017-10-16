package com.djk.tickets;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SeatTest {

    private Seat seat;

    @Before
    public void before() {
        seat = new Seat(4,5);
    }

    @Test
    public void testGetters(){
        Assert.assertEquals(4, seat.getRow());
        Assert.assertEquals(5, seat.getCol());
    }

    @Test
    public void testEquals() {
        Assert.assertTrue(seat.equals(seat));
        Assert.assertFalse(seat.equals(null));
        Assert.assertFalse(seat.equals("not a seat"));

        Seat seat2 = new Seat(3,5);
        Assert.assertFalse(seat.equals(seat2));

        seat2 = new Seat(4,6);
        Assert.assertFalse(seat.equals(seat2));

        seat2 = new Seat(4,5);
        Assert.assertTrue(seat.equals(seat2));
    }

    @Test
    public void testHashcode() {
        Seat seat2 = new Seat(4,5);
        Assert.assertTrue(seat.equals(seat2));
        Assert.assertEquals(seat.hashCode(), seat2.hashCode());
    }
}
