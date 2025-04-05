package lancaster.model;

public class Review {
    private int id;
    private int rating;
    private String author;
    private String title;
    private String description;
    private String room;
    private String timestamp;
    private String reviewType;
    private String showName;

    public Review(int id, int rating, String author, String title, String description, String room, String reviewType, String showName) {
        this.id = id;
        this.rating = rating;
        this.author = author;
        this.title = title;
        this.description = description;
        this.room = room;
        this.reviewType = reviewType;
        this.showName = showName;
    }

    public int getId() {
        return id;
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

    public String getRoom() {
        return room;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getReviewType() {
        return reviewType;
    }

    public String getShowName() {
        return showName;
    }
}