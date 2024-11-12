package fr.enssat.BoulderDash.controllers;

import fr.enssat.BoulderDash.models.LevelModel;

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
public class RockfordUpdateController extends RunnableTaskController {
//	private LevelModel levelModel;
	private Thread elementMovingThread;
	private int rockfordPositionX;
	private int rockfordPositionY;
	private boolean rockfordHasMoved;

	/**
	 * Class constructor
	 *
	 * @param levelModel  Level model
	 */
	public RockfordUpdateController(LevelModel levelModel) {
		super(levelModel);
		this.rockfordHasMoved = false;
	}


	@Override
	protected int getSleepDuration() {
		return 100;
	}

	@Override
	protected void executeTask() {
		if (this.rockfordHasMoved) {
			this.levelModel.setPositionOfRockford(rockfordPositionX, rockfordPositionY);
			this.rockfordHasMoved = false;
		}
	}

	/**
	 * Moves Rockford
	 *
	 * @param  rockfordPositionX  Next horizontal position on the grid
	 * @param  rockfordPositionY  Next vertical position on the grid
	 */
	public void moveRockford(int rockfordPositionX, int rockfordPositionY) {
		this.rockfordPositionX = rockfordPositionX;
		this.rockfordPositionY = rockfordPositionY;
		this.rockfordHasMoved = true;
	}
}
