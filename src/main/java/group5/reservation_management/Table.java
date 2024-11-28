package group5.reservation_management;

public class Table {
    private int tableNumber;
    private int capacity;
    private boolean isOccupied;

    public Table(int tableNumber, int capacity) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.isOccupied = false;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }
}
