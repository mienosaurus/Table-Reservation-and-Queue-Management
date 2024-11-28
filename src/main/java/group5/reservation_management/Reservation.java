package group5.reservation_management;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {
    private String name;
    private int partySize;
    private LocalDate date;
    private LocalTime time;
    private String status;

    public Reservation(String name, int partySize, LocalDate date, LocalTime time, String status) {
        this.name = name;
        this.partySize = partySize;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPartySize() {
        return partySize;
    }

    public void setPartySize(int partySize) {
        this.partySize = partySize;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
