package fr.enssat.BoulderDash.controllers;

import fr.enssat.BoulderDash.models.LevelModel;
import fr.enssat.BoulderDash.helpers.AudioLoadHelper;
import fr.enssat.BoulderDash.controllers.NavigationBetweenViewController;
import fr.enssat.BoulderDash.views.MenuView;
import fr.enssat.BoulderDash.views.GameView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * GameController
 *
 * This system creates the view.
 * The game loop is also handled there.
 *
 * @author      Colin Leverger <me@colinleverger.fr>
 * @since       2015-06-19
 */
public class GameController extends BaseController {
//	private LevelModel levelModel;
//    private AudioLoadHelper audioLoadHelper;
    private boolean firstClickOnPause;
	private MenuView menuView;
	private GameView gameView;
	private NavigationBetweenViewController navigationBetweenViewController;
	
    /**
     * Class constructor
     *
     * @param  levelModel  Level model
     * @param navigationBetweenViewController 
     */
	public GameController(LevelModel levelModel, AudioLoadHelper audioLoadHelper, NavigationBetweenViewController navigationBetweenViewController) {
		super(levelModel, audioLoadHelper);
		registerCommand("pause", this::handlePause);
		registerCommand("restart", this::handleRestart);
		registerCommand("menu", this::handleMenu);
		this.firstClickOnPause = true;
        
        this.navigationBetweenViewController = navigationBetweenViewController;
        this.gameView = new GameView(this, levelModel); 
        this.menuView = navigationBetweenViewController.getMenuView();

        this.getAudioLoadHelper().stopMusic();
        this.getAudioLoadHelper().playSound("new");
	}


	/**
	 * Function to reset the game
	 */
    private void resetGame(String source) {
		this.gameView.dispose();
		
		if(source.equals("restart")){
	    	this.levelModel = new LevelModel(this.navigationBetweenViewController.getPickedLevelIdentifier(), audioLoadHelper);
			this.gameView = new GameView(this, levelModel);
			this.gameView.setVisible(true);
		}
	}

	/**
     * Gets the audio load helper instance
     *
     * @return  Audio load helper instance
     */
    public AudioLoadHelper getAudioLoadHelper() {
        return this.audioLoadHelper;
    }

    /**
     * Return the game view
     * @return gameView
     */
	public GameView getGameView() {
		return gameView;
	}

	/**
	 * Set the gameView
	 * @param gameView
	 */
	public void setGameView(GameView gameView) {
		this.gameView = gameView;
	}

	private void handlePause() {
		if(this.firstClickOnPause) {
			this.levelModel.setGamePaused(true);
		} else if(!this.firstClickOnPause) {
			this.levelModel.setGamePaused(false);
		}

		this.firstClickOnPause = !this.firstClickOnPause;
		this.gameView.getGameFieldView().grabFocus();
	}

	private void handleRestart() {
		this.resetGame("restart");
		this.getAudioLoadHelper().playSound("new");
		this.gameView.getGameFieldView().grabFocus();
	}

	private void handleMenu() {
		this.menuView.setVisible(true);
		this.getAudioLoadHelper().startMusic("game");
		this.resetGame("menu");
	}
}