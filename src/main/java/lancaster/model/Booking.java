package lancaster.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Booking {
    private String space;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String bookedBy;
    private String configuration;

    public Booking(String space, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, String bookedBy, String configuration) {
        this.space = space;

        this.startTime = startTime;
        this.endTime = endTime;
        this.bookedBy = bookedBy;
        this.startDate = startDate;
        this.endDate = endDate;
        this.configuration = configuration;
    }

    // Getters
    public String getSpace() {
        return space;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalTime getEndTime() {
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

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }
}