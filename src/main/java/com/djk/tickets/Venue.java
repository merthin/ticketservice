package com.djk.tickets;

import java.util.*;

public abstract class Venue implements TicketService {
    private HashSet<Seat> available;

    private Timer timer = new Timer();

    private long expirationMillis;

    private HashMap<Integer, TimerTask> holds = new HashMap<Integer, TimerTask>();

    public Venue(long expirationMillis) {
        this.expirationMillis = expirationMillis;
    }

    public synchronized SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
        final SeatHold rc = holdSeats(numSeats, customerEmail);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                holdExpired(rc);
            }
        };
        holds.put(rc.getId(), task);
        timer.schedule(task, expirationMillis);
        return rc;
    }

    public String reserveSeats(int seatHoldId, String customerEmail) {
        TimerTask task = holds.get(seatHoldId);
        if (task == null){
            return null;
        }
        task.cancel();
        return UUID.randomUUID().toString();
    }

    protected abstract SeatHold holdSeats(int numSeats, String customerEmail);

    private synchronized void holdExpired(SeatHold hold){
        holds.remove(hold.getId());
        unholdSeats(hold);
    }

    protected abstract void unholdSeats(SeatHold hold);
}
