package se233.chapter4.model;

import javafx.scene.input.KeyCode;

import java.util.EnumSet;
import java.util.Set;

public class Keys {
    private final Set<KeyCode> keys = EnumSet.noneOf(KeyCode.class);

    public synchronized void add(KeyCode key) {
        keys.add(key);
    }

    public synchronized void remove(KeyCode key) {
        keys.remove(key);
    }

    public synchronized boolean isPressed(KeyCode key) {
        return keys.contains(key);
    }
}
