package lancaster.model;

/**
 * Represents a seat within a specific room of a venue.
 * <p>
 * This class encapsulates the details of a seat, including its unique ID, location information,
 * and accessibility features. Each seat is associated with a room and is identified by its row and seat numbers.
 * It may also have attributes indicating whether it is accessible or wheelchair-friendly.
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
     * Constructs an empty {@code Seat} instance.
     */
    public Seat() {
        // default constructor
    }

    /**
     * Constructs a new {@code Seat} instance with the provided details.
     *
     * @param seatId              the unique identifier for the seat.
     * @param roomId              the identifier of the room where the seat is located.
     * @param rowNumber           the row number in which the seat is positioned.
     * @param seatNumber          the specific seat number within the row.
     * @param isAccessible        whether the seat is accessible.
     * @param isWheelchairFriendly whether the seat is suitable for wheelchair users.
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
     * @return the seat ID.
     */
    public int getSeatId() {
        return seatId;
    }

    /**
     * Returns the ID of the room where the seat is located.
     *
     * @return the room ID.
     */
    public int getRoomId() {
        return roomId;
    }

    /**
     * Returns the row number of the seat.
     *
     * @return the row number.
     */
    public int getRowNumber() {
        return rowNumber;
    }

    /**
     * Returns the specific number of the seat within the row.
     *
     * @return the seat number.
     */
    public int getSeatNumber() {
        return seatNumber;
    }

    /**
     * Indicates whether the seat is accessible.
     *
     * @return {@code true} if the seat is accessible; {@code false} otherwise.
     */
    public boolean isAccessible() {
        return isAccessible;
    }

    /**
     * Indicates whether the seat is wheelchair-friendly.
     *
     * @return {@code true} if the seat is wheelchair-friendly; {@code false} otherwise.
     */
    public boolean isWheelchairFriendly() {
        return isWheelchairFriendly;
    }

    /**
     * Sets the unique identifier for the seat.
     *
     * @param seatId the seat ID to set.
     */
    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    /**
     * Sets the ID of the room associated with the seat.
     *
     * @param roomId the room ID to set.
     */
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    /**
     * Sets the row number where the seat is located.
     *
     * @param rowNumber the row number to set.
     */
    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    /**
     * Sets the specific seat number within the row.
     *
     * @param seatNumber the seat number to set.
     */
    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    /**
     * Sets whether the seat is accessible.
     *
     * @param accessible {@code true} to mark the seat as accessible; {@code false} otherwise.
     */
    public void setAccessible(boolean accessible) {
        isAccessible = accessible;
    }

    /**
     * Sets whether the seat is wheelchair-friendly.
     *
     * @param wheelchairFriendly {@code true} to mark the seat as wheelchair-friendly; {@code false} otherwise.
     */
    public void setWheelchairFriendly(boolean wheelchairFriendly) {
        isWheelchairFriendly = wheelchairFriendly;
    }

    /**
     * Returns a string representation of the {@code Seat} object.
     *
     * @return a string summarizing the seat details.
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
