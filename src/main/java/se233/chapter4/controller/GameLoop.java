package se233.chapter4.controller;

import se233.chapter4.model.GameCharacter;
import se233.chapter4.view.GameStage;

public class GameLoop implements Runnable {
    private static final int FRAME_RATE = 10;
    private final GameStage gameStage;
    private final long intervalNanos = 1_000_000_000L / FRAME_RATE;
    private volatile boolean running = true;

    public GameLoop(GameStage gameStage) {
        this.gameStage = gameStage;
    }

    private void update(GameCharacter gameCharacter) {
        boolean leftPressed = gameStage.getKeys().isPressed(gameCharacter.getLeftKey());
        boolean rightPressed = gameStage.getKeys().isPressed(gameCharacter.getRightKey());
        boolean upPressed = gameStage.getKeys().isPressed(gameCharacter.getUpKey());

        if (leftPressed && rightPressed) {
            gameCharacter.stop();
        } else if (leftPressed) {
            gameCharacter.moveLeft();
            gameCharacter.trace();
        } else if (rightPressed) {
            gameCharacter.moveRight();
            gameCharacter.trace();
        } else {
            gameCharacter.stop();
        }

        if (upPressed) {
            gameCharacter.jump();
        }
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            long startTime = System.nanoTime();
            for (GameCharacter gameCharacter : gameStage.getGameCharacters()) {
                update(gameCharacter);
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
