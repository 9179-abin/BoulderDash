package fr.enssat.BoulderDash.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.enssat.BoulderDash.helpers.AudioLoadHelper;
import fr.enssat.BoulderDash.models.LevelModel;
import fr.enssat.BoulderDash.views.MenuView;
import fr.enssat.BoulderDash.controllers.LevelEditorController;
import fr.enssat.BoulderDash.controllers.GameController;

/**
 * Controller to navigate between the different views
 * 
 * @author Colin Leverger <me@colinleverger.fr>
 *
 */
public class NavigationBetweenViewController extends BaseController {
	private LevelEditorController levelEditorController;
	private MenuView menuView;
	private AudioLoadHelper audioLoadHelper;
    private GameController gameController;
	private String pickedLevelIdentifier;

    /**
     * Class constructor
     */
	public NavigationBetweenViewController() {
		super(new AudioLoadHelper());
		registerCommand("quit", this::handleQuit);
		registerCommand("editor", this::handleEditor);
		registerCommand("game", this::handleGame);
		this.audioLoadHelper = new AudioLoadHelper();
        // Play game music
        this.getAudioLoadHelper().startMusic("game");

		// Creation of the first view
		this.menuView = new MenuView(this);
	}

    /**
     * Action performed event handler
     *
     * @param  event  Action event
     */
	@Override
	public void actionPerformed(ActionEvent event) {
		super.actionPerformed(event);
		this.menuView.setVisible(false);
	}

    /**
     * Get the audio load helper
     *
     * @return  Audio load helper
     */
    public AudioLoadHelper getAudioLoadHelper() {
        return this.audioLoadHelper;
    }

    /**
     * Get the first view
     *
     * @return  First view
     */
    public MenuView getMenuView() {
        return this.menuView;
    }

	/**
	 * Set the first view
	 * 
	 * @param menuView
	 */
	public MenuView setMenuView() {
		this.menuView = new MenuView(this);
		return menuView;
	}

	/**
	 * Get the pickedLevel
     *
	 * @return  pickedLevelIdentifier  Picked level identifier
	 */
	public String getPickedLevelIdentifier() {
		return pickedLevelIdentifier;
	}

	/**
	 * Set the pickedLevelIdentifier
     *
	 * @param  pickedLevelIdentifier  Picked level identifier
	 */
	public void setPickedLevelIdentifier(String pickedLevelIdentifier) {
		this.pickedLevelIdentifier = pickedLevelIdentifier;
	}

	private void handleQuit(){
		System.exit(0);
	}
	
	private void handleEditor(){
		LevelModel levelModelForEditor = new LevelModel(audioLoadHelper);
		this.levelEditorController = new LevelEditorController(levelModelForEditor, this);

		this.levelEditorController.getLevelEditorView().setVisible(true);
		this.levelEditorController.getLevelEditorView().getLevelEditorGroundView().grabFocus();

		if (gameController != null) {
			this.gameController.getGameView().setVisible(false);
		}
	}

	private void handleGame(){
		// Reinit the levelModelForGame...
		pickedLevelIdentifier = this.menuView.getLevelIdentifier();

		LevelModel levelModelForGame = new LevelModel(pickedLevelIdentifier, audioLoadHelper);
		this.gameController = new GameController(levelModelForGame, audioLoadHelper, this);

		if (levelEditorController != null) {
			this.levelEditorController.getLevelEditorView().setVisible(false);
		}

		this.gameController.getGameView().setVisible(true);
		this.gameController.getGameView().getGameFieldView().grabFocus();
	}
}
