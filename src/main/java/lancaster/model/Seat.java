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

