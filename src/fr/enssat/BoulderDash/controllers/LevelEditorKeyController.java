package fr.enssat.BoulderDash.controllers;

import fr.enssat.BoulderDash.models.LevelModel;
import fr.enssat.BoulderDash.views.LevelEditorView;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * LevelEditorKeyController
 *
 * Manages the key events controller.
 *
 * @author      Valerian Saliou <valerian@valeriansaliou.name>
 * @since       2015-06-21
 */
public class LevelEditorKeyController extends BaseController {
//    private LevelModel levelModel;
    private LevelEditorView levelEditorView;
	private boolean capLocks;

    /**
     * Class constructor
     *
     * @param  levelModel       Level model
     * @param  levelEditorView  Level editor view
     */
    public LevelEditorKeyController(LevelModel levelModel, LevelEditorView levelEditorView) {
        super(levelModel);
        this.capLocks = false;
        this.levelEditorView = levelEditorView;
    }


    @Override
    protected void handleUpKey() {
        this.levelModel.decrementCursorYPosition();
    }

    @Override
    protected void handleDownKey() {
        this.levelModel.incrementCursorYPosition();
    }

    @Override
    protected void handleLeftKey() {
        this.levelModel.decrementCursorXPosition();
    }

    @Override
    protected void handleRightKey() {
        this.levelModel.incrementCursorXPosition();
    }

    @Override
    protected void handleOtherKey(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            // Key: SPACE
            case KeyEvent.VK_SPACE:
                this.levelModel.triggerBlockChange(this.levelEditorView.getPickedBlockValue());
                break;

            case 16:
                this.capLocks = !capLocks;
                break;

        }
        // Hold block change (quick edit)
        if(capLocks) {
            this.levelModel.triggerBlockChange(this.levelEditorView.getPickedBlockValue());
        }
    }
}
