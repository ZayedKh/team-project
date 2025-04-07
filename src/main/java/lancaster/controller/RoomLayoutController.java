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

public class RoomLayoutController implements Initializable {

    @FXML private ScrollPane scrollPane;
    @FXML private HBox layoutsContainer;
    @FXML private Label roomNameLabel;
    @FXML private Button bookButton;
    @FXML private ToggleGroup roomToggleGroup;
    @FXML private ToggleButton greenRoomToggle;
    @FXML private ToggleButton bronteBoardroomToggle;
    @FXML private ToggleButton dickensDenToggle;
    @FXML private ToggleButton poeParlorToggle;
    @FXML private ToggleButton globeRoomToggle;
    @FXML private ToggleButton chekhovChamberToggle;

    private double scaleFactor = 1.0;
    private static final double SEAT_WIDTH = 20;
    private static final double SEAT_HEIGHT = 20;
    private static final double SEAT_SPACING = 10;
    private static final int LAYOUT_WIDTH = 300;
    private static final int LAYOUT_HEIGHT = 350;

    // This map is used temporarily while creating layouts.
    private Map<String, SeatStatus> seatStatusMap = new HashMap<>();
    // This set tracks seats that have been reserved (booked) persistently.
    private Set<String> reservedSeats = new HashSet<>();
    // This set tracks seats that are currently selected by the user (but not yet booked).
    private Set<String> selectedSeats = new HashSet<>();

    private final Map<String, Map<String, Integer>> roomCapacities = new HashMap<>();
    private String currentRoom = "Green Room";
    private String[] layoutTypes = {"Classroom", "Boardroom", "Presentation"};

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

    private void initializeRoomCapacities() {
        roomCapacities.put("Green Room", Map.of("Classroom", 12, "Boardroom", 10, "Presentation", 20));
        roomCapacities.put("Brontë Boardroom", Map.of("Classroom", 25, "Boardroom", 18, "Presentation", 40));
        roomCapacities.put("Dickens Den", Map.of("Classroom", 15, "Boardroom", 12, "Presentation", 25));
        roomCapacities.put("Poe Parlor", Map.of("Classroom", 20, "Boardroom", 14, "Presentation", 30));
        roomCapacities.put("Globe Room", Map.of("Classroom", 30, "Boardroom", 20, "Presentation", 50));
        roomCapacities.put("Chekhov Chamber", Map.of("Classroom", 18, "Boardroom", 16, "Presentation", 35));
    }

    @FXML private void handleGreenRoomToggle() { updateRoomDisplay("Green Room"); }
    @FXML private void handleBronteBoardroomToggle() { updateRoomDisplay("Brontë Boardroom"); }
    @FXML private void handleDickensDenToggle() { updateRoomDisplay("Dickens Den"); }
    @FXML private void handlePoeParlorToggle() { updateRoomDisplay("Poe Parlor"); }
    @FXML private void handleGlobeRoomToggle() { updateRoomDisplay("Globe Room"); }
    @FXML private void handleChekhovChamberToggle() { updateRoomDisplay("Chekhov Chamber"); }

    private void updateRoomDisplay(String roomName) {
        currentRoom = roomName;
        roomNameLabel.setText(roomName);
        selectedSeats.clear();
        updateBookButtonState();
        updateAllLayouts();
    }

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

    private void createClassroomLayout(Pane container, int totalSeats) {
        double centerX = container.getPrefWidth() / 2;
        double startY = 30;
        int seatsPerRow = Math.min(8, totalSeats);
        int rows = (int) Math.ceil((double) totalSeats / seatsPerRow);
        double rowSpacing = SEAT_HEIGHT + SEAT_SPACING;
        int seatCounter = 1;

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

    private void createBoardroomLayout(Pane container, int totalSeats) {
        double centerX = container.getPrefWidth() / 2;
        double centerY = container.getPrefHeight() / 2;

        // Table
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

        // Top side
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

        // Bottom side
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

        // Left side
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

        // Right side
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

    private void createPresentationLayout(Pane container, int totalSeats) {
        double centerX = container.getPrefWidth() / 2;
        double startY = 30;
        int seatsPerRow = Math.min(10, totalSeats);
        int rows = (int) Math.ceil((double) totalSeats / seatsPerRow);
        double rowSpacing = SEAT_HEIGHT + SEAT_SPACING;
        int seatCounter = 1;

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

    private void addDoorLabel(Pane container) {
        Label doorLabel = new Label("DOOR →");
        doorLabel.setFont(Font.font("System", FontWeight.BOLD, 10));
        doorLabel.setLayoutX(10);
        doorLabel.setLayoutY(10);
        container.getChildren().add(doorLabel);
    }

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

        // Update the temporary map.
        seatStatusMap.put(seatId, status);
        // Allow selection only if the seat is available.
        if (status == SeatStatus.AVAILABLE) {
            seat.setOnMouseClicked(event -> handleSeatClick(seatId, seat));
        }
        return seat;
    }

    private void handleSeatClick(String seatId, StackPane seat) {
        // Only allow selection if the seat is available.
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

    private void updateBookButtonState() {
        bookButton.setDisable(selectedSeats.isEmpty());
    }

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
