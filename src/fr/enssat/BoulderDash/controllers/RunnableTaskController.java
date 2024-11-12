package fr.enssat.BoulderDash.controllers;

import fr.enssat.BoulderDash.helpers.AudioLoadHelper;
import fr.enssat.BoulderDash.models.LevelModel;

public class RunnableTaskController extends BaseController implements Runnable {
    private Thread taskThread;

    public RunnableTaskController(LevelModel levelModel, AudioLoadHelper audioLoadHelper) {
        super(levelModel, audioLoadHelper);
        startThread();
    }

    public RunnableTaskController(LevelModel levelModel) {
        super(levelModel);
        startThread();
    }

    private void startThread() {
        taskThread = new Thread(this);
        taskThread.start();
    }

    @Override
    public void run() {
        while (levelModel.isGameRunning()) {
            if (!levelModel.getGamePaused()) {
                executeTask();  // Calls subclass-specific task
            }
            try {
                Thread.sleep(getSleepDuration());  // Subclass can override the sleep duration
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Defines the specific task to execute, which each subclass will implement.
     */
    protected void executeTask(){}

    /**
     * Allows subclasses to override the sleep duration between cycles.
     */
    protected int getSleepDuration() {
        return 250;  // Default sleep duration
    }
}
