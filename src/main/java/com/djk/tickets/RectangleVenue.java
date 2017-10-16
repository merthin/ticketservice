package com.djk.tickets;

import java.awt.*;
import java.util.*;

public class RectangleVenue extends Venue {

    private int numRows;

    private int numCols;

    private Point bestSeat;

    private int seatsAvailable;

    private boolean[][] seats;

    private int nextHoldId;

    public RectangleVenue(int numRows, int numCols) {
        super(1000);
        this.numCols = numCols;
        this.numRows = numRows;
        bestSeat = new Point(0, numCols / 2);
        seats = new boolean[numRows][numCols];
        seatsAvailable = numRows * numCols;
    }

    protected SeatHold holdSeats(int numSeats, String customerEmail) {
        Set<Point> points = findSeats(numSeats, findBestAvailableSeat(bestSeat));
        if (!points.isEmpty()) {
            Set<Seat> seatsForHold = new HashSet<Seat>(points.size());
            for (Point point : points) {
                seats[point.x][point.y] = true;
                seatsForHold.add(new Seat(point.x, point.y));
            }
            seatsAvailable -= numSeats;
            SeatHold rc = new SeatHold(nextHoldId++, seatsForHold);
            return rc;
        }
        return null;
    }

    public void unholdSeats(SeatHold hold) {
        for (Seat seat : hold.getSeats()) {
            seats[seat.getRow()][seat.getCol()] = false;
        }

        seatsAvailable += hold.getSeats().size();
    }

    private Point findBestAvailableSeat(Point start) {
        return findBestAvailableSeat(start, 0);
    }

    private Point findBestAvailableSeat(Point start, int skip) {
        HashSet<Point> skipped = new HashSet<Point>();

        if (!seats[start.x][start.y]) {
            if (skip == 0)
                return start;
            else
                skipped.add(start);
        }

        Queue<Point> queue = new LinkedList<Point>();
        queue.add(start);

        while (!queue.isEmpty()) {
            Point point = queue.remove();

            if (skipped.contains(point))
                skip--;

            int curRow = point.x;
            int curCol = point.y;

            if (curCol - 1 >= 0) {
                Point newPoint = new Point(curRow, curCol - 1);
                if (seats[curRow][curCol - 1]) {
                    queue.add(newPoint);
                } else {
                    if (skip == 0 && !skipped.contains(newPoint))
                        return newPoint;
                    else if (!skipped.contains(newPoint)){
                        skipped.add(newPoint);
                        queue.add(newPoint);
                    }
                }
            }
            if (curCol + 1 < numCols) {
                Point newPoint = new Point(curRow, curCol + 1);
                if (seats[curRow][curCol + 1]) {
                    queue.add(newPoint);
                } else {
                    if (skip == 0 && !skipped.contains(newPoint))
                        return newPoint;
                    else if (!skipped.contains(newPoint)){
                        skipped.add(newPoint);
                        queue.add(newPoint);
                    }
                }
            }
            if (curRow + 1 < numRows) {
                Point newPoint = new Point(curRow + 1, curCol);
                if (seats[curRow + 1][curCol]) {
                    queue.add(newPoint);
                } else {
                    if (skip == 0 && !skipped.contains(newPoint))
                        return newPoint;
                    else if (!skipped.contains(newPoint)){
                        skipped.add(newPoint);
                        queue.add(newPoint);
                    }
                }
            }
        }
        return null;
    }

    private Set<Point> findSeats(int numSeats, Point start) {
        HashSet<Point> selectedSeats = new HashSet<Point>(numSeats);

        Point theStart = start;
        traverseFrom(theStart, selectedSeats, numSeats);

        while (selectedSeats.size() < numSeats) {
            int skip = selectedSeats.size();
            selectedSeats.clear();
            theStart = findBestAvailableSeat(start, skip);
            if (theStart == null)
                break;
            traverseFrom(theStart, selectedSeats, numSeats);
        }

        return selectedSeats;
    }

    private void traverseFrom(Point curSeat, HashSet<Point> selectedSeats, int seatsNeeded) {
        Queue<Point> queue = new LinkedList<Point>();
        queue.add(curSeat);

        while (!queue.isEmpty()) {
            Point curPoint = queue.remove();
            selectedSeats.add(curPoint);

            if (selectedSeats.size() == seatsNeeded)
                return;

            int curRow = curPoint.x;
            int curCol = curPoint.y;

            if (curCol < bestSeat.y) {
                if (curRow - 1 >= 0 && !seats[curRow - 1][curCol] && !selectedSeats.contains(new Point(curRow - 1, curCol))) {
                    // move up one row
//                    queue.add(new Point(curRow - 1, curCol));
                    embarkFrom(new Point(curRow - 1, curCol), selectedSeats, seatsNeeded);
                    if (selectedSeats.size() == seatsNeeded)
                        return;
                }
                if (!seats[curRow][curCol+1] && !selectedSeats.contains(new Point(curRow, curCol + 1))) {
                    // move to the right
//                    queue.add(new Point(curRow, curCol + 1));
                    embarkFrom(new Point(curRow, curCol + 1), selectedSeats, seatsNeeded);
                    if (selectedSeats.size() == seatsNeeded)
                        return;
                }
                if (curRow + 1 < numRows && !seats[curRow + 1][curCol] && !selectedSeats.contains(new Point(curRow + 1, curCol))) {
                    // move back one row
                    queue.add(new Point(curRow + 1, curCol));
                }
                if (curCol - 1 >= 0 && !seats[curRow][curCol - 1] && !selectedSeats.contains(new Point(curRow, curCol - 1))) {
                    // move to the left
                    queue.add(new Point(curRow, curCol - 1));
                }
            } else if (curCol == bestSeat.y) {
                boolean leftAdded = false;
                boolean rightAdded = false;

                // pre-add the seat to the left
                if (curCol - 1 >= 0 && !seats[curRow][curCol - 1]) {
                    selectedSeats.add(new Point(curRow, curCol - 1));
                    queue.add(new Point(curRow, curCol - 1));
                    leftAdded = true;

                    if (selectedSeats.size() == seatsNeeded)
                        return;
                }

                // pre-add the seat to the right
                if (curCol + 1 < numCols && !seats[curRow][curCol + 1]) {
                    selectedSeats.add(new Point(curRow, curCol + 1));
                    queue.add(new Point(curRow, curCol + 1));
                    rightAdded = true;

                    if (selectedSeats.size() == seatsNeeded)
                        return;
                }

                // for the best seat only, pre-add the seat behind
                if (curRow + 1 < numRows && !seats[curRow + 1][curCol]) {
                    if (curRow == bestSeat.x) {
                        selectedSeats.add(new Point(curRow + 1, curCol));
                        if (selectedSeats.size() == seatsNeeded)
                            return;
                    }
                    queue.add(new Point(curRow + 1, curCol));
                }
            } else if (curCol > bestSeat.y) {
                if (curRow - 1 >= 0 && !seats[curRow - 1][curCol] && !selectedSeats.contains(new Point(curRow - 1, curCol))) {
                    // move up one row
//                    queue.add(new Point(curRow - 1, curCol));
                    embarkFrom(new Point(curRow - 1, curCol), selectedSeats, seatsNeeded);
                    if (selectedSeats.size() == seatsNeeded)
                        return;
                }
                if (curCol - 1 >= 0 && !seats[curRow][curCol - 1] && !selectedSeats.contains(new Point(curRow, curCol - 1))) {
                    // move to the left
//                    queue.add(new Point(curRow, curCol - 1));
                    embarkFrom(new Point(curRow, curCol - 1), selectedSeats, seatsNeeded);
                    if (selectedSeats.size() == seatsNeeded)
                        return;
                }
                if (curRow + 1 < numRows && !seats[curRow + 1][curCol] && !selectedSeats.contains(new Point(curRow + 1, curCol))) {
                    // move back one row
                    queue.add(new Point(curRow + 1, curCol));
                }
                if (curCol + 1 < numCols && !seats[curRow][curCol+1] && !selectedSeats.contains(new Point(curRow, curCol + 1))) {
                    // move to the right
                    queue.add(new Point(curRow, curCol + 1));
                }
            }
        }
    }

    private void embarkFrom(Point curSeat, HashSet<Point> selectedSeats, int seatsNeeded) {
        selectedSeats.add(curSeat);

        if (selectedSeats.size() == seatsNeeded)
            return;

        int curRow = curSeat.x;
        int curCol = curSeat.y;

        if (curCol < bestSeat.y) {
            if (curRow - 1 >= 0 && !seats[curRow - 1][curCol] && !selectedSeats.contains(new Point(curRow - 1, curCol))) {
                // move up one row
                embarkFrom(new Point(curRow - 1, curCol), selectedSeats, seatsNeeded);
            } else if (!seats[curRow][curCol+1] && !selectedSeats.contains(new Point(curRow, curCol + 1))) {
                // move to the right
                embarkFrom(new Point(curRow, curCol + 1), selectedSeats, seatsNeeded);
            } else {
                if (selectedSeats.contains(new Point(curRow - 1, curCol))) {
                    // we've already visited the seat in front of us, so try to move one row back first
                    if (curRow + 1 < numRows && !seats[curRow + 1][curCol] && !selectedSeats.contains(new Point(curRow + 1, curCol))) {
                        // move back one row
                        embarkFrom(new Point(curRow + 1, curCol), selectedSeats, seatsNeeded);
                    } else if (curCol - 1 >= 0 && !seats[curRow][curCol - 1] && !selectedSeats.contains(new Point(curRow, curCol - 1))) {
                        // move to the left
                        embarkFrom(new Point(curRow, curCol - 1), selectedSeats, seatsNeeded);
                    }
                } else {
                    if (curCol - 1 >= 0 && !seats[curRow][curCol - 1] && !selectedSeats.contains(new Point(curRow, curCol - 1))) {
                        // move to the left
                        embarkFrom(new Point(curRow, curCol - 1), selectedSeats, seatsNeeded);
                    } else if (curRow + 1 < numRows && !seats[curRow + 1][curCol] && !selectedSeats.contains(new Point(curRow + 1, curCol))) {
                        // move back one row
                        embarkFrom(new Point(curRow + 1, curCol), selectedSeats, seatsNeeded);
                    }
                }
            }
        } else if (curCol == bestSeat.y) {
            boolean leftAdded = false;
            boolean rightAdded = false;

            // pre-add the seat to the left
            if (!seats[curRow][curCol - 1] && !selectedSeats.contains(new Point(curRow, curCol - 1))) {
                selectedSeats.add(new Point(curRow, curCol - 1));
                leftAdded = true;

                if (selectedSeats.size() == seatsNeeded)
                    return;
            }

            // pre-add the seat to the right
            if (!seats[curRow][curCol + 1] && !selectedSeats.contains(new Point(curRow, curCol + 1))) {
                selectedSeats.add(new Point(curRow, curCol + 1));
                rightAdded = true;

                if (selectedSeats.size() == seatsNeeded)
                    return;
            }

            // for the best sead only, pre-add the seat behind
            if (curRow == bestSeat.x && !seats[curRow+1][curCol]) {
                selectedSeats.add(new Point(curRow + 1, curCol));

                if (selectedSeats.size() == seatsNeeded)
                    return;
            }

            if (leftAdded)
                embarkFrom(new Point(curRow, curCol - 1), selectedSeats, seatsNeeded);
            if (rightAdded)
                embarkFrom(new Point(curRow, curCol + 1), selectedSeats, seatsNeeded);
        } else if (curCol > bestSeat.y) {
            if (curRow - 1 >= 0 && !seats[curRow - 1][curCol] && !selectedSeats.contains(new Point(curRow - 1, curCol))) {
                // move up one row
                embarkFrom(new Point(curRow - 1, curCol), selectedSeats, seatsNeeded);
            } else if (curCol - 1 >= 0 && !seats[curRow][curCol - 1] && !selectedSeats.contains(new Point(curRow, curCol - 1))) {
                // move to the left
                embarkFrom(new Point(curRow, curCol - 1), selectedSeats, seatsNeeded);
            } else {
                if (selectedSeats.contains(new Point(curRow - 1, curCol))) {
                    // we've already visited the seat in front of us, so try to move one row back first
                    if (curRow + 1 < numRows && !seats[curRow + 1][curCol] && !selectedSeats.contains(new Point(curRow + 1, curCol))) {
                        // move back one row
                        embarkFrom(new Point(curRow + 1, curCol), selectedSeats, seatsNeeded);
                    } else if (curCol + 1 < numCols && !seats[curRow][curCol + 1] && !selectedSeats.contains(new Point(curRow, curCol + 1))) {
                        // move to the right
                        embarkFrom(new Point(curRow, curCol + 1), selectedSeats, seatsNeeded);
                    }
                } else {
                    if (curCol + 1 < numCols && !seats[curRow][curCol + 1] && !selectedSeats.contains(new Point(curRow, curCol + 1))) {
                        // move to the right
                        embarkFrom(new Point(curRow, curCol + 1), selectedSeats, seatsNeeded);
                    } else if (curRow + 1 < numRows && !seats[curRow + 1][curCol] && !selectedSeats.contains(new Point(curRow + 1, curCol))) {
                        // move back one row
                        embarkFrom(new Point(curRow + 1, curCol), selectedSeats, seatsNeeded);
                    }
                }
            }
        }
    }

    public int numSeatsAvailable() {
        return seatsAvailable;
    }
}
