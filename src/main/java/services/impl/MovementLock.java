package services.impl;

/**
 * Created by shn on 20/06/2016.
 */
public class MovementLock {
    private static MovementLock instance = null;

    private MovementLock() {}

    public static MovementLock getInstance() {
        if (instance == null)
            synchronized (MovementLock.class) {
                if (instance == null)
                    instance = new MovementLock();
            }
        return instance;
    }

    public void pleaseNotifyAll() {
        notifyAll();
    }

}
