package lancaster.model;

public class Booking {
    private String space;
    private String startTime;
    private String endTime;
    private String bookedBy;
    private String configuration;

    public Booking(String space, String startTime, String endTime, String bookedBy, String configuration) {
        this.space = space;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bookedBy = bookedBy;
        this.configuration = configuration;
    }

    // Getters
    public String getSpace() {
        return space;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public String getConfiguration() {
        return configuration;
    }

    // Setters
    public void setSpace(String space) {
        this.space = space;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }
}