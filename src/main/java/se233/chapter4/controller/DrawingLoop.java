package se233.chapter4.controller;

import javafx.application.Platform;
import se233.chapter4.model.GameCharacter;
import se233.chapter4.view.GameStage;

public class DrawingLoop implements Runnable {
    private static final int FRAME_RATE = 60;
    private final GameStage gameStage;
    private final long intervalNanos = 1_000_000_000L / FRAME_RATE;
    private volatile boolean running = true;

    public DrawingLoop(GameStage gameStage) {
        this.gameStage = gameStage;
    }

    private void checkDrawCollisions(GameCharacter gameCharacter) {
        gameCharacter.checkReachGameWall();
        gameCharacter.checkReachHighest();
        gameCharacter.checkReachFloor();
    }

    private void paint(GameCharacter gameCharacter) {
        Platform.runLater(() -> {
            if (gameCharacter.isMoving()) {
                gameCharacter.getImageView().tick();
            }
            gameCharacter.render();
        });
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            long startTime = System.nanoTime();
            for (GameCharacter gameCharacter : gameStage.getGameCharacters()) {
                gameCharacter.updatePhysics();
                checkDrawCollisions(gameCharacter);
                paint(gameCharacter);
            }
            parkUntilNextFrame(startTime);
        }
    }

    private void parkUntilNextFrame(long startTime) {
        long remaining = intervalNanos - (System.nanoTime() - startTime);
        if (remaining > 0) {
            try {
                Thread.sleep(Math.max(1L, remaining / 1_000_000L));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running = false;
            }
        }
    }
}
