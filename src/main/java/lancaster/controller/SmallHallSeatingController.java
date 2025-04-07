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

public class SmallHallSeatingController implements Initializable {

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
    private static final double AISLE_WIDTH = 15;

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

    private void createSeatingLayout() {
        seatingContainer.getChildren().clear();
        double centerX = 400;
        createStallsArea(centerX);
        createSoundDesk(centerX);
    }

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

            for (int seatNum = 1; seatNum <= seatsPerRow; seatNum++) {
                String seatId = row + seatNum;
                double x = startX + (seatNum - 1) * (SEAT_WIDTH + SEAT_SPACING);
                StackPane seat = createSeat(seatId, x, y, SeatStatus.AVAILABLE);
                seatingContainer.getChildren().add(seat);
            }

            Label rowLabel = new Label(row);
            rowLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            rowLabel.setLayoutX(startX - 25);
            rowLabel.setLayoutY(y);
            seatingContainer.getChildren().add(rowLabel);
        }
    }

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

    public void setEventInfo(String eventName, Date eventDate) {
        eventLabel.setText("Current Event: " + eventName + " - " + eventDate);
    }
}