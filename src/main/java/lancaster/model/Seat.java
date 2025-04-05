package lancaster.model;

public class Seat {
    private int seatId;
    private Room room;
    private int rowNumber;
    private int seatNumber;
    private boolean isAccessible;
    private boolean isWheelchairFriendly;


    /**
     * Constructor for seat
     * @param seatId                ID of seat
     * @param room                Room the seat is in
     * @param rowNumber             Row number of seat
     * @param seatNumber            Seat number in row
     * @param isAccessible          Is the seat accessible?
     * @param isWheelchairFriendly  Is the seat wheelchair friendly?
     */
    public Seat(int seatId, Room room, int rowNumber, int seatNumber,
                boolean isAccessible, boolean isWheelchairFriendly) {
        this.seatId = seatId;
        this.room = room;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.isAccessible = isAccessible;
        this.isWheelchairFriendly = isWheelchairFriendly;
    }

    // Getters
    public int getSeatId() {
        return seatId;
    }

    public Room getRoom() {
        return room;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public boolean isAccessible() {
        return isAccessible;
    }

    public boolean isWheelchairFriendly() {
        return isWheelchairFriendly;
    }

    // Setters
    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public void setAccessible(boolean accessible) {
        isAccessible = accessible;
    }

    public void setWheelchairFriendly(boolean wheelchairFriendly) {
        isWheelchairFriendly = wheelchairFriendly;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatId=" + seatId +
                ", room=" + room.getName() +
                ", row=" + rowNumber +
                ", seat=" + seatNumber +
                ", accessible=" + isAccessible +
                ", wheelchair=" + isWheelchairFriendly +
                '}';
    }
}

