package fr.enssat.BoulderDash.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.enssat.BoulderDash.exceptions.LevelConstraintNotRespectedException;
import fr.enssat.BoulderDash.helpers.AudioLoadHelper;
import fr.enssat.BoulderDash.helpers.LevelRemoveHelper;
import fr.enssat.BoulderDash.helpers.LevelSaveHelper;
import fr.enssat.BoulderDash.models.LevelModel;
import fr.enssat.BoulderDash.views.HelpView;
import fr.enssat.BoulderDash.views.LevelEditorView;
import fr.enssat.BoulderDash.controllers.NavigationBetweenViewController;

import javax.swing.*;

/**
 * LevelEditorController
 *
 * Manages the level editor controller.
 *
 * @author      Valerian Saliou <valerian@valeriansaliou.name>
 * @since       2015-06-19
 */
public class LevelEditorController extends BaseController implements ActionListener {
//    private LevelModel levelModel;
	private LevelEditorView levelEditorView;
	private NavigationBetweenViewController nav;

    /**
     * Class constructor'
     *
     * @param  levelModel  Level model
     */
    public LevelEditorController(LevelModel levelModel, NavigationBetweenViewController nav) {
        super(levelModel);
        registerCommand("save",this::handleSave);
        registerCommand("delete",this::handleDelete);
        registerCommand("help",this::handleHelp);
        registerCommand("new",this::handleNewLevel);
        registerCommand("menu", this::handleMenu);
        this.levelModel.setShowCursor(true);

        this.nav = nav;
        this.nav.getAudioLoadHelper().stopMusic();
        
        this.levelEditorView = new LevelEditorView(this, levelModel, nav);

        // Pre-bind event watcher (hack to fix a Java issue)
        this.levelModel.decrementCursorXPosition();
    }

    /**
     * Handles the 'action performed' event
     *
     * @param  event  Action event
     */

    @Override
    public void actionPerformed(ActionEvent event) {
        super.actionPerformed(event);
        this.getLevelEditorView().getLevelEditorGroundView().grabFocus();
    }

    /**
     * Gets the level editor view
     *
     * @return  Level editor view
     */
	public LevelEditorView getLevelEditorView() {
		return levelEditorView;
	}

    /**
     * Gets level model
     *
     * @return  Level model
     */
    public LevelModel getLevelModel() {
        return this.levelModel;
    }

    /**
     * Sets the level editor view
     *
     * @param  levelEditorView  Level editor view
     */
	public void setLevelEditorView(LevelEditorView levelEditorView) {
		this.levelEditorView = levelEditorView;
	}

    private void handleSave(){
        // Check constraints
        try {
            this.levelModel.checkConstraints();

            // Save action (direct save)
            String levelId = this.levelEditorView.getSelectedLevel();
            LevelSaveHelper levelSave;

            if(levelId == null || levelId.isEmpty()) {
                // Create a new level
                levelSave = new LevelSaveHelper(levelModel.getGroundLevelModel());
            } else {
                // Overwrite existing level
                levelSave = new LevelSaveHelper(levelId, levelModel.getGroundLevelModel());
            }

            JFrame frameDialog = new JFrame("Info");
            JOptionPane.showMessageDialog(frameDialog, "Level saved");

            this.levelEditorView.openedLevelChange(levelSave.getLevelId());
        } catch(LevelConstraintNotRespectedException e) {
            JFrame frameDialog = new JFrame("Error");
            JOptionPane.showMessageDialog(frameDialog, e.getMessage());
        }
    }

    private void handleDelete(){
        String levelId = this.levelEditorView.getSelectedLevel();
        JFrame frameDialog = new JFrame("Info");

        if(levelId == null || levelId.isEmpty()) {
            JOptionPane.showMessageDialog(frameDialog, "Level not yet saved, no need to delete it!");
        } else {
            new LevelRemoveHelper(levelId);
            JOptionPane.showMessageDialog(frameDialog, "Level deleted!");

            this.levelEditorView.openedLevelChange(null);
        }
    }

    private void handleHelp(){
        new HelpView();
    }

    private void handleNewLevel(){
        this.levelEditorView.openedLevelChange(null);
    }

    private void handleMenu(){
        this.levelEditorView.setVisible(false);
        this.nav.setMenuView();
        this.nav.getAudioLoadHelper().startMusic("game");
    }
    
    
}