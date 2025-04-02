package lancaster.ui;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lancaster.model.Review;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReviewUI extends Application {

    private final ObservableList<Review> reviews = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Lancaster's Music Hall - Reviews");

        TableView<Review> reviewTable = new TableView<>();
        reviewTable.setItems(reviews);

        TableColumn<Review, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));

        TableColumn<Review, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAuthor()));

        TableColumn<Review, Integer> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        ratingCol.setCellFactory(column -> new TableCell<Review, Integer>() {
            @Override
            protected void updateItem(Integer rating, boolean empty) {
                super.updateItem(rating, empty);
                if (empty || rating == null) {
                    setText(null);
                } else {
                    int filledStars = rating;
                    int emptyStars = 5 - rating;
                    StringBuilder stars = new StringBuilder();
                    for (int i = 0; i < filledStars; i++) stars.append("★");
                    for (int i = 0; i < emptyStars; i++) stars.append("☆");
                    setText(stars.toString());
                    setStyle("-fx-text-fill: gold; -fx-font-size: 14px;");
                }
            }
        });

        TableColumn<Review, String> roomCol = new TableColumn<>("Room");
        roomCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRoom()));

        TableColumn<Review, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        descCol.setStyle("-fx-text-fill: #4CAF50;");

        TableColumn<Review, String> timeCol = new TableColumn<>("Submitted At");
        timeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTimestamp()));

        reviewTable.getColumns().addAll(titleCol, authorCol, ratingCol, roomCol, descCol, timeCol);
        reviewTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        reviewTable.setStyle("-fx-control-inner-background: #1F2426; -fx-table-cell-border-color: #4CAF50; -fx-border-color: #4CAF50; -fx-text-fill: #4CAF50;");
        reviewTable.setRowFactory(tv -> {
            TableRow<Review> row = new TableRow<>();
            row.setStyle("-fx-border-color: #4CAF50;");
            return row;
        });

        Label formTitle = new Label("Add a New Review");
        formTitle.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");

        TextField titleField = new TextField();
        titleField.setPromptText("Review Title");
        titleField.setStyle("-fx-background-color: #2B2F31; -fx-text-fill: #4CAF50; -fx-border-color: #555555;");

        TextField authorField = new TextField();
        authorField.setPromptText("Author");
        authorField.setStyle("-fx-background-color: #2B2F31; -fx-text-fill: #4CAF50; -fx-border-color: #555555;");

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Description");
        descriptionArea.setWrapText(true);
        descriptionArea.setStyle("-fx-control-inner-background: #2B2F31; -fx-background-color: #2B2F31; -fx-text-fill: #4CAF50; -fx-border-color: #555;");

        ComboBox<String> roomComboBox = new ComboBox<>();
        roomComboBox.getItems().addAll("The Green Room", "Brontë Boardroom", "Dickens Den", "Poe Parlor", "Globe Room", "Chekhov Chamber");
        roomComboBox.setPromptText("Select Room");
        roomComboBox.setStyle("-fx-background-color: #2B2F31; -fx-text-fill: #4CAF50; -fx-border-color: #555;");

        Spinner<Integer> ratingSpinner = new Spinner<>(1, 5, 3);
        ratingSpinner.setStyle("-fx-background-color: #2A2E2E; -fx-border-color: #00FF00; -fx-border-width: 1; -fx-border-radius: 4; -fx-text-fill: green;");

        Label ratingLabel = new Label("Rating:");
        ratingLabel.setStyle("-fx-text-fill: #4CAF50;");

        Button submitButton = new Button("Add Review");
        submitButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        submitButton.setOnAction(e -> {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String description = descriptionArea.getText().trim();
            String room = roomComboBox.getValue();
            Integer rating = ratingSpinner.getValue();

            if (title.isEmpty() || author.isEmpty() || room == null || rating == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Missing Required Fields");
                alert.setHeaderText("All Required Fields Must Be Filled");
                alert.setContentText("Please fill in the following fields: Title, Author, Room, Rating");
                alert.showAndWait();
                return;
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));


            Review newReview = new Review(
                    reviews.size() + 1,
                    rating,
                    author,
                    title,
                    description,
                    room
            );
            newReview.setTimestamp(timestamp);
            reviews.add(newReview);
            titleField.clear();
            authorField.clear();
            descriptionArea.clear();
            roomComboBox.getSelectionModel().clearSelection();
            ratingSpinner.getValueFactory().setValue(3);
        });

        VBox form = new VBox(10,
                formTitle,
                titleField,
                authorField,
                descriptionArea,
                roomComboBox,
                new HBox(10, ratingLabel, ratingSpinner),
                submitButton
        );
        form.setPadding(new Insets(10));
        form.setStyle("-fx-background-color: #2A2F30;");
        form.setBorder(new Border(new BorderStroke(Color.web("#4CAF50"), BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));

        VBox root = new VBox(15, reviewTable, form);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #1F2426;");

        Scene scene = new Scene(root, 950, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}