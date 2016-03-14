package com.dylowen.tinglebot.train;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.dylowen.tinglebot.Timer;
import com.dylowen.tinglebot.brain.Brain;
import com.dylowen.tinglebot.brain.BrainJava;

/**
 * TODO add description
 *
 * @author dylan.owen
 * @since Feb-2016
 */
public class SerializedBrainTrainer<T extends Brain>
    extends Trainer<T> {

    private final String filePath;

    public SerializedBrainTrainer(final String filePath) {
        this.filePath = filePath;
    }

    @Override
    public T train() {
        final Timer timer = new Timer();
        final T brain;

        try (FileInputStream fileIn = new FileInputStream(this.filePath); ObjectInputStream in = new ObjectInputStream(fileIn)) {
            brain = (T) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        System.out.println("Brain stateCount: " + brain.stateCount());
        System.out.println("Brain feed time: " + timer.getS() + "s");

        return brain;
    }
}
