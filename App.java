package com.mycompany.cloudexify.project1;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class App extends Application {

    public Game gameInstance;

    public TabPane tabPane;
    public Tab tab1, tab2;
    
    public Label l1, l2, l3;
    public TextField t1;
    public Button b1, b2;
    public ComboBox<String> cb1;
    
    public Label l4;
    public Button b4;

    @Override
    public void start(Stage primaryStage) {
        gameInstance = new Game();

        primaryStage.setTitle("CloudExify  Guessing   Game");

        tab1 = new Tab("Play Game");
        tab1.setClosable(false);

        l1 = new Label("Select Difficulty:");
        cb1 = new ComboBox<>();
        cb1.getItems().addAll("Easy (1-50)", "Hard (1-200)");
        cb1.setValue("Easy (1-50)");

        b1 = new Button("Start New Game");
        l2 = new Label("Click Start to begin!");
        
        TextField t1 = new TextField();
        t1.setPromptText("Enter your guess");
        t1.setDisable(true);
        
        b2 = new Button("Submit Guess");
        b2.setDisable(true);
        
        l3 = new Label("Attempts: 0 / Max: 0");

        VBox v1 = new VBox(15);
        v1.setPadding(new Insets(20));
        v1.setAlignment(Pos.CENTER);
        
        HBox h1 = new HBox(10);
        h1.setAlignment(Pos.CENTER);
        h1.getChildren().addAll(l1, cb1, b1);

        HBox h2 = new HBox(10);
        h2.setAlignment(Pos.CENTER);
        h2.getChildren().addAll(t1, b2);

        v1.getChildren().addAll(h1, l2, h2, l3);
        tab1.setContent(v1);

        tab2 = new Tab("Game Stats");
        tab2.setClosable(false);

        int recordValue = gameInstance.bestScore;
        l4 = new Label("Best Score: " + (recordValue == Integer.MAX_VALUE ? "No score yet" : recordValue + " attempts"));
        l4.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        b4 = new Button("Reset Best Score");
        
        VBox v2 = new VBox(20);
        v2.setPadding(new Insets(20));
        v2.setAlignment(Pos.CENTER);
        v2.getChildren().addAll(l4, b4);
        tab2.setContent(v2);

        tabPane = new TabPane();
        tabPane.getTabs().addAll(tab1, tab2);

        b1.setOnAction(e -> handleStartButton());
        b2.setOnAction(e -> handleGuessButton());
        b4.setOnAction(e -> handleResetButton());

        Scene scene = new Scene(tabPane, 480, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void handleStartButton() {
        gameInstance.startNewGame(cb1.getValue());
        
        l2.setText("Game Started! Guess a number between 1 and " + gameInstance.range);
        l3.setText("Attempts: 0 / Max: " + gameInstance.maxAttempts);
        t1.clear();
        t1.setDisable(false);
        b2.setDisable(false);
    }

    public void handleGuessButton() {
        try {
            int inputGuess = Integer.parseInt(t1.getText().trim());
            String feedback = gameInstance.processUserGuess(inputGuess);
            l3.setText("Attempts: " + gameInstance.attempts + " / Max: " + gameInstance.maxAttempts);

            if (feedback.equals("CORRECT")) {
                t1.setDisable(true);
                b2.setDisable(true);
                
                boolean isNewRecord = gameInstance.checkAndUpdateBestScore();
                l4.setText("Best Score: " + gameInstance.bestScore + " attempts");
                
                if (isNewRecord) {
                    l2.setText("CORRECT! New Best Score!");
                } else {
                    l2.setText("CORRECT! Finished in " + gameInstance.attempts + " attempts.");
                }
            } else {
                if (gameInstance.attempts >= gameInstance.maxAttempts) {
                    l2.setText("GAME OVER! You reached the limit. Number was: " + gameInstance.secretNumber);
                    t1.setDisable(true);
                    b2.setDisable(true);
                } else {
                    l2.setText(feedback);
                }
            }
        } catch (NumberFormatException ex) {
            l2.setText("Please enter a valid integer!");
        }
        t1.clear();
    }

    public void handleResetButton() {
        gameInstance.resetBestScoreRecord();
        l4.setText("Best Score: No score yet");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
