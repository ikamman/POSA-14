package edu.vuum.mocca;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

/**
 * @class SimpleSemaphore
 * 
 * @brief This class provides a simple counting semaphore implementation using
 *        Java a ReentrantLock and a ConditionObject. It must implement both
 *        "Fair" and "NonFair" semaphore semantics, just liked Java Semaphores.
 */
public class SimpleSemaphore {
    /**
     * Define a ReentrantLock to protect the critical section.
     */
    // TODO - you fill in here
    private ReentrantLock lock = null;

    /**
     * Define a ConditionObject to wait while the number of
     * permits is 0.
     */
    // TODO - you fill in here
    private Condition isEmpty = null;
    private Condition isFull = null;

    /**
     * Define a count of the number of available permits.
     */
    // TODO - you fill in here.  Make sure that this data member will
    // ensure its values aren't cached by multiple Threads..
    private int maxPermits = 0;
    private volatile int permits = 0;

    public SimpleSemaphore(int permits, boolean fair) {
        // TODO - you fill in here to initialize the SimpleSemaphore,
        // making sure to allow both fair and non-fair Semaphore
        // semantics.
        lock = new ReentrantLock(fair);

        isEmpty = lock.newCondition();
        isFull = lock.newCondition();

        this.maxPermits = permits;
        this.permits = permits;
    }

    /**
     * Acquire one permit from the semaphore in a manner that can be
     * interrupted.
     */
    public void acquire() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (isEmpty())
                isEmpty.await();
            acquirePermit();
        } finally {
            lock.unlock();
        }
    }

    private void acquirePermit() {
        permits--;
        isFull.signal();
    }

    private boolean isEmpty() {
        return permits == 0;
    }

    /**
     * Acquire one permit from the semaphore in a manner that cannot be
     * interrupted.
     */
    public void acquireUninterruptibly() {
        lock.lock();
        try {
            while (isEmpty())
                isEmpty.awaitUninterruptibly();
            acquirePermit();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Return one permit to the semaphore.
     */
    void release() {
        // TODO - you fill in here.
        lock.lock();
        try {
            while (isFull())
                isEmpty.awaitUninterruptibly();
            releasePermit();
        } finally {
            lock.unlock();
        }
    }

    private void releasePermit() {
        permits++;
        isEmpty.signal();
    }

    private boolean isFull() {
        return permits == maxPermits;
    }

    /**
     * Return the number of permits available.
     */
    public int availablePermits() {
        // TODO - you fill in here by changing null to the appropriate
        // return value.
        return permits;
    }
}
