package se233.chapter4;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se233.chapter4.controller.DrawingLoop;
import se233.chapter4.controller.GameLoop;
import se233.chapter4.view.GameStage;

public class Launcher extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        GameStage gameStage = new GameStage();
        GameLoop gameLoop = new GameLoop(gameStage);
        DrawingLoop drawingLoop = new DrawingLoop(gameStage);
        Scene scene = new Scene(gameStage, GameStage.WIDTH, GameStage.HEIGHT);
        scene.setOnKeyPressed(event -> gameStage.getKeys().add(event.getCode()));
        scene.setOnKeyReleased(event -> gameStage.getKeys().remove(event.getCode()));
        stage.setTitle("Mario");
        stage.setScene(scene);
        stage.show();
        gameStage.requestFocus();
        Thread gameLoopThread = new Thread(gameLoop, "game-loop");
        Thread drawingLoopThread = new Thread(drawingLoop, "drawing-loop");
        stage.setOnCloseRequest(event -> {
            gameLoop.stop();
            drawingLoop.stop();
            gameLoopThread.interrupt();
            drawingLoopThread.interrupt();
        });
        gameLoopThread.setDaemon(true);
        drawingLoopThread.setDaemon(true);
        gameLoopThread.start();
        drawingLoopThread.start();
    }
}
