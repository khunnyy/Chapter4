package se233.chapter4.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import se233.chapter4.Launcher;
import se233.chapter4.model.GameCharacter;
import se233.chapter4.model.Keys;

import java.util.List;

public class GameStage extends Pane {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 400;
    public static final int GROUND = 300;

    private final Image gameStageImg;
    private final List<GameCharacter> gameCharacters;
    private final Keys keys;

    public GameStage() {
        keys = new Keys();
        java.io.InputStream backgroundStream = Launcher.class.getResourceAsStream(
                "/se233/chapter4/assets/Background.png");
        gameStageImg = backgroundStream == null ? new WritableImage(WIDTH, HEIGHT)
                : new Image(backgroundStream);
        ImageView backgroundImg = new ImageView(gameStageImg);
        backgroundImg.setFitHeight(HEIGHT);
        backgroundImg.setFitWidth(WIDTH);

        GameCharacter mario = new GameCharacter(30, 30, KeyCode.A, KeyCode.D, KeyCode.W);
        GameCharacter rockman = new GameCharacter(
                "Rockman", 300, 30, "/se233/chapter4/assets/Rockman.png",
                0, 0, 541, 514, 5,
                KeyCode.LEFT, KeyCode.RIGHT, KeyCode.UP, 9, 20,
                GameCharacter.CHARACTER_WIDTH, GameCharacter.CHARACTER_HEIGHT);
        gameCharacters = List.of(mario, rockman);
        setFocusTraversable(true);
        getChildren().add(backgroundImg);
        getChildren().addAll(gameCharacters);
    }

    public GameCharacter getGameCharacter() {
        return gameCharacters.get(0);
    }

    public List<GameCharacter> getGameCharacters() {
        return gameCharacters;
    }

    public Keys getKeys() {
        return keys;
    }
}
