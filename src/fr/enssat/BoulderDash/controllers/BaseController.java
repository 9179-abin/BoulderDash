package fr.enssat.BoulderDash.controllers;

import fr.enssat.BoulderDash.models.LevelModel;
import fr.enssat.BoulderDash.helpers.AudioLoadHelper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class BaseController implements ActionListener, KeyListener {
    private Map<String, Runnable> commandMap = new HashMap<>();
    protected LevelModel levelModel;
    protected AudioLoadHelper audioLoadHelper;

    public BaseController(LevelModel levelModel, AudioLoadHelper audioLoadHelper) {
        this.levelModel = levelModel;
        this.audioLoadHelper = audioLoadHelper;
    }

    public BaseController(LevelModel levelModel) {
        this.levelModel = levelModel;
    }

    public BaseController(AudioLoadHelper audioLoadHelper) { this.audioLoadHelper = audioLoadHelper; }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        Runnable action = commandMap.get(command);

        if (action != null) {
            action.run();
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_UP:
                handleUpKey();
                break;

            case KeyEvent.VK_DOWN:
                handleDownKey();
                break;

            case KeyEvent.VK_LEFT:
                handleLeftKey();
                break;

            case KeyEvent.VK_RIGHT:
                handleRightKey();
                break;

            default:
                handleOtherKey(event);
                break;
        }
    }


    protected void handleUpKey(){}
    protected void handleDownKey(){}
    protected void handleLeftKey(){}
    protected void handleRightKey(){}

    // A method for other key events, can be overridden by subclasses if needed
    protected void handleOtherKey(KeyEvent e) {
        // Default implementation does nothing
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Can be overridden by subclasses
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Default implementation does nothing
    }

    protected void registerCommand(String command, Runnable action) {
        commandMap.put(command, action);
    }

}


