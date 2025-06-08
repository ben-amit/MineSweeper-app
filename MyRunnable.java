package com.example.minesweeper;

import android.os.Handler;

public class MyRunnable implements Runnable {

    GameOverActivity gameOverActivity;
    int frameNum;

    public MyRunnable(GameOverActivity gameOverActivity) {
        this.gameOverActivity = gameOverActivity;
    }

    @Override
    public void run() {
        gameOverActivity.changeFrame(frameNum);
    }

    public void setFrame(int frameNum) {
        this.frameNum = frameNum;
    }
}
