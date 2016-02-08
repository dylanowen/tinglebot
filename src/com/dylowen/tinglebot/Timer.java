package com.dylowen.tinglebot;

/**
 * TODO add description
 *
 * @author dylan.owen
 * @since Feb-2016
 */
public class Timer {

    private final long start;
    private long finish = 0;

    public Timer() {
        this.start = System.nanoTime();
    }

    public void stop() {
        this.finish = (0 == this.finish) ? System.nanoTime() : this.finish;
    }

    public double getMs() {
        stop();

        final long tmp = (this.finish - this.start) / 10000L;

        return tmp / 100.0;
    }

    public double getS() {
        stop();

        final long tmp = (this.finish - this.start) / 1000000L;

        return tmp / 1000.0;
    }

}
