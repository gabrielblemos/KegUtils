package com.keg.kegutils.Utils;

/**
 * Created by gamer on 16/07/2017.
 */

public class UtilsTask {

    public static void runInBackground(Runnable runnable) {
        new Thread(runnable) {
            @Override
            public void run() {
                super.run();
                interrupt();
            }
        }.start();
    }

    public static void waitBeforeRunInBackground(Runnable runnable, final long waitTime) {
        new Thread(runnable) {
            @Override
            public void run() {
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
                interrupt();
            }
        }.start();
    }

}
