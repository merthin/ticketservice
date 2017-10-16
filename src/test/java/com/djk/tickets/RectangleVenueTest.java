package com.djk.tickets;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

public class RectangleVenueTest {

    private RectangleVenue rectangle;

    @Before
    public void before() {
        rectangle = new RectangleVenue(3, 5);
    }

    @Test
    public void testFirstHoldSizeOne() {
        SeatHold hold = rectangle.holdSeats(1, "email");
        Set<Seat> seats = hold.getSeats();
        Assert.assertEquals(1, seats.size());
        Assert.assertEquals(0, seats.iterator().next().getRow());
        Assert.assertEquals(2, seats.iterator().next().getCol());
        Assert.assertEquals(14, rectangle.numSeatsAvailable());

        hold = rectangle.holdSeats(1, "email");
        seats = hold.getSeats();
        Assert.assertEquals(1, seats.size());
        Assert.assertEquals(0, seats.iterator().next().getRow());
        Assert.assertEquals(1, seats.iterator().next().getCol());
        Assert.assertEquals(13, rectangle.numSeatsAvailable());

        hold = rectangle.holdSeats(1, "email");
        seats = hold.getSeats();
        Assert.assertEquals(1, seats.size());
        Assert.assertEquals(0, seats.iterator().next().getRow());
        Assert.assertEquals(3, seats.iterator().next().getCol());
        Assert.assertEquals(12, rectangle.numSeatsAvailable());

        hold = rectangle.holdSeats(1, "email");
        seats = hold.getSeats();
        Assert.assertEquals(1, seats.size());
        Assert.assertEquals(1, seats.iterator().next().getRow());
        Assert.assertEquals(2, seats.iterator().next().getCol());
        Assert.assertEquals(11, rectangle.numSeatsAvailable());

        hold = rectangle.holdSeats(1, "email");
        seats = hold.getSeats();
        Assert.assertEquals(1, seats.size());
        Assert.assertEquals(0, seats.iterator().next().getRow());
        Assert.assertEquals(0, seats.iterator().next().getCol());
        Assert.assertEquals(10, rectangle.numSeatsAvailable());
    }

    @Test
    public void testUnhold() {
        SeatHold hold1 = rectangle.holdSeats(1, "email");
        Set<Seat> seats1 = hold1.getSeats();

        rectangle.unholdSeats(hold1);

        SeatHold hold2 = rectangle.holdSeats(1, "email");
        Set<Seat> seats2 = hold2.getSeats();

        Assert.assertEquals(seats1.iterator().next().getRow(), seats2.iterator().next().getRow());
        Assert.assertEquals(seats1.iterator().next().getCol(), seats2.iterator().next().getCol());
    }

    @Test
    public void testHoldTwoSeats() {
        SeatHold hold = rectangle.holdSeats(1, "email");
        Set<Seat> seats = hold.getSeats();
        Assert.assertEquals(1, seats.size());

        hold = rectangle.holdSeats(2, "email");
        seats = hold.getSeats();
        Assert.assertEquals(2, seats.size());

        Assert.assertTrue(seats.contains(new Seat(0, 1)));
        Assert.assertTrue(seats.contains(new Seat(1, 1)));

        Assert.assertEquals(12, rectangle.numSeatsAvailable());
    }

    @Test
    public void testFirstGroupTooSmall() {
        SeatHold hold1 = rectangle.holdSeats(1, "email");
        SeatHold hold2 = rectangle.holdSeats(1, "email");
        SeatHold hold3 = rectangle.holdSeats(1, "email");
        SeatHold hold4 = rectangle.holdSeats(1, "email");

        rectangle.unholdSeats(hold1);

        SeatHold hold5 = rectangle.holdSeats(2, "email");

        Set<Seat> seats = hold5.getSeats();
        Assert.assertEquals(2, seats.size());

        Assert.assertTrue(seats.contains(new Seat(0, 0)));
        Assert.assertTrue(seats.contains(new Seat(1, 0)));

        Assert.assertEquals(10, rectangle.numSeatsAvailable());
    }

    @Test
    public void testFirstHoldSizeThree() {
        SeatHold hold = rectangle.holdSeats(3, "email");
        Set<Seat> seats = hold.getSeats();
        Assert.assertEquals(3, seats.size());

        Assert.assertTrue(seats.contains(new Seat(0, 2)));
        Assert.assertTrue(seats.contains(new Seat(0, 1)));
        Assert.assertTrue(seats.contains(new Seat(0, 3)));
    }

    @Test
    public void testFirstHoldSizeSix() {
        SeatHold hold = rectangle.holdSeats(6, "email");
        Set<Seat> seats = hold.getSeats();
        Assert.assertEquals(6, seats.size());
    }

    @Test
    public void testLeftSideThenRightSide() {
        SeatHold hold = rectangle.holdSeats(4, "email");

        hold = rectangle.holdSeats(4, "email");
        Set<Seat> seats = hold.getSeats();
        Assert.assertEquals(4, seats.size());

        Assert.assertTrue(seats.contains(new Seat(0, 0)));
        Assert.assertTrue(seats.contains(new Seat(1, 0)));
        Assert.assertTrue(seats.contains(new Seat(1, 1)));
        Assert.assertTrue(seats.contains(new Seat(2, 1)));

        hold = rectangle.holdSeats(5, "email");
        seats = hold.getSeats();
        Assert.assertEquals(5, seats.size());

        Assert.assertTrue(seats.contains(new Seat(0, 4)));
        Assert.assertTrue(seats.contains(new Seat(1, 4)));
        Assert.assertTrue(seats.contains(new Seat(1, 3)));
        Assert.assertTrue(seats.contains(new Seat(2, 3)));
        Assert.assertTrue(seats.contains(new Seat(2, 2)));
    }

    @Test
    public void testNoRoomForHold() {
        Assert.assertNull(rectangle.holdSeats(16, "email"));
        Assert.assertNotNull(rectangle.holdSeats(15, "email"));
    }

    @Test
    public void testSingleRow() {
        rectangle = new RectangleVenue(1, 6);

        SeatHold hold = rectangle.holdSeats(4, "email");
        Set<Seat> seats = hold.getSeats();
        Assert.assertEquals(4, seats.size());

        Assert.assertTrue(seats.contains(new Seat(0, 1)));
        Assert.assertTrue(seats.contains(new Seat(0, 2)));
        Assert.assertTrue(seats.contains(new Seat(0, 3)));
        Assert.assertTrue(seats.contains(new Seat(0, 4)));
    }

    @Test
    public void testSingleColumn() {
        rectangle = new RectangleVenue(5, 1);

        SeatHold hold = rectangle.holdSeats(3, "email");
        Set<Seat> seats = hold.getSeats();
        Assert.assertEquals(3, seats.size());

        Assert.assertTrue(seats.contains(new Seat(0, 0)));
        Assert.assertTrue(seats.contains(new Seat(1, 0)));
        Assert.assertTrue(seats.contains(new Seat(2, 0)));
    }

    @Test
    public void testVeeLeftToRight() {
        rectangle = new RectangleVenue(6, 5);

        SeatHold hold = rectangle.holdSeats(4, "email");
        hold = rectangle.holdSeats(9, "email");
        Set<Seat> seats = hold.getSeats();
        Assert.assertEquals(9, seats.size());

        Assert.assertTrue(seats.contains(new Seat(0, 0)));
        Assert.assertTrue(seats.contains(new Seat(1, 0)));
        Assert.assertTrue(seats.contains(new Seat(1, 1)));
        Assert.assertTrue(seats.contains(new Seat(2, 1)));
        Assert.assertTrue(seats.contains(new Seat(2, 2)));
        Assert.assertTrue(seats.contains(new Seat(2, 3)));
        Assert.assertTrue(seats.contains(new Seat(1, 3)));
        Assert.assertTrue(seats.contains(new Seat(1, 4)));
        Assert.assertTrue(seats.contains(new Seat(0, 4)));
    }

    @Test
    public void testVeeRightToLeft() {
        rectangle = new RectangleVenue(6, 5);

        SeatHold hold = rectangle.holdSeats(4, "email");
        hold = rectangle.holdSeats(5, "email");
        hold = rectangle.holdSeats(9, "email");

        Set<Seat> seats = hold.getSeats();
        Assert.assertEquals(9, seats.size());

        Assert.assertTrue(seats.contains(new Seat(0, 4)));
        Assert.assertTrue(seats.contains(new Seat(1, 4)));
        Assert.assertTrue(seats.contains(new Seat(1, 3)));
        Assert.assertTrue(seats.contains(new Seat(2, 3)));
        Assert.assertTrue(seats.contains(new Seat(3, 3)));
        Assert.assertTrue(seats.contains(new Seat(3, 2)));
        Assert.assertTrue(seats.contains(new Seat(3, 1)));
        Assert.assertTrue(seats.contains(new Seat(3, 0)));
        Assert.assertTrue(seats.contains(new Seat(2, 0)));
    }

    @Test
    public void testSingleSeat() {
        rectangle = new RectangleVenue(1, 1);
        Assert.assertNotNull(rectangle.holdSeats(1, "email"));
    }
}
