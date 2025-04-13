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
 * Controller class for managing room layouts in the Lancaster application.
 * <p>
 * This class is responsible for displaying various room layouts and supporting seat selection,
 * booking, and zooming functionality. It handles user interactions, updates the UI components
 * (layouts, seat appearance, and booking state), and manages room-specific data such as seat capacities.
 * </p>
 */
public class RoomLayoutController implements Initializable {

    @FXML private ScrollPane scrollPane;           // Scroll pane for the main content.
    @FXML private HBox layoutsContainer;           // Container that holds the room layout displays.
    @FXML private Label roomNameLabel;             // Label to display the name of the currently selected room.
    @FXML private Button bookButton;               // Button that confirms the booking of selected seats.
    @FXML private ToggleGroup roomToggleGroup;     // Group that manages the selection of room toggle buttons.
    @FXML private ToggleButton greenRoomToggle;    // Toggle button for selecting the Green Room.
    @FXML private ToggleButton bronteBoardroomToggle;  // Toggle button for selecting the Brontë Boardroom.
    @FXML private ToggleButton dickensDenToggle;   // Toggle button for selecting the Dickens Den.
    @FXML private ToggleButton poeParlorToggle;    // Toggle button for selecting the Poe Parlor.
    @FXML private ToggleButton globeRoomToggle;    // Toggle button for selecting the Globe Room.
    @FXML private ToggleButton chekhovChamberToggle;  // Toggle button for selecting the Chekhov Chamber.

    private double scaleFactor = 1.0;              // Current zoom scale factor for room layouts.
    private static final double SEAT_WIDTH = 20;   // The width (in pixels) of each seat.
    private static final double SEAT_HEIGHT = 20;  // The height (in pixels) of each seat.
    private static final double SEAT_SPACING = 10; // The spacing (in pixels) between seats.
    private static final int LAYOUT_WIDTH = 300;   // The fixed width (in pixels) of each layout pane.
    private static final int LAYOUT_HEIGHT = 350;  // The fixed height (in pixels) of each layout pane.

    // Temporary map for keeping track of the current status of each seat.
    private Map<String, SeatStatus> seatStatusMap = new HashMap<>();
    // Set to store the identifiers of seats that have already been reserved.
    private Set<String> reservedSeats = new HashSet<>();
    // Set to store the identifiers of seats that are currently selected (but not booked yet).
    private Set<String> selectedSeats = new HashSet<>();

    // Map representing room capacities for different layout types keyed by room name.
    private final Map<String, Map<String, Integer>> roomCapacities = new HashMap<>();
    private String currentRoom = "Green Room";     // The currently selected room.
    private String[] layoutTypes = {"Classroom", "Boardroom", "Presentation"};  // The available layout types.

    /**
     * Enum representing the status of a seat.
     * <p>
     * Each enum value defines its corresponding background color, border color, and a descriptive label.
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

        public String getBackgroundColor() { return backgroundColor; }
        public String getBorderColor() { return borderColor; }
        public String getDescription() { return description; }
    }

    /**
     * Initializes the controller after its root element has been completely processed.
     * <p>
     * This method initializes room capacities, sets up the zoom handler on the scroll pane,
     * applies custom styles to room selection toggle buttons, selects the default room,
     * and updates the display to show the default room layout.
     * </p>
     *
     * @param location  The location used to resolve relative paths for the root object, or {@code null} if unknown.
     * @param resources The resources used to localize the root object, or {@code null} if not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeRoomCapacities();  // Populate the map with room capacities.
        scrollPane.setOnScroll(this::handleScroll);  // Set up the scroll handler for zoom functionality.
        scrollPane.setFitToWidth(true);  // Ensure the content fits the scroll pane width.
        scrollPane.setFitToHeight(true);  // Ensure the content fits the scroll pane height.
        applyToggleButtonStyles();  // Apply consistent styling to room toggle buttons.
        greenRoomToggle.setSelected(true);  // Set Green Room as the default selected room.
        updateRoomDisplay("Green Room");  // Update the UI to display the Green Room layout.
    }

    /**
     * Applies styles to the room toggle buttons.
     * <p>
     * Each toggle button is given a base style and a distinct style when selected.
     * This method iterates over all defined room toggles and sets the appropriate style based on selection state.
     * </p>
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
     * Initializes the room capacities for each room and layout type.
     * <p>
     * This method populates the {@code roomCapacities} map with predefined numbers for each room and layout category.
     * </p>
     */
    private void initializeRoomCapacities() {
        roomCapacities.put("Green Room", Map.of("Classroom", 12, "Boardroom", 10, "Presentation", 20));
        roomCapacities.put("Brontë Boardroom", Map.of("Classroom", 25, "Boardroom", 18, "Presentation", 40));
        roomCapacities.put("Dickens Den", Map.of("Classroom", 15, "Boardroom", 12, "Presentation", 25));
        roomCapacities.put("Poe Parlor", Map.of("Classroom", 20, "Boardroom", 14, "Presentation", 30));
        roomCapacities.put("Globe Room", Map.of("Classroom", 30, "Boardroom", 20, "Presentation", 50));
        roomCapacities.put("Chekhov Chamber", Map.of("Classroom", 18, "Boardroom", 16, "Presentation", 35));
    }

    // **Toggle Handlers for Room Selection**

    /**
     * Handles the Green Room toggle action.
     * <p>
     * Updates the room display to the Green Room.
     * </p>
     */
    @FXML private void handleGreenRoomToggle() { updateRoomDisplay("Green Room"); }

    /**
     * Handles the Brontë Boardroom toggle action.
     * <p>
     * Updates the room display to the Brontë Boardroom.
     * </p>
     */
    @FXML private void handleBronteBoardroomToggle() { updateRoomDisplay("Brontë Boardroom"); }

    /**
     * Handles the Dickens Den toggle action.
     * <p>
     * Updates the room display to the Dickens Den.
     * </p>
     */
    @FXML private void handleDickensDenToggle() { updateRoomDisplay("Dickens Den"); }

    /**
     * Handles the Poe Parlor toggle action.
     * <p>
     * Updates the room display to the Poe Parlor.
     * </p>
     */
    @FXML private void handlePoeParlorToggle() { updateRoomDisplay("Poe Parlor"); }

    /**
     * Handles the Globe Room toggle action.
     * <p>
     * Updates the room display to the Globe Room.
     * </p>
     */
    @FXML private void handleGlobeRoomToggle() { updateRoomDisplay("Globe Room"); }

    /**
     * Handles the Chekhov Chamber toggle action.
     * <p>
     * Updates the room display to the Chekhov Chamber.
     * </p>
     */
    @FXML private void handleChekhovChamberToggle() { updateRoomDisplay("Chekhov Chamber"); }

    /**
     * Updates the room display when a new room is selected.
     * <p>
     * This method clears any previously selected seats, updates the room name label,
     * and refreshes the layout displays for the newly selected room.
     * </p>
     *
     * @param roomName The name of the room to be displayed.
     */
    private void updateRoomDisplay(String roomName) {
        currentRoom = roomName;
        roomNameLabel.setText(roomName);
        selectedSeats.clear();  // Clear current seat selections.
        updateBookButtonState();  // Refresh the state of the book button.
        updateAllLayouts();  // Rebuild all layout displays for the current room.
    }

    /**
     * Updates all layout displays for the current room.
     * <p>
     * This method clears the temporary seat status data and the layouts container,
     * then iterates through each available layout type to create and add new layout panes.
     * </p>
     */
    private void updateAllLayouts() {
        seatStatusMap.clear();  // Clear previous seat statuses.
        layoutsContainer.getChildren().clear();  // Clear existing layout displays.

        for (String layoutType : layoutTypes) {
            VBox layoutBox = new VBox();
            layoutBox.setSpacing(10);

            // Create and style the layout title.
            Label titleLabel = new Label(layoutType);
            titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
            titleLabel.setStyle("-fx-background-color: #DDDDDD; -fx-padding: 5; -fx-alignment: center;");
            titleLabel.setPrefWidth(LAYOUT_WIDTH);
            titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

            // Create the layout pane.
            Pane layoutPane = new Pane();
            layoutPane.setPrefSize(LAYOUT_WIDTH, LAYOUT_HEIGHT);
            layoutPane.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1;");

            // Retrieve the capacity for the current layout type.
            int capacity = roomCapacities.get(currentRoom).get(layoutType);
            Label capacityLabel = new Label("Capacity: " + capacity + " seats");
            capacityLabel.setAlignment(javafx.geometry.Pos.CENTER);
            capacityLabel.setPrefWidth(LAYOUT_WIDTH);

            // Create the specific layout based on type.
            createLayout(layoutPane, layoutType, capacity);

            // Combine title, layout pane, and capacity label into one container.
            layoutBox.getChildren().addAll(titleLabel, layoutPane, capacityLabel);
            layoutsContainer.getChildren().add(layoutBox);
        }
    }

    /**
     * Creates a specific layout type within the provided container.
     * <p>
     * This method selects the appropriate layout creation routine based on the layout type,
     * and always adds a door label to the layout pane.
     * </p>
     *
     * @param container  The pane where the layout will be added.
     * @param layoutType The type of layout to create (e.g., Classroom, Boardroom, Presentation).
     * @param seatCount  The total number of seats to create for the layout.
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
        addDoorLabel(container);  // Append a door label to the layout.
    }

    /**
     * Creates a classroom layout with rows of seats and an instructor desk.
     * <p>
     * Seats are arranged in rows with center alignment, and an instructor desk is placed below the seats.
     * </p>
     *
     * @param container  The pane to which the classroom layout is added.
     * @param totalSeats The total number of seats in the layout.
     */
    private void createClassroomLayout(Pane container, int totalSeats) {
        double centerX = container.getPrefWidth() / 2;
        double startY = 30;
        int seatsPerRow = Math.min(8, totalSeats);
        int rows = (int) Math.ceil((double) totalSeats / seatsPerRow);
        double rowSpacing = SEAT_HEIGHT + SEAT_SPACING;
        int seatCounter = 1;

        // Create the instructor desk.
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

        // Create seats arranged in rows.
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
     * Creates a boardroom layout with seats around a central table.
     * <p>
     * A central table is created in the middle of the pane, with seats distributed around its perimeter.
     * </p>
     *
     * @param container  The pane to which the boardroom layout is added.
     * @param totalSeats The total number of seats in the layout.
     */
    private void createBoardroomLayout(Pane container, int totalSeats) {
        double centerX = container.getPrefWidth() / 2;
        double centerY = container.getPrefHeight() / 2;

        // Create the central table.
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

        // Determine the number of seats for each side.
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

        // Create top side seats.
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

        // Create bottom side seats.
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

        // Create left side seats.
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

        // Create right side seats.
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
     * Creates a presentation layout with rows of seats facing a presentation area.
     * <p>
     * In this layout, seats are arranged in rows and a designated presentation area is placed below.
     * </p>
     *
     * @param container  The pane to which the presentation layout is added.
     * @param totalSeats The total number of seats in this layout.
     */
    private void createPresentationLayout(Pane container, int totalSeats) {
        double centerX = container.getPrefWidth() / 2;
        double startY = 30;
        int seatsPerRow = Math.min(10, totalSeats);
        int rows = (int) Math.ceil((double) totalSeats / seatsPerRow);
        double rowSpacing = SEAT_HEIGHT + SEAT_SPACING;
        int seatCounter = 1;

        // Create the presentation area.
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

        // Arrange seats in rows.
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
     * Adds a door label to the given layout container.
     * <p>
     * The door label helps indicate the entry point within the layout.
     * </p>
     *
     * @param container The pane to which the door label will be added.
     */
    private void addDoorLabel(Pane container) {
        Label doorLabel = new Label("DOOR →");
        doorLabel.setFont(Font.font("System", FontWeight.BOLD, 10));
        doorLabel.setLayoutX(10);
        doorLabel.setLayoutY(10);
        container.getChildren().add(doorLabel);
    }

    /**
     * Creates a seat as a StackPane with given properties.
     * <p>
     * The seat displays a number label, applies its status colors,
     * installs a tooltip with seat information, and sets an on-click handler for selection if available.
     * </p>
     *
     * @param seatId        A unique identifier for the seat.
     * @param x             The x-coordinate of the seat in the layout.
     * @param y             The y-coordinate of the seat in the layout.
     * @param defaultStatus The default status to use for the seat.
     * @param seatNumber    The display number of the seat.
     * @return A StackPane representing the seat.
     */
    private StackPane createSeat(String seatId, double x, double y, SeatStatus defaultStatus, int seatNumber) {
        // Use RESERVED status if the seat has been booked.
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

        // Update the temporary seat status map.
        seatStatusMap.put(seatId, status);
        // If the seat is available, allow it to be selectable.
        if (status == SeatStatus.AVAILABLE) {
            seat.setOnMouseClicked(event -> handleSeatClick(seatId, seat));
        }
        return seat;
    }

    /**
     * Handles the click event on a seat.
     * <p>
     * If the seat is available, this method toggles its selection state and updates the appearance accordingly.
     * It also refreshes the state of the book button.
     * </p>
     *
     * @param seatId The unique identifier of the clicked seat.
     * @param seat   The StackPane representing the seat.
     */
    private void handleSeatClick(String seatId, StackPane seat) {
        // Only allow selection if the seat is available.
        if (seatStatusMap.get(seatId) == SeatStatus.AVAILABLE) {
            if (selectedSeats.contains(seatId)) {
                selectedSeats.remove(seatId);  // Deselect the seat.
                updateSeatAppearance(seat, seatId, false);
            } else {
                selectedSeats.add(seatId);  // Select the seat.
                updateSeatAppearance(seat, seatId, true);
            }
            updateBookButtonState();  // Refresh the book button's enabled/disabled state.
        }
    }

    /**
     * Updates the visual appearance of a seat based on its selection state.
     * <p>
     * When a seat is selected, additional styling such as a drop shadow and thicker border are applied.
     * </p>
     *
     * @param seat       The StackPane representing the seat.
     * @param seatId     The unique identifier of the seat.
     * @param isSelected A boolean indicating whether the seat is selected.
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
     * Updates the enabled state of the book button.
     * <p>
     * The book button is enabled only if there is at least one seat selected; otherwise, it is disabled.
     * </p>
     */
    private void updateBookButtonState() {
        bookButton.setDisable(selectedSeats.isEmpty());
    }

    /**
     * Handles the booking process for selected seats.
     * <p>
     * When invoked, a confirmation dialog is shown with the details of selected seats.
     * Upon confirmation, the seats are marked as reserved, the selected set is cleared,
     * and all layouts are refreshed to reflect the updated statuses.
     * </p>
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

            reservedSeats.addAll(selectedSeats);  // Mark the selected seats as reserved.
            selectedSeats.clear();  // Clear the selection.
            updateAllLayouts();  // Refresh the layouts to update seat statuses.
        }
    }

    /**
     * Handles scroll events to implement zoom functionality on the room layouts.
     * <p>
     * When the Control key is pressed during scrolling, the scale factor is adjusted (zoom in or out)
     * within the range of 0.5 to 3.0. The updated scale factor is then applied to the layouts container.
     * </p>
     *
     * @param event The {@link ScrollEvent} triggered by the user.
     */
    private void handleScroll(ScrollEvent event) {
        if (event.isControlDown()) {  // Perform zoom only if the Control key is pressed.
            double deltaY = event.getDeltaY();
            scaleFactor = Math.max(0.5, Math.min(3.0, deltaY > 0 ? scaleFactor * 1.1 : scaleFactor / 1.1));
            layoutsContainer.setScaleX(scaleFactor);
            layoutsContainer.setScaleY(scaleFactor);
            event.consume();  // Consume the event to prevent the default scroll behavior.
        }
    }
}
