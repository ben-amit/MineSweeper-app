package com.example.minesweeper;

import android.os.Handler;
import android.os.Looper;

public class MyThread extends Thread {
    private GameOverActivity gameOverActivity;
    private boolean running = true;
    private Handler handler;
    MyRunnable myRunnable;

    public MyThread(GameOverActivity gameOverActivity) {
        this.gameOverActivity = gameOverActivity;
        handler = new Handler(Looper.getMainLooper());
        myRunnable = new MyRunnable(gameOverActivity);
    }

    @Override
    public void run() {
        int i = 0;
        while (running) {
            //Change frame
            myRunnable.setFrame(i);
            handler.post(myRunnable);
            //Change i
            i++;
            if (i > 5) {
                i = 0;
            }
            try {
                sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }

    public void stopRunning() {
        running = false;
    }
}