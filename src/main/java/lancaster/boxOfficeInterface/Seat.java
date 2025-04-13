package lancaster.boxOfficeInterface;

/**
 * Represents a seating unit within a room.
 * <p>
 * This class encapsulates details about a seat such as its identifier, the room where it is located,
 * its row and seat numbers, and whether it is accessible or wheelchair-friendly.
 * </p>
 */
public class Seat {
    private int seatId;
    private int roomId;
    private int rowNumber;
    private int seatNumber;
    private boolean isAccessible;
    private boolean isWheelchairFriendly;

    /**
     * Default constructor for creating an empty Seat object.
     */
    public Seat() {
        // default constructor
    }

    /**
     * Constructs a Seat with specified details.
     *
     * @param seatId              the unique identifier for the seat
     * @param roomId              the identifier of the room containing the seat
     * @param rowNumber           the row number where the seat is located
     * @param seatNumber          the seat's position number within the row
     * @param isAccessible        flag indicating if the seat is accessible
     * @param isWheelchairFriendly flag indicating if the seat is wheelchair-friendly
     */
    public Seat(int seatId, int roomId, int rowNumber, int seatNumber,
                boolean isAccessible, boolean isWheelchairFriendly) {
        this.seatId = seatId;
        this.roomId = roomId;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.isAccessible = isAccessible;
        this.isWheelchairFriendly = isWheelchairFriendly;
    }

    /**
     * Returns the unique identifier of the seat.
     *
     * @return the seat ID as an int
     */
    public int getSeatId() {
        return seatId;
    }

    /**
     * Returns the identifier of the room that contains this seat.
     *
     * @return the room ID as an int
     */
    public int getRoomId() {
        return roomId;
    }

    /**
     * Returns the row number of this seat within the room.
     *
     * @return the row number as an int
     */
    public int getRowNumber() {
        return rowNumber;
    }

    /**
     * Returns the seat number within its row.
     *
     * @return the seat number as an int
     */
    public int getSeatNumber() {
        return seatNumber;
    }

    /**
     * Indicates whether the seat is accessible.
     *
     * @return  {@code true} if the seat is accessible; {@code false} otherwise
     */
    public boolean isAccessible() {
        return isAccessible;
    }

    /**
     * Indicates whether the seat is wheelchair-friendly.
     *
     * @return {@code true} if the seat is wheelchair-friendly; {@code false} otherwise
     */
    public boolean isWheelchairFriendly() {
        return isWheelchairFriendly;
    }

    /**
     * Sets the unique identifier for the seat.
     *
     * @param seatId the new seat ID as an int
     */
    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    /**
     * Sets the room identifier for this seat.
     *
     * @param roomId the new room ID as an int
     */
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    /**
     * Sets the row number for the seat.
     *
     * @param rowNumber the new row number as an int
     */
    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    /**
     * Sets the seat number within the row.
     *
     * @param seatNumber the new seat number as an int
     */
    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    /**
     * Sets whether the seat is accessible.
     *
     * @param accessible the accessibility flag as a boolean
     */
    public void setAccessible(boolean accessible) {
        isAccessible = accessible;
    }

    /**
     * Sets whether the seat is wheelchair-friendly.
     *
     * @param wheelchairFriendly the wheelchair-friendly flag as a boolean
     */
    public void setWheelchairFriendly(boolean wheelchairFriendly) {
        isWheelchairFriendly = wheelchairFriendly;
    }

    /**
     * Returns a string representation of the Seat object.
     *
     * @return a string containing seat details such as seat ID, room ID, row number, seat number,
     *         accessibility, and wheelchair friendliness.
     */
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
