package lancaster.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.*;

/**
 * Controller class for managing the seating layout of the Small Hall.
 * <p>
 * This controller handles the creation and display of seating arrangements for a small hall,
 * including the generation of seat nodes, random initialization of seat statuses, handling seat selection,
 * zooming of the seating layout via scroll events, and updating the booking button state.
 * </p>
 */
public class SmallHallSeatingController implements Initializable {

    @FXML
    private ScrollPane scrollPane; // ScrollPane that allows the seating layout to be zoomed and scrolled.

    @FXML
    private BorderPane mainBorderPane; // Main container; used if needed for additional layout management.

    @FXML
    private Pane seatingContainer; // Container pane that holds all seat nodes and additional layout elements.

    @FXML
    private Label eventLabel; // Label to display event information related to the seating arrangement.

    @FXML
    private Button bookButton; // Button used to confirm the booking of selected seats.

    private double scaleFactor = 1.0; // Current scale factor for zooming the seating layout.
    private static final double SEAT_WIDTH = 20; // Width of each seat in pixels.
    private static final double SEAT_HEIGHT = 20; // Height of each seat in pixels.
    private static final double SEAT_SPACING = 5; // Spacing between seats in pixels.
    private static final double AISLE_WIDTH = 15; // Width reserved for aisles (if applicable).

    // Map to store the current status (AVAILABLE, RESERVED, UNAVAILABLE) for each seat by its identifier.
    private Map<String, SeatStatus> seatStatusMap = new HashMap<>();

    // Set that keeps track of seat IDs currently selected by the user for booking.
    private Set<String> selectedSeats = new HashSet<>();

    /**
     * Enum representing the various statuses a seat can have(available, reserved or unavailable).
     * <p>
     * Each status defines a background color, border color, and descriptive text.
     * </p>
     */
    public enum SeatStatus {
        AVAILABLE("#E8F5E9", "#4CAF50", "Available"),
        RESERVED("#FFF8E1", "#FFA000", "Reserved"),
        UNAVAILABLE("#FFEBEE", "#D32F2F", "Unavailable");

        private final String backgroundColor;
        private final String borderColor;
        private final String description;

        SeatStatus(String backgroundColor, String borderColor, String description) {
            this.backgroundColor = backgroundColor;
            this.borderColor = borderColor;
            this.description = description;
        }

        /**
         * Returns the background color associated with this seat status.
         *
         * @return A hexadecimal color string for the background.
         */
        public String getBackgroundColor() { return backgroundColor; }

        /**
         * Returns the border color associated with this seat status.
         *
         * @return A hexadecimal color string for the border.
         */
        public String getBorderColor() { return borderColor; }

        /**
         * Returns the descriptive text for this seat status.
         *
         * @return A description of the seat status.
         */
        public String getDescription() { return description; }
    }

    /**
     * Called to initialize the controller after its root element has been processed.
     * <p>
     * This method sets up the scroll behavior, creates the seating layout, adds additional areas (stage, entrance, aisle),
     * randomly initializes seat statuses, updates the seating display, and finally updates the state of the booking button.
     * </p>
     *
     * @param location  The location used to resolve relative paths for the root object, or {@code null} if unknown.
     * @param resources The resources used to localize the root object, or {@code null} if not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scrollPane.setOnScroll(this::handleScroll);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        createSeatingLayout();
        addStageArea();
        addEntrance();
        addAisleLabel();
        initializeRandomSeatStatuses();
        updateSeatingDisplay();
        updateBookButtonState();
    }

    /**
     * Creates the seating layout by clearing the container and generating the stalls area and sound desk.
     */
    private void createSeatingLayout() {
        seatingContainer.getChildren().clear();
        double centerX = 400; // Fixed center coordinate for layout positioning.
        createStallsArea(centerX);
        createSoundDesk(centerX);
    }

    /**
     * Creates the stalls area containing rows of seats.
     * <p>
     * Rows are labeled from "N" to "A" and the number of seats per row can vary.
     * The method calculates starting positions for each row to center the seats.
     * </p>
     *
     * @param centerX The x-coordinate to be used as the center reference for seat layout.
     */
    private void createStallsArea(double centerX) {
        String[] rows = {"N", "M", "L", "K", "J", "I", "H", "G", "F", "E", "D", "C", "B", "A"};
        double startY = 50;
        double commonStartX = 0;

        for (int rowIndex = 0; rowIndex < rows.length; rowIndex++) {
            String row = rows[rowIndex];
            double y = startY + rowIndex * (SEAT_HEIGHT + SEAT_SPACING);
            int seatsPerRow;
            double startX = 0;

            if (row.equals("N")) {
                seatsPerRow = 4;
                double totalWidth = seatsPerRow * (SEAT_WIDTH + SEAT_SPACING) - SEAT_SPACING;
                startX = centerX - (totalWidth / 2);
            } else if (row.equals("M")) {
                seatsPerRow = 4;
                double totalWidth = seatsPerRow * (SEAT_WIDTH + SEAT_SPACING) - SEAT_SPACING;
                double offset = SEAT_WIDTH + SEAT_SPACING;
                startX = centerX - (totalWidth / 2) + offset;
                commonStartX = startX;
            } else if (row.equals("C") || row.equals("B") || row.equals("A")) {
                seatsPerRow = 8;
                startX = commonStartX;
            } else {
                seatsPerRow = 7;
                startX = commonStartX;
            }

            // Create seat nodes for the row.
            for (int seatNum = 1; seatNum <= seatsPerRow; seatNum++) {
                String seatId = row + seatNum;
                double x = startX + (seatNum - 1) * (SEAT_WIDTH + SEAT_SPACING);
                StackPane seat = createSeat(seatId, x, y, SeatStatus.AVAILABLE);
                seatingContainer.getChildren().add(seat);
            }

            // Add row label.
            Label rowLabel = new Label(row);
            rowLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            rowLabel.setLayoutX(startX - 25);
            rowLabel.setLayoutY(y);
            seatingContainer.getChildren().add(rowLabel);
        }
    }

    /**
     * Creates and adds a sound desk to the seating layout.
     * <p>
     * The sound desk is positioned to the right of the seating area.
     * </p>
     *
     * @param centerX The x-coordinate for centering purposes.
     */
    private void createSoundDesk(double centerX) {
        double soundDeskX = centerX + 80;
        double soundDeskY = 50;

        StackPane soundDesk = new StackPane();
        soundDesk.setPrefSize(60, 50);
        soundDesk.setLayoutX(soundDeskX);
        soundDesk.setLayoutY(soundDeskY);
        soundDesk.setStyle("-fx-background-color: #CCCCCC; -fx-background-radius: 5;");

        Label soundDeskLabel = new Label("SOUND\nDESK");
        soundDeskLabel.setFont(Font.font("System", FontWeight.BOLD, 10));
        soundDeskLabel.setTextFill(Color.BLACK);

        soundDesk.getChildren().add(soundDeskLabel);
        seatingContainer.getChildren().add(soundDesk);
    }

    /**
     * Adds a stage area to the seating layout.
     * <p>
     * The stage is placed below the lowest seat row with a fixed margin.
     * </p>
     */
    private void addStageArea() {
        double maxY = 0;
        for (Node node : seatingContainer.getChildren()) {
            if (node instanceof StackPane && node.getId() != null && node.getId().startsWith("seat-")) {
                double seatY = node.getLayoutY() + SEAT_HEIGHT;
                if (seatY > maxY) maxY = seatY;
            }
        }
        double stageDistance = 50;
        StackPane stageArea = new StackPane();
        stageArea.setPrefSize(450, 30);
        stageArea.setLayoutX(175);
        stageArea.setLayoutY(maxY + stageDistance);
        stageArea.setStyle("-fx-background-color: #CCCCCC; -fx-background-radius: 5 5 0 0;");

        Label stageLabel = new Label("STAGE");
        stageLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        stageArea.getChildren().add(stageLabel);

        seatingContainer.getChildren().add(stageArea);
    }

    /**
     * Adds an entrance label to the seating layout.
     * <p>
     * The entrance label indicates the entry point for the hall.
     * </p>
     */
    private void addEntrance() {
        double commonStartX = 0;
        String rowM = "M";
        double centerX = 400;
        double totalWidth = 4 * (SEAT_WIDTH + SEAT_SPACING) - SEAT_SPACING;
        double offset = SEAT_WIDTH + SEAT_SPACING;
        commonStartX = centerX - (totalWidth / 2) + offset;

        double entranceX = commonStartX - 150;
        double entranceY = 50 + 1 * (SEAT_HEIGHT + SEAT_SPACING);

        Label entranceLabel = new Label("↓ ENTRANCE");
        entranceLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        entranceLabel.setLayoutX(entranceX);
        entranceLabel.setLayoutY(entranceY);
        seatingContainer.getChildren().add(entranceLabel);
    }

    /**
     * Adds an aisle label to the seating layout.
     * <p>
     * The label "AISLE" is displayed vertically to indicate the presence of an aisle in the hall.
     * </p>
     */
    private void addAisleLabel() {
        double commonStartX = 0;
        String rowM = "M";
        double centerX = 400;
        double totalWidth = 4 * (SEAT_WIDTH + SEAT_SPACING) - SEAT_SPACING;
        double offset = SEAT_WIDTH + SEAT_SPACING;
        commonStartX = centerX - (totalWidth / 2) + offset;

        int rowIndexF = 6;
        double y = 50 + rowIndexF * (SEAT_HEIGHT + SEAT_SPACING);
        double aisleX = commonStartX - 100;

        Label aisleLabel = new Label("A\nI\nS\nL\nE");
        aisleLabel.setFont(Font.font("System", FontWeight.BOLD, 10));
        aisleLabel.setLayoutX(aisleX);
        aisleLabel.setLayoutY(y - 10);
        seatingContainer.getChildren().add(aisleLabel);
    }

    /**
     * Creates a seat node for the given seat identifier and status.
     * <p>
     * Each seat is represented as a StackPane with a label, tooltip, and click handler.
     * </p>
     *
     * @param seatId The unique identifier for the seat.
     * @param x      The x-coordinate where the seat is placed.
     * @param y      The y-coordinate where the seat is placed.
     * @param status The initial status of the seat.
     * @return A StackPane representing the seat.
     */
    private StackPane createSeat(String seatId, double x, double y, SeatStatus status) {
        StackPane seat = new StackPane();
        seat.setId("seat-" + seatId);
        seat.setLayoutX(x);
        seat.setLayoutY(y);
        seat.setPrefSize(SEAT_WIDTH, SEAT_HEIGHT);
        seat.getStyleClass().add("seat");
        String style = "-fx-background-color: " + status.getBackgroundColor() + ";" +
                "-fx-border-color: " + status.getBorderColor() + ";" +
                "-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-width: 1;";
        seat.setStyle(style);

        Label seatLabel = new Label(seatId);
        seatLabel.setFont(Font.font("System", FontWeight.NORMAL, 6));
        seatLabel.setTextFill(Color.BLACK);
        seat.getChildren().add(seatLabel);

        seatStatusMap.put(seatId, status);

        // Set up mouse click handler for seat selection.
        seat.setOnMouseClicked(event -> handleSeatClick(seatId, seat));
        Tooltip tooltip = new Tooltip(seatId + " - " + status.getDescription());
        Tooltip.install(seat, tooltip);

        return seat;
    }

    /**
     * Handles a click event on a seat.
     * <p>
     * Toggles the selection state of the seat if it is available, updates its appearance,
     * and then updates the booking button state.
     * </p>
     *
     * @param seatId The unique identifier of the clicked seat.
     * @param seat   The StackPane representing the seat.
     */
    private void handleSeatClick(String seatId, StackPane seat) {
        if (seatStatusMap.get(seatId) == SeatStatus.AVAILABLE) {
            if (selectedSeats.contains(seatId)) {
                selectedSeats.remove(seatId);
                updateSeatAppearance(seat, seatId, false);
            } else {
                selectedSeats.add(seatId);
                updateSeatAppearance(seat, seatId, true);
            }
            updateBookButtonState();
        }
    }

    /**
     * Updates the visual appearance of a seat based on its selection state.
     *
     * @param seat       The StackPane representing the seat.
     * @param seatId     The unique identifier of the seat.
     * @param isSelected True if the seat is selected, false otherwise.
     */
    private void updateSeatAppearance(StackPane seat, String seatId, boolean isSelected) {
        SeatStatus status = seatStatusMap.get(seatId);
        String style = "-fx-background-color: " + status.getBackgroundColor() + ";" +
                "-fx-border-color: " + status.getBorderColor() + ";" +
                "-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-width: " + (isSelected ? "2" : "1") + ";";
        if (isSelected) style += "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);";
        seat.setStyle(style);
    }

    /**
     * Iterates over all seat nodes to update their appearance.
     * <p>
     * Ensures the UI reflects the current status of each seat and re-installs tooltips.
     * </p>
     */
    private void updateSeatingDisplay() {
        seatingContainer.getChildren().forEach(node -> {
            if (node instanceof StackPane && node.getId() != null && node.getId().startsWith("seat-")) {
                String seatId = node.getId().substring(5);
                boolean isSelected = selectedSeats.contains(seatId);
                updateSeatAppearance((StackPane) node, seatId, isSelected);
                SeatStatus status = seatStatusMap.get(seatId);
                Tooltip.install(node, new Tooltip(seatId + " - " + status.getDescription()));
            }
        });
    }

    /**
     * Updates the enabled state of the booking button.
     * <p>
     * The button is enabled only when there is at least one selected seat.
     * </p>
     */
    private void updateBookButtonState() {
        bookButton.setDisable(selectedSeats.isEmpty());
    }

    /**
     * Initializes seat statuses for demonstration purposes.
     * <p>
     * This method assigns a status (AVAILABLE, RESERVED, or UNAVAILABLE) to each seat.
     * </p>
     */
    private void initializeRandomSeatStatuses() {
        Random random = new Random();
        for (String seatId : seatStatusMap.keySet()) {
            int rand = random.nextInt(10);
            seatStatusMap.put(seatId, rand < 6 ? SeatStatus.AVAILABLE : rand < 8 ? SeatStatus.RESERVED : SeatStatus.UNAVAILABLE);
        }
    }

    /**
     * Handles the booking action when the book button is clicked.
     * <p>
     * Displays a confirmation dialog showing the selected seats. After confirmation,
     * the selected seats are marked as RESERVED, and the seating display is updated.
     * </p>
     */
    @FXML
    private void handleBookSeats() {
        if (!selectedSeats.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Booking Confirmation");
            alert.setHeaderText("Seat Booking");
            String seatsText = "You have selected the following seats:\n" + String.join(", ", selectedSeats);
            alert.setContentText(seatsText);
            alert.showAndWait();
            for (String seatId : selectedSeats) seatStatusMap.put(seatId, SeatStatus.RESERVED);
            selectedSeats.clear();
            updateSeatingDisplay();
            updateBookButtonState();
        }
    }

    /**
     * Handles scroll events to implement zoom functionality on the seating layout.
     * <p>
     * When the Control key is pressed, the scale factor is adjusted (zoom in or out) within a defined range
     * and applied to the seating container.
     * </p>
     *
     * @param event The ScrollEvent triggered by the user.
     */
    private void handleScroll(ScrollEvent event) {
        if (event.isControlDown()) {
            double deltaY = event.getDeltaY();
            scaleFactor = Math.max(0.5, Math.min(3.0, deltaY > 0 ? scaleFactor * 1.1 : scaleFactor / 1.1));
            seatingContainer.setScaleX(scaleFactor);
            seatingContainer.setScaleY(scaleFactor);
            event.consume();
        }
    }

    /**
     * Updates the status of a specific seat.
     * <p>
     * If the seat exists, its status is updated and the seating display is refreshed.
     * </p>
     *
     * @param seatId The identifier of the seat to update.
     * @param status The new status to set.
     */
    public void updateSeatStatus(String seatId, SeatStatus status) {
        if (seatStatusMap.containsKey(seatId)) {
            seatStatusMap.put(seatId, status);
            updateSeatingDisplay();
        }
    }

    /**
     * Sets the event information in the seating layout header.
     * <p>
     * Displays the current event name and date.
     * </p>
     *
     * @param eventName The name of the event.
     * @param eventDate The date of the event.
     */
    public void setEventInfo(String eventName, Date eventDate) {
        eventLabel.setText("Current Event: " + eventName + " - " + eventDate);
    }
}
