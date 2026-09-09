package se233.chapter4.model;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AnimatedSprite extends ImageView {
    private final int startX;
    private final int startY;
    private final int frameWidth;
    private final int frameHeight;
    private final int frameCount;
    private final int displayWidth;
    private final int displayHeight;
    private int currentFrame;

    public AnimatedSprite(Image image, int startX, int startY, int frameWidth, int frameHeight) {
        this(image, startX, startY, frameWidth, frameHeight,
                Math.max(1, (int) image.getWidth() / frameWidth));
    }

    public AnimatedSprite(Image image, int startX, int startY, int frameWidth,
                          int frameHeight, int frameCount) {
        this(image, startX, startY, frameWidth, frameHeight, frameCount, frameWidth, frameHeight);
    }

    public AnimatedSprite(Image image, int startX, int startY, int frameWidth,
                          int frameHeight, int frameCount, int displayWidth, int displayHeight) {
        super(image);
        if (frameWidth <= 0 || frameHeight <= 0 || frameCount <= 0
                || displayWidth <= 0 || displayHeight <= 0) {
            throw new IllegalArgumentException("Sprite frame dimensions and count must be positive");
        }
        if (startX < 0 || startY < 0
                || startX + frameWidth * frameCount > image.getWidth()
                || startY + frameHeight > image.getHeight()) {
            throw new IllegalArgumentException("Sprite frames exceed the source image bounds");
        }
        this.startX = startX;
        this.startY = startY;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.frameCount = frameCount;
        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;
        this.currentFrame = 0;
        setSmooth(false);
        updateViewport();
    }

    public void tick() {
        currentFrame = (currentFrame + 1) % frameCount;
        updateViewport();
    }

    private void updateViewport() {
        setViewport(new Rectangle2D(
                startX + currentFrame * frameWidth,
                startY,
                frameWidth,
                frameHeight));
        setFitWidth(displayWidth);
        setFitHeight(displayHeight);
    }
}
