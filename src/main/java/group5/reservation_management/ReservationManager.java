package group5.reservation_management;

import java.util.*;

public class ReservationManager {
    private LinkedList<Reservation> reservations;
    private Queue<Reservation> waitlist;
    private Map<Integer, Table> tables;

    public ReservationManager() {
        reservations = new LinkedList<>();
        waitlist = new LinkedList<>();
        tables = new HashMap<>();

        for (int i = 1; i <= 10; i++) {
            tables.put(i, new Table(i, i));
        }
    }

    public boolean addReservation(Reservation reservation) {
        for (Table table : tables.values()) {
            if (!table.isOccupied() && table.getCapacity() >= reservation.getPartySize()) {
                table.setOccupied(true);
                reservations.add(reservation);
                return true;
            }
        }
        waitlist.add(reservation);
        return false;
    }

    public void removeReservation(Reservation reservation) {
        if (reservations.remove(reservation)) {
        }
    }

    public LinkedList<Reservation> getReservations() {
        return reservations;
    }
}
