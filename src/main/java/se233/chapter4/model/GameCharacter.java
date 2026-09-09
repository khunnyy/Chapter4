package se233.chapter4.model;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se233.chapter4.Launcher;
import se233.chapter4.view.GameStage;

public class GameCharacter extends Pane {
    private static final Logger LOGGER = LogManager.getLogger(GameCharacter.class);

    public static final int CHARACTER_WIDTH = 64;
    public static final int CHARACTER_HEIGHT = 64;

    private final String name;
    private final AnimatedSprite imageView;
    private final KeyCode leftKey;
    private final KeyCode rightKey;
    private final KeyCode upKey;
    private final int characterWidth;
    private final int characterHeight;

    private int x;
    private int y;
    private int xVelocity;
    private int yVelocity;
    private boolean movingLeft;
    private boolean movingRight;
    private boolean falling = true;
    private boolean jumping;
    private boolean canJump;
    private double facingDirection = 1;
    private final int horizontalAcceleration;
    private final int verticalAcceleration;
    private final int maxHorizontalVelocity;
    private final int maxVerticalVelocity;

    public GameCharacter(String name, int x, int y, String spriteResource,
                         int spriteX, int spriteY, int frameWidth, int frameHeight,
                         int frameCount, KeyCode leftKey, KeyCode rightKey, KeyCode upKey,
                         int maxHorizontalVelocity, int maxVerticalVelocity,
                         int characterWidth, int characterHeight) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.upKey = upKey;
        this.characterWidth = characterWidth;
        this.characterHeight = characterHeight;
        this.horizontalAcceleration = 1;
        this.verticalAcceleration = 1;
        this.maxHorizontalVelocity = maxHorizontalVelocity;
        this.maxVerticalVelocity = maxVerticalVelocity;

        Image spriteSheet = loadImage(spriteResource,
                Math.max(CHARACTER_WIDTH, spriteX + frameWidth * frameCount),
                Math.max(CHARACTER_HEIGHT, spriteY + frameHeight));
        imageView = new AnimatedSprite(
                spriteSheet,
                spriteX,
                spriteY,
                frameWidth,
                frameHeight,
                frameCount,
                characterWidth,
                characterHeight);
        getChildren().add(imageView);
        render();
    }

    public GameCharacter(int x, int y, KeyCode leftKey, KeyCode rightKey, KeyCode upKey) {
        this("Mario", x, y, "/se233/chapter4/assets/StillMario.png",
                0, 0, 64, 64, 1, leftKey, rightKey, upKey, 7, 17,
                32, 32);
    }

    private static Image loadImage(String resource, int fallbackWidth, int fallbackHeight) {
        java.io.InputStream stream = Launcher.class.getResourceAsStream(resource);
        return stream == null ? new WritableImage(fallbackWidth, fallbackHeight) : new Image(stream);
    }

    public KeyCode getLeftKey() {
        return leftKey;
    }

    public KeyCode getRightKey() {
        return rightKey;
    }

    public KeyCode getUpKey() {
        return upKey;
    }

    public AnimatedSprite getImageView() {
        return imageView;
    }

    public synchronized void moveLeft() {
        facingDirection = -1;
        movingLeft = true;
        movingRight = false;
    }

    public synchronized void moveRight() {
        facingDirection = 1;
        movingLeft = false;
        movingRight = true;
    }

    public synchronized void stop() {
        movingLeft = false;
        movingRight = false;
        xVelocity = 0;
    }

    public synchronized void jump() {
        if (canJump) {
            yVelocity = maxVerticalVelocity;
            canJump = false;
            jumping = true;
            falling = false;
        }
    }

    public synchronized void updatePhysics() {
        moveX();
        moveY();
    }

    private void moveX() {
        if (movingLeft || movingRight) {
            xVelocity = Math.min(maxHorizontalVelocity, xVelocity + horizontalAcceleration);
        }
        if (movingLeft) {
            x -= xVelocity;
        } else if (movingRight) {
            x += xVelocity;
        }
    }

    private void moveY() {
        if (falling) {
            yVelocity = Math.min(maxVerticalVelocity, yVelocity + verticalAcceleration);
            y += yVelocity;
        } else if (jumping) {
            yVelocity = Math.max(0, yVelocity - verticalAcceleration);
            y -= yVelocity;
        }
    }

    public synchronized void checkReachGameWall() {
        int correctedX = Math.max(0, Math.min(x, GameStage.WIDTH - characterWidth));
        if (correctedX != x) {
            LOGGER.debug("{} collided with a game boundary at x={}", name, x);
            x = correctedX;
        }
    }

    public synchronized void checkReachHighest() {
        if (jumping && yVelocity == 0) {
            jumping = false;
            falling = true;
        }
    }

    public synchronized void checkReachFloor() {
        int floorY = GameStage.GROUND - characterHeight;
        if (falling && y >= floorY) {
            y = floorY;
            yVelocity = 0;
            falling = false;
            canJump = true;
        }
    }

    public synchronized boolean isMoving() {
        return movingLeft || movingRight;
    }

    public synchronized void render() {
        setTranslateX(x);
        setTranslateY(y);
        setScaleX(facingDirection);
    }

    public synchronized void trace() {
        LOGGER.debug("{}: x={} y={} vx={} vy={}", name, x, y, xVelocity, yVelocity);
    }
}
