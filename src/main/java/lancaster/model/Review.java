package lancaster.model;

import java.sql.Timestamp;

public class Review {

    private int reviewID;
    private Room room;
    private Event event;
    private int rating;
    private String author;
    private String title;
    private String description;
    private Timestamp timestamp;


    /**
     * Constructor for review
     * @param reviewID      ID for review
     * @param room          The room that the event took place, if null be for lancaster as
     *                      a whole
     * @param event         The event that the review is for, if null be for lancaster as a
     *                      whole
     * @param rating        The rating of the review
     * @param author        The author of the review
     * @param title         The title of the review
     * @param description   The description of the review
     * @param timestamp     Time the review was placed
     */
    public Review(int reviewID, Room room, Event event, int rating, String author,
                  String title, String description, Timestamp timestamp){

        this.reviewID = reviewID;
        this.room = room;
        this.event = event;
        this.rating = rating;
        this.author = author;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
    }

    public int getReviewID() {
        return reviewID;
    }

    public Room getRoom() {
        return room;
    }

    public Event getEvent() {
        return event;
    }

    public int getRating() {
        return rating;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}