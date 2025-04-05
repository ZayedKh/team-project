package lancaster.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class BookingTypeSelectionUI {
    private StackPane mainView;
    private Node calendarView;
    private AnchorPaneNode anchorPaneNode;
    private BookingManager bookingManager;

    public BookingTypeSelectionUI(StackPane mainView, Node calendarView, AnchorPaneNode anchorPaneNode, BookingManager bookingManager) {
        this.mainView = mainView;
        this.calendarView = calendarView;
        this.anchorPaneNode = anchorPaneNode;
        this.bookingManager = bookingManager;
    }

    public void show() {
        BorderPane typeScreen = new BorderPane();
        typeScreen.setPadding(new Insets(30));
        typeScreen.setPrefSize(800, 600);
        typeScreen.setStyle("-fx-background-color: #122023;");

        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15));

        Button backButton = new Button("Back");
        backButton.setPrefSize(100, 30);
        backButton.setStyle("-fx-font-size: 14px;");
        backButton.setOnAction(e -> mainView.getChildren().setAll(calendarView));

        Label titleLabel = new Label("Select Booking Type");
        StackPane titleWrapper = new StackPane();
        titleWrapper.setStyle("-fx-background-color: #2ECC40; -fx-padding: 10px 25px; -fx-background-radius: 5px;");
        titleWrapper.setPadding(new Insets(8));
        titleWrapper.getChildren().add(titleLabel);
        titleLabel.setStyle("-fx-font-size: 20px;");

        header.getChildren().addAll(backButton, titleWrapper);
        typeScreen.setTop(header);

        VBox options = new VBox(20);
        options.setAlignment(Pos.CENTER);

        Button singleBookingButton = new Button("Single Day Booking");
        singleBookingButton.setPrefSize(200, 50);
        singleBookingButton.setStyle("-fx-font-size: 16px;");
        singleBookingButton.setOnAction(e -> new SingleBookingUI(mainView, calendarView, anchorPaneNode, bookingManager).showSingleBookingScreen());

        Button multiDayBookingButton = new Button("Multi-day Booking");
        multiDayBookingButton.setPrefSize(200, 50);
        multiDayBookingButton.setStyle("-fx-font-size: 16px;");
        multiDayBookingButton.setOnAction(e -> new MultiDayBookingUI(mainView, calendarView, anchorPaneNode, bookingManager).showMultiDayBookingScreen());

        options.getChildren().addAll(singleBookingButton, multiDayBookingButton);
        typeScreen.setCenter(options);

        mainView.getChildren().setAll(typeScreen);
    }
}