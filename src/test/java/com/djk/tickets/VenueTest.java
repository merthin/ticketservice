package com.djk.tickets;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class VenueTest {

    private final class MyVenue extends Venue {
        boolean holdReleased;

        public MyVenue() {
            super(500);
        }

        protected SeatHold holdSeats(int numSeats, String customerEmail) {
            return hold;
        }

        public void unholdSeats(SeatHold hold) {
            holdReleased = true;
        }

        public int numSeatsAvailable() {
            return 0;
        }
    }

    private SeatHold hold = Mockito.mock(SeatHold.class);

    private MyVenue venue;

    @Before
    public void before(){
        venue = new MyVenue();

        Mockito.when(hold.getId()).thenReturn(3);
    }

    @Test
    public void testExpiration() throws InterruptedException {
        venue.findAndHoldSeats(1, "");
        Thread.sleep(1000);
        Assert.assertTrue(venue.holdReleased);
    }

    @Test
    public void testReserve() throws InterruptedException {
        SeatHold hold = venue.findAndHoldSeats(1, "");
        venue.reserveSeats(hold.getId(), "");
        Thread.sleep(1000);
        Assert.assertFalse(venue.holdReleased);
    }

    @Test
    public void testReservedTooLate() throws InterruptedException {
        SeatHold hold = venue.findAndHoldSeats(1, "");
        Thread.sleep(1000);
        Assert.assertNull(venue.reserveSeats(hold.getId(), ""));
        Assert.assertTrue(venue.holdReleased);
    }
}
