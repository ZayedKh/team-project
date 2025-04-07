package lancaster.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller class for managing room layout and seat booking functionality in a JavaFX application.
 */
public class RoomLayoutController implements Initializable {

    @FXML private ScrollPane scrollPane;              // Scroll pane containing all layouts
    @FXML private HBox layoutsContainer;             // Container for different layout types
    @FXML private Label roomNameLabel;              // Displays current room name
    @FXML private Button bookButton;                // Button to confirm seat booking
    @FXML private ToggleGroup roomToggleGroup;      // Group for room selection toggle buttons
    @FXML private ToggleButton greenRoomToggle;     // Toggle for Green Room
    @FXML private ToggleButton bronteBoardroomToggle; // Toggle for Brontë Boardroom
    @FXML private ToggleButton dickensDenToggle;    // Toggle for Dickens Den
    @FXML private ToggleButton poeParlorToggle;     // Toggle for Poe Parlor
    @FXML private ToggleButton globeRoomToggle;     // Toggle for Globe Room
    @FXML private ToggleButton chekhovChamberToggle;// Toggle for Chekhov Chamber

    private double scaleFactor = 1.0;               // Zoom scale factor for layout display
    private static final double SEAT_WIDTH = 20;    // Width of each seat
    private static final double SEAT_HEIGHT = 20;   // Height of each seat
    private static final double SEAT_SPACING = 10;  // Spacing between seats
    private static final int LAYOUT_WIDTH = 300;    // Width of each layout pane
    private static final int LAYOUT_HEIGHT = 350;   // Height of each layout pane

    private Map<String, SeatStatus> seatStatusMap = new HashMap<>(); // Tracks status of all seats
    private Set<String> reservedSeats = new HashSet<>();            // Set of reserved seat IDs
    private Set<String> selectedSeats = new HashSet<>();           // Set of currently selected seat IDs

    private final Map<String, Map<String, Integer>> roomCapacities = new HashMap<>(); // Room capacities by layout type
    private String currentRoom = "Green Room";                    // Currently selected room
    private String[] layoutTypes = {"Classroom", "Boardroom", "Presentation"}; // Available layout types

    /**
     * Enum representing the possible status states of a seat.
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

        public String getBackgroundColor() { return backgroundColor; }
        public String getBorderColor() { return borderColor; }
        public String getDescription() { return description; }
    }

    /**
     * Initializes the controller after its root element has been processed.
     * @param location The location used to resolve relative paths for the root object
     * @param resources The resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeRoomCapacities();
        scrollPane.setOnScroll(this::handleScroll);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        applyToggleButtonStyles();
        greenRoomToggle.setSelected(true);
        updateRoomDisplay("Green Room");
    }

    /**
     * Applies styling to toggle buttons for room selection.
     */
    private void applyToggleButtonStyles() {
        String baseStyle = "-fx-background-color: #1976D2; -fx-text-fill: white; -fx-background-radius: 5;";
        String selectedStyle = "-fx-background-color: #0D47A1; -fx-text-fill: white; -fx-background-radius: 5;";
        ToggleButton[] toggles = {greenRoomToggle, bronteBoardroomToggle, dickensDenToggle,
                poeParlorToggle, globeRoomToggle, chekhovChamberToggle};
        for (ToggleButton toggle : toggles) {
            toggle.setStyle(baseStyle);
            toggle.selectedProperty().addListener((obs, old, newVal) ->
                    toggle.setStyle(newVal ? selectedStyle : baseStyle));
        }
    }

    /**
     * Initializes the room capacity data for all rooms and layout types.
     */
    private void initializeRoomCapacities() {
        roomCapacities.put("Green Room", Map.of("Classroom", 12, "Boardroom", 10, "Presentation", 20));
        roomCapacities.put("Brontë Boardroom", Map.of("Classroom", 25, "Boardroom", 18, "Presentation", 40));
        roomCapacities.put("Dickens Den", Map.of("Classroom", 15, "Boardroom", 12, "Presentation", 25));
        roomCapacities.put("Poe Parlor", Map.of("Classroom", 20, "Boardroom", 14, "Presentation", 30));
        roomCapacities.put("Globe Room", Map.of("Classroom", 30, "Boardroom", 20, "Presentation", 50));
        roomCapacities.put("Chekhov Chamber", Map.of("Classroom", 18, "Boardroom", 16, "Presentation", 35));
    }

    // Handler methods for room toggle buttons
    @FXML private void handleGreenRoomToggle() { updateRoomDisplay("Green Room"); }
    @FXML private void handleBronteBoardroomToggle() { updateRoomDisplay("Brontë Boardroom"); }
    @FXML private void handleDickensDenToggle() { updateRoomDisplay("Dickens Den"); }
    @FXML private void handlePoeParlorToggle() { updateRoomDisplay("Poe Parlor"); }
    @FXML private void handleGlobeRoomToggle() { updateRoomDisplay("Globe Room"); }
    @FXML private void handleChekhovChamberToggle() { updateRoomDisplay("Chekhov Chamber"); }

    /**
     * Updates the display for the selected room.
     * @param roomName The name of the room to display
     */
    private void updateRoomDisplay(String roomName) {
        currentRoom = roomName;
        roomNameLabel.setText(roomName);
        roomNameLabel.setTextFill(Color.WHITE);
        selectedSeats.clear();
        updateBookButtonState();
        updateAllLayouts();
    }

    /**
     * Updates all layout displays for the current room.
     */
    private void updateAllLayouts() {
        seatStatusMap.clear();
        layoutsContainer.getChildren().clear();

        for (String layoutType : layoutTypes) {
            VBox layoutBox = new VBox();
            layoutBox.setSpacing(10);

            Label titleLabel = new Label(layoutType);
            titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
            titleLabel.setStyle("-fx-background-color: #DDDDDD; -fx-padding: 5; -fx-alignment: center;");
            titleLabel.setPrefWidth(LAYOUT_WIDTH);
            titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

            Pane layoutPane = new Pane();
            layoutPane.setPrefSize(LAYOUT_WIDTH, LAYOUT_HEIGHT);
            layoutPane.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1;");

            int capacity = roomCapacities.get(currentRoom).get(layoutType);
            Label capacityLabel = new Label("Capacity: " + capacity + " seats");
            capacityLabel.setAlignment(javafx.geometry.Pos.CENTER);
            capacityLabel.setPrefWidth(LAYOUT_WIDTH);

            createLayout(layoutPane, layoutType, capacity);

            layoutBox.getChildren().addAll(titleLabel, layoutPane, capacityLabel);
            layoutsContainer.getChildren().add(layoutBox);
        }
    }

    /**
     * Creates the layout for a specific type and capacity.
     * @param container The pane to contain the layout
     * @param layoutType The type of layout to create
     * @param seatCount The number of seats in the layout
     */
    private void createLayout(Pane container, String layoutType, int seatCount) {
        switch (layoutType) {
            case "Classroom":
                createClassroomLayout(container, seatCount);
                break;
            case "Boardroom":
                createBoardroomLayout(container, seatCount);
                break;
            case "Presentation":
                createPresentationLayout(container, seatCount);
                break;
        }
        addDoorLabel(container);
    }

    /**
     * Creates a classroom-style layout with rows and an instructor desk.
     * @param container The pane to contain the layout
     * @param totalSeats The total number of seats to create
     */
    private void createClassroomLayout(Pane container, int totalSeats) {
        double centerX = container.getPrefWidth() / 2;
        double startY = 30;
        int seatsPerRow = Math.min(8, totalSeats);
        int rows = (int) Math.ceil((double) totalSeats / seatsPerRow);
        double rowSpacing = SEAT_HEIGHT + SEAT_SPACING;
        int seatCounter = 1;

        // Add instructor desk
        StackPane instructorDesk = new StackPane();
        instructorDesk.setPrefSize(80, 20);
        instructorDesk.setLayoutX(centerX - 40);
        instructorDesk.setLayoutY(startY + rows * rowSpacing + 30);
        instructorDesk.setStyle("-fx-background-color: #CCCCCC; -fx-background-radius: 5;");

        Label instructorLabel = new Label("INSTRUCTOR");
        instructorLabel.setFont(Font.font("System", FontWeight.BOLD, 8));
        instructorLabel.setTextFill(Color.BLACK);

        instructorDesk.getChildren().add(instructorLabel);
        container.getChildren().add(instructorDesk);

        // Create seat rows
        for (int row = 0; row < rows; row++) {
            int seatsInThisRow = (row == rows - 1 && totalSeats % seatsPerRow != 0) ?
                    totalSeats % seatsPerRow : seatsPerRow;
            double rowWidth = seatsInThisRow * (SEAT_WIDTH + SEAT_SPACING) - SEAT_SPACING;
            double rowStartX = centerX - (rowWidth / 2);

            for (int col = 0; col < seatsInThisRow; col++) {
                if (seatCounter > totalSeats) break;
                String seatId = currentRoom + "-Classroom-" + seatCounter;
                double x = rowStartX + col * (SEAT_WIDTH + SEAT_SPACING);
                double y = startY + row * rowSpacing;
                StackPane seat = createSeat(seatId, x, y, SeatStatus.AVAILABLE, seatCounter);
                container.getChildren().add(seat);
                seatCounter++;
            }
        }
    }

    /**
     * Creates a boardroom-style layout with seats around a central table.
     * @param container The pane to contain the layout
     * @param totalSeats The total number of seats to create
     */
    private void createBoardroomLayout(Pane container, int totalSeats) {
        double centerX = container.getPrefWidth() / 2;
        double centerY = container.getPrefHeight() / 2;

        // Create central table
        StackPane table = new StackPane();
        double tableWidth = 120;
        double tableHeight = 70;
        table.setPrefSize(tableWidth, tableHeight);
        table.setLayoutX(centerX - tableWidth / 2);
        table.setLayoutY(centerY - tableHeight / 2);
        table.setStyle("-fx-background-color: #A0522D; -fx-background-radius: 10;");

        Label tableLabel = new Label("TABLE");
        tableLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        tableLabel.setTextFill(Color.WHITE);

        table.getChildren().add(tableLabel);
        container.getChildren().add(table);

        // Calculate seat distribution
        int seatsPerLongSide = (int) Math.ceil(totalSeats / 4.0);
        int seatsPerShortSide = (totalSeats - 2 * seatsPerLongSide) / 2;
        if (seatsPerShortSide < 0) {
            seatsPerShortSide = 0;
            seatsPerLongSide = totalSeats / 2;
        }
        int remainingSeats = totalSeats - (2 * seatsPerLongSide + 2 * seatsPerShortSide);
        if (remainingSeats > 0) {
            seatsPerLongSide += remainingSeats / 2;
        }

        int seatCounter = 1;

        // Top seats
        double topY = centerY - tableHeight / 2 - SEAT_HEIGHT - SEAT_SPACING;
        double topSpacing = seatsPerLongSide > 1 ? (tableWidth - SEAT_WIDTH) / (seatsPerLongSide - 1) : 0;
        for (int i = 0; i < seatsPerLongSide && seatCounter <= totalSeats; i++) {
            double x = (centerX - tableWidth / 2) + (i * topSpacing);
            if (seatsPerLongSide == 1) x = centerX - SEAT_WIDTH / 2;
            String seatId = currentRoom + "-Boardroom-" + seatCounter;
            StackPane seat = createSeat(seatId, x, topY, SeatStatus.AVAILABLE, seatCounter);
            container.getChildren().add(seat);
            seatCounter++;
        }

        // Bottom seats
        double bottomY = centerY + tableHeight / 2 + SEAT_SPACING;
        double bottomSpacing = seatsPerLongSide > 1 ? (tableWidth - SEAT_WIDTH) / (seatsPerLongSide - 1) : 0;
        for (int i = 0; i < seatsPerLongSide && seatCounter <= totalSeats; i++) {
            double x = (centerX - tableWidth / 2) + (i * bottomSpacing);
            if (seatsPerLongSide == 1) x = centerX - SEAT_WIDTH / 2;
            String seatId = currentRoom + "-Boardroom-" + seatCounter;
            StackPane seat = createSeat(seatId, x, bottomY, SeatStatus.AVAILABLE, seatCounter);
            container.getChildren().add(seat);
            seatCounter++;
        }

        // Left seats
        double leftX = centerX - tableWidth / 2 - SEAT_WIDTH - SEAT_SPACING;
        double leftSpacing = seatsPerShortSide > 1 ? (tableHeight - SEAT_HEIGHT) / (seatsPerShortSide - 1) : 0;
        for (int i = 0; i < seatsPerShortSide && seatCounter <= totalSeats; i++) {
            double y = (centerY - tableHeight / 2) + (i * leftSpacing);
            if (seatsPerShortSide == 1) y = centerY - SEAT_HEIGHT / 2;
            String seatId = currentRoom + "-Boardroom-" + seatCounter;
            StackPane seat = createSeat(seatId, leftX, y, SeatStatus.AVAILABLE, seatCounter);
            container.getChildren().add(seat);
            seatCounter++;
        }

        // Right seats
        double rightX = centerX + tableWidth / 2 + SEAT_SPACING;
        double rightSpacing = seatsPerShortSide > 1 ? (tableHeight - SEAT_HEIGHT) / (seatsPerShortSide - 1) : 0;
        for (int i = 0; i < seatsPerShortSide && seatCounter <= totalSeats; i++) {
            double y = (centerY - tableHeight / 2) + (i * rightSpacing);
            if (seatsPerShortSide == 1) y = centerY - SEAT_HEIGHT / 2;
            String seatId = currentRoom + "-Boardroom-" + seatCounter;
            StackPane seat = createSeat(seatId, rightX, y, SeatStatus.AVAILABLE, seatCounter);
            container.getChildren().add(seat);
            seatCounter++;
        }
    }

    /**
     * Creates a presentation-style layout with rows facing a presentation area.
     * @param container The pane to contain the layout
     * @param totalSeats The total number of seats to create
     */
    private void createPresentationLayout(Pane container, int totalSeats) {
        double centerX = container.getPrefWidth() / 2;
        double startY = 30;
        int seatsPerRow = Math.min(10, totalSeats);
        int rows = (int) Math.ceil((double) totalSeats / seatsPerRow);
        double rowSpacing = SEAT_HEIGHT + SEAT_SPACING;
        int seatCounter = 1;

        // Add presentation area
        StackPane presentationArea = new StackPane();
        presentationArea.setPrefSize(150, 30);
        presentationArea.setLayoutX(centerX - 75);
        presentationArea.setLayoutY(startY + rows * rowSpacing + 30);
        presentationArea.setStyle("-fx-background-color: #CCCCCC; -fx-background-radius: 5;");

        Label presentationLabel = new Label("PRESENTATION");
        presentationLabel.setFont(Font.font("System", FontWeight.BOLD, 10));
        presentationLabel.setTextFill(Color.BLACK);

        presentationArea.getChildren().add(presentationLabel);
        container.getChildren().add(presentationArea);

        // Create seat rows
        for (int row = 0; row < rows; row++) {
            int seatsInThisRow = (row == rows - 1 && totalSeats % seatsPerRow != 0) ?
                    totalSeats % seatsPerRow : seatsPerRow;
            double rowWidth = seatsInThisRow * (SEAT_WIDTH + SEAT_SPACING) - SEAT_SPACING;
            double rowStartX = centerX - (rowWidth / 2);

            for (int col = 0; col < seatsInThisRow; col++) {
                if (seatCounter > totalSeats) break;
                String seatId = currentRoom + "-Presentation-" + seatCounter;
                double x = rowStartX + col * (SEAT_WIDTH + SEAT_SPACING);
                double y = startY + row * rowSpacing;
                StackPane seat = createSeat(seatId, x, y, SeatStatus.AVAILABLE, seatCounter);
                container.getChildren().add(seat);
                seatCounter++;
            }
        }
    }

    /**
     * Adds a door label to the layout pane.
     * @param container The pane to add the door label to
     */
    private void addDoorLabel(Pane container) {
        Label doorLabel = new Label("DOOR →");
        doorLabel.setFont(Font.font("System", FontWeight.BOLD, 10));
        doorLabel.setLayoutX(10);
        doorLabel.setLayoutY(10);
        container.getChildren().add(doorLabel);
    }

    /**
     * Creates a visual representation of a seat.
     * @param seatId Unique identifier for the seat
     * @param x X-coordinate position
     * @param y Y-coordinate position
     * @param defaultStatus Initial status of the seat
     * @param seatNumber Number to display on the seat
     * @return StackPane representing the seat
     */
    private StackPane createSeat(String seatId, double x, double y, SeatStatus defaultStatus, int seatNumber) {
        SeatStatus status = reservedSeats.contains(seatId) ? SeatStatus.RESERVED : defaultStatus;
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

        Label seatLabel = new Label(String.valueOf(seatNumber));
        seatLabel.setFont(Font.font("System", FontWeight.NORMAL, 6));
        seatLabel.setTextFill(Color.BLACK);
        seat.getChildren().add(seatLabel);

        Tooltip tooltip = new Tooltip("Seat " + seatNumber + " - " + status.getDescription());
        Tooltip.install(seat, tooltip);

        seatStatusMap.put(seatId, status);
        if (status == SeatStatus.AVAILABLE) {
            seat.setOnMouseClicked(event -> handleSeatClick(seatId, seat));
        }
        return seat;
    }

    /**
     * Handles mouse click events on seats for selection/deselection.
     * @param seatId The ID of the clicked seat
     * @param seat The StackPane representing the seat
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
     * Updates the visual appearance of a seat based on selection state.
     * @param seat The StackPane representing the seat
     * @param seatId The ID of the seat
     * @param isSelected Whether the seat is currently selected
     */
    private void updateSeatAppearance(StackPane seat, String seatId, boolean isSelected) {
        SeatStatus status = seatStatusMap.get(seatId);
        String style = "-fx-background-color: " + status.getBackgroundColor() + ";" +
                "-fx-border-color: " + status.getBorderColor() + ";" +
                "-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-width: " + (isSelected ? "2" : "1") + ";";
        if (isSelected) {
            style += "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);";
        }
        seat.setStyle(style);
    }

    /**
     * Updates the enabled/disabled state of the book button based on seat selection.
     */
    private void updateBookButtonState() {
        bookButton.setDisable(selectedSeats.isEmpty());
    }

    /**
     * Handles the booking of selected seats and shows confirmation dialog.
     */
    @FXML
    private void handleBookSeats() {
        if (!selectedSeats.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Booking Confirmation");
            alert.setHeaderText("Seat Booking - " + currentRoom);
            String seatsText = "You have selected the following seats:\n" +
                    selectedSeats.stream()
                            .map(seatId -> "Seat " + seatId.split("-")[2])
                            .collect(Collectors.joining(", "));
            alert.setContentText(seatsText);
            alert.showAndWait();

            reservedSeats.addAll(selectedSeats);
            selectedSeats.clear();
            updateAllLayouts();
        }
    }

    /**
     * Handles scroll events for zooming the layout display.
     * @param event The scroll event
     */
    private void handleScroll(ScrollEvent event) {
        if (event.isControlDown()) {
            double deltaY = event.getDeltaY();
            scaleFactor = Math.max(0.5, Math.min(3.0, deltaY > 0 ? scaleFactor * 1.1 : scaleFactor / 1.1));
            layoutsContainer.setScaleX(scaleFactor);
            layoutsContainer.setScaleY(scaleFactor);
            event.consume();
        }
    }
}