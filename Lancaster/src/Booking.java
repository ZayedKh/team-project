import java.time.LocalDateTime;
import java.util.List;


public class Booking {
    private String clientName;
    private String eventType;
    private List<String> spaces;
    private LocalDateTime startDate;
    private LocalDateTime endDate;


    Booking(String clientName, String eventType, List<String> spaces, LocalDateTime startDate, LocalDateTime endDate) {
        this.clientName = clientName;
        this.eventType = eventType;
        this.spaces = spaces;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
