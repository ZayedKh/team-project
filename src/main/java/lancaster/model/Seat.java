package lancaster.model;

public class Seat {
    private int seatId;
    private int roomId;
    private int rowNumber;
    private int seatNumber;
    private boolean isAccessible;
    private boolean isWheelchairFriendly;

    public Seat() {
        // default constructor
    }

    public Seat(int seatId, int roomId, int rowNumber, int seatNumber,
                boolean isAccessible, boolean isWheelchairFriendly) {
        this.seatId = seatId;
        this.roomId = roomId;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.isAccessible = isAccessible;
        this.isWheelchairFriendly = isWheelchairFriendly;
    }

    // Getters
    public int getSeatId() {
        return seatId;
    }

    public int getRoomId() {
        return roomId;
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

    public void setRoomId(int roomId) {
        this.roomId = roomId;
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
                ", roomId=" + roomId +
                ", row=" + rowNumber +
                ", seat=" + seatNumber +
                ", accessible=" + isAccessible +
                ", wheelchair=" + isWheelchairFriendly +
                '}';
    }
}

