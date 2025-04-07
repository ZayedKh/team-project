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

public class TheaterSeatingController implements Initializable {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Pane seatingContainer;

    @FXML
    private Label eventLabel;

    @FXML
    private Button bookButton;

    private double scaleFactor = 1.0;

    private static final double SEAT_WIDTH = 20;
    private static final double SEAT_HEIGHT = 20;
    private static final double SEAT_SPACING = 5;

    private Map<String, SeatStatus> seatStatusMap = new HashMap<>();

    private Set<String> selectedSeats = new HashSet<>();

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
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        createSeatingLayout();
        addStageArea();
        initializeRandomSeatStatuses();
        updateSeatingDisplay();
    }

    private void createSeatingLayout() {
        seatingContainer.getChildren().clear();
        double centerX = 500;
        createBalconyAreas(centerX);
        createStallsArea(centerX);
        createSectionLabel("BALCONY", centerX - 50, 10);
        createSectionLabel("STALLS", centerX - 50, 200);
    }

    private void createBalconyAreas(double centerX) {

        createLeftBalconyArea();
        createRightBalconyArea();
        createCenterBalconyArea(centerX);
    }

    private void createCenterBalconyArea(double centerX) {
        String[] rowLabels = {"CC", "BB", "AA"};
        int[] startSeatNums = {1, 6, 21};
        int[] endSeatNums = {8, 23, 33};
        double centerBalconyStartY = 40;

        for (int rowIndex = 0; rowIndex < rowLabels.length; rowIndex++) {
            String row = rowLabels[rowIndex];
            int startSeat = startSeatNums[rowIndex];
            int endSeat = endSeatNums[rowIndex];
            int numSeats = endSeat - startSeat + 1;

            double totalWidthRow = numSeats * (SEAT_WIDTH + SEAT_SPACING) - SEAT_SPACING;
            double rowStartX = centerX - (totalWidthRow / 2);

            double y = centerBalconyStartY + rowIndex * (SEAT_HEIGHT + SEAT_SPACING);

            for (int seatNum = startSeat; seatNum <= endSeat; seatNum++) {
                String seatId = row + seatNum;
                double x = rowStartX + (seatNum - startSeat) * (SEAT_WIDTH + SEAT_SPACING);
                StackPane seat = createSeat(seatId, x, y, SeatStatus.AVAILABLE);
                seatingContainer.getChildren().add(seat);
            }
        }
    }

    private void createLeftBalconyArea() {
        double balconyStartX = 50;
        double balconyStartY = 40;

        int bbSeatCount = 5;
        double bbX = balconyStartX;
        for (int i = bbSeatCount; i >= 1; i--) {
            double y = balconyStartY + (bbSeatCount - i) * (SEAT_HEIGHT + SEAT_SPACING);
            String seatId = "BB" + i;
            StackPane seat = createSeat(seatId, bbX, y, SeatStatus.AVAILABLE);
            seatingContainer.getChildren().add(seat);
        }

        int aaSeatCount = 20;
        double aaX = balconyStartX + (SEAT_WIDTH + SEAT_SPACING) * 2;
        for (int i = aaSeatCount; i >= 1; i--) {
            double y = balconyStartY + (aaSeatCount - i) * (SEAT_HEIGHT + SEAT_SPACING);
            String seatId = "AA" + i;
            StackPane seat = createSeat(seatId, aaX, y, SeatStatus.AVAILABLE);
            seatingContainer.getChildren().add(seat);
        }
    }

    private void createRightBalconyArea() {
        double balconyStartX = 830;
        double balconyStartY = 40;


        int aaSeatCount = 20;
        double aaX = balconyStartX;
        for (int i = 0; i < aaSeatCount; i++) {
            double y = balconyStartY + i * (SEAT_HEIGHT + SEAT_SPACING);
            int seatNumber = 34 + i;
            String seatId = "AA" + seatNumber;
            StackPane seat = createSeat(seatId, aaX, y, SeatStatus.AVAILABLE);
            seatingContainer.getChildren().add(seat);
        }

        int bbSeatCount = 5;
        double bbX = balconyStartX + (SEAT_WIDTH + SEAT_SPACING) * 2;
        for (int i = 0; i < bbSeatCount; i++) {
            double y = balconyStartY + i * (SEAT_HEIGHT + SEAT_SPACING);
            int seatNumber = 24 + i;
            String seatId = "BB" + seatNumber;
            StackPane seat = createSeat(seatId, bbX, y, SeatStatus.AVAILABLE);
            seatingContainer.getChildren().add(seat);
        }
    }


    private void createStallsArea(double centerX) {

        String[] stallRows = {"Q", "P", "O", "N", "M", "L", "K", "J", "I", "H", "G", "F", "E", "D", "C", "B", "A"};
        double startY = 220;

        for (int rowIndex = 0; rowIndex < stallRows.length; rowIndex++) {
            String row = stallRows[rowIndex];

            int seatsPerRow;
            switch (row) {
                case "Q":
                    seatsPerRow = 10;
                    break;
                case "P":
                    seatsPerRow = 11;
                    break;
                case "O":
                    seatsPerRow = 20;
                    break;
                case "M":
                    seatsPerRow = 16;
                    break;
                default:
                    seatsPerRow = 19;
                    break;
            }

            double totalWidthRow = seatsPerRow * (SEAT_WIDTH + SEAT_SPACING) - SEAT_SPACING;
            double startX = centerX - (totalWidthRow / 2);
            double y = startY + rowIndex * (SEAT_HEIGHT + SEAT_SPACING);

            for (int seatNum = 1; seatNum <= seatsPerRow; seatNum++) {
                String seatId = row + seatNum;
                double x = startX + (seatNum - 1) * (SEAT_WIDTH + SEAT_SPACING);
                StackPane seat = createSeat(seatId, x, y, SeatStatus.AVAILABLE);
                seatingContainer.getChildren().add(seat);
            }
        }
    }


    private void addStageArea() {
        double maxY = 0;
        for (Node node : seatingContainer.getChildren()) {
            if (node instanceof StackPane) {
                double seatY = node.getLayoutY() + SEAT_HEIGHT;
                if (seatY > maxY) maxY = seatY;
            }
        }
        StackPane stageArea = new StackPane();
        stageArea.setPrefSize(1000, 25);
        stageArea.setLayoutX(0);
        stageArea.setLayoutY(maxY + 5);
        stageArea.setStyle("-fx-background-color: #CCCCCC; -fx-background-radius: 5 5 0 0;");
        Label stageLabel = new Label("STAGE");
        stageLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        stageArea.getChildren().add(stageLabel);
        seatingContainer.getChildren().add(stageArea);
    }

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

        seat.setOnMouseClicked(event -> handleSeatClick(seatId, seat));

        Tooltip tooltip = new Tooltip(seatId + " - " + status.getDescription());
        Tooltip.install(seat, tooltip);

        return seat;
    }

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

    private void updateSeatAppearance(StackPane seat, String seatId, boolean isSelected) {
        SeatStatus status = seatStatusMap.get(seatId);
        String style = "-fx-background-color: " + status.getBackgroundColor() + ";" +
                "-fx-border-color: " + status.getBorderColor() + ";" +
                "-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-width: " + (isSelected ? "2" : "1") + ";";
        if (isSelected) style += "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);";
        seat.setStyle(style);
    }

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

    private void updateBookButtonState() {
        bookButton.setDisable(selectedSeats.isEmpty());
    }

    private void initializeRandomSeatStatuses() {
        Random random = new Random();
        for (String seatId : seatStatusMap.keySet()) {
            int rand = random.nextInt(10);
            seatStatusMap.put(seatId, rand < 6 ? SeatStatus.AVAILABLE : rand < 8 ? SeatStatus.RESERVED : SeatStatus.UNAVAILABLE);
        }
    }

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

    private void handleScroll(ScrollEvent event) {
        if (event.isControlDown()) {
            double deltaY = event.getDeltaY();
            scaleFactor = Math.max(0.5, Math.min(3.0, deltaY > 0 ? scaleFactor * 1.1 : scaleFactor / 1.1));
            seatingContainer.setScaleX(scaleFactor);
            seatingContainer.setScaleY(scaleFactor);
            event.consume();
        }
    }

    public void updateSeatStatus(String seatId, SeatStatus status) {
        if (seatStatusMap.containsKey(seatId)) {
            seatStatusMap.put(seatId, status);
            updateSeatingDisplay();
        }
    }

    private void createSectionLabel(String text, double x, double y) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.BOLD, 14));
        label.setTextFill(Color.BLACK);
        label.setLayoutX(x);
        label.setLayoutY(y);
        seatingContainer.getChildren().add(label);
    }

    public void setEventInfo(String eventName, Date eventDate) {
        eventLabel.setText("Current Event: " + eventName + " - " + eventDate);
    }
}