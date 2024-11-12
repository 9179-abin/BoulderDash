package fr.enssat.BoulderDash.controllers;

import fr.enssat.BoulderDash.models.LevelModel;
import fr.enssat.BoulderDash.models.DirtModel;
import fr.enssat.BoulderDash.models.DisplayableElementModel;
import fr.enssat.BoulderDash.helpers.AudioLoadHelper;

/**
 * ElementPositionUpdateHelper
 *
 * Updates position of all elements displayed on the map, according to their
 * next potential position. Each object has a weight, which is used to compare
 * their power to destroy in the food chain. Sorry for that Darwinism.
 *
 * @author Colin Leverger <me@colinleverger.fr>
 * @since 2015-06-19
 */
public class BoulderAndDiamondController extends RunnableTaskController {
//	private LevelModel levelModel;
//    private AudioLoadHelper audioLoadHelper;
	private Thread elementMovingThread;

	/**
	 * Class constructor
	 *
	 * @param levelModel  Level model
	 */
	public BoulderAndDiamondController(LevelModel levelModel, AudioLoadHelper audioLoadHelper) {
		super(levelModel, audioLoadHelper);
	}


	@Override
	protected void executeTask() {
		this.manageFallingObject();
	}

	/**
	 * Scan the ground to detect the boulders & the diamonds, then make them
	 * fall if necessary
     * Note: scan of the ground upside down: we want things to fall slowly !
	 */
	private void manageFallingObject() {
		for (int x = this.levelModel.getSizeWidth() - 1; x >= 0; x--) {
			for (int y = this.levelModel.getSizeHeight() - 1; y >= 0; y--) {
				// Gets the spriteName of actual DisplayableElementModel object scanned
				DisplayableElementModel elementModel = this.levelModel.getGroundLevelModel()[x][y];

				if(elementModel == null) {
					elementModel = new DirtModel();
				}

				String spriteName = elementModel.getSpriteName();
				
				// If it is a boulder or a diamond...
				if (spriteName == "boulder" || spriteName == "diamond") {
					this.handleFallingBoulderOrDiamond(x, y);
				} else if(spriteName == "expandingwall"){
					if(this.expandWall(x,y).equals("left")){
						x -= 1;
					}
				}
			}
		}
	}

	/**
	 * Expand the wall at left & right
     *
	 * @param  x  Horizontal position
	 * @param  y  Vertical position
	 */
	private String expandWall(int x, int y) {
		DisplayableElementModel elementLeft  = this.levelModel.getGroundLevelModel()[x - 1][y];
        DisplayableElementModel elementRight = this.levelModel.getGroundLevelModel()[x + 1][y];
        String spriteNameLeft  = elementLeft.getSpriteName();
		String spriteNameRight = elementRight.getSpriteName();
		
		String way = "";
		if(spriteNameLeft == "black"){
			this.levelModel.expandThisWallToLeft(x,y);
			way = "left";
		}
		if(spriteNameRight == "black"){
			this.levelModel.expandThisWallToRight(x,y);
			way = "right";
		}
		return way;
	}

	/**
	 * Manages the fall of elements
	 *
	 * @param  x  Horizontal position
	 * @param  y  Vertical position
	 */
	private void handleFallingBoulderOrDiamond(int x, int y) {
		// Get informed about Rockford surroundings
		DisplayableElementModel[][] groundLevelModel = levelModel.getGroundLevelModel();
		DisplayableElementModel elementBelow = levelModel.getElementBelow(x,y);
        DisplayableElementModel elementLeft  = levelModel.getElementLeft(x,y);
        DisplayableElementModel elementRight = levelModel.getElementRight(x,y);

        String spriteNameBelow = elementBelow.getSpriteName();
		String spriteNameLeft  = elementLeft.getSpriteName();
		String spriteNameRight = elementRight.getSpriteName();

		// Then, process in case of the surrounding
		if (spriteNameBelow == "black") {
			this.levelModel.makeThisDisplayableElementFall(x, y);
		} else if (spriteNameBelow == "boulder") {
			// Boulders have to roll if they hit another boulder
			if (groundLevelModel[x - 1][y + 1].getSpriteName() == "black") {
				this.levelModel.makeThisBoulderSlideLeft(x, y);
			} else if (groundLevelModel[x + 1][y + 1].getSpriteName() == "black") {
				this.levelModel.makeThisBoulderSlideRight(x, y);
			}
		} else if (spriteNameBelow == "rockford" && groundLevelModel[x][y].isFalling()) {
			this.levelModel.exploseGround(x, y + 1);

            this.audioLoadHelper.playSound("die");

			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			this.levelModel.setGameRunning(false);
		} else if (spriteNameBelow == "magicwall") {
			if (groundLevelModel[x][y].getSpriteName() == "boulder"
					&& (groundLevelModel[x][y+2].getSpriteName() == "dirt" ||
							groundLevelModel[x][y+2].getSpriteName() == "black")) {
				if(groundLevelModel[x][y].isConvertible()) {
					this.levelModel.transformThisBoulderIntoADiamond(x, y);
				} else {
					this.levelModel.deleteThisBoulder(x, y);
				}
			}
		} else if (elementBelow.isDestructible() && spriteNameBelow != "dirt" && groundLevelModel[x][y].isFalling()) {
				this.levelModel.exploseThisBrickWall(x, y);
		} else if (spriteNameLeft == "rockford" && this.levelModel.getRockford().isRunningRight() && groundLevelModel[x + 1][y].getSpriteName() == "black") {
			this.levelModel.moveThisBoulderToRight(x, y);
		} else if (spriteNameRight == "rockford" && this.levelModel.getRockford().isRunningLeft() && groundLevelModel[x - 1][y].getSpriteName() == "black") {
			this.levelModel.moveThisBoulderToLeft(x, y);
		} else {
			groundLevelModel[x][y].setFalling(false);
		}
	}
}
