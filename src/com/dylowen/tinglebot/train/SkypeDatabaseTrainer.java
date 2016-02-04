package com.dylowen.tinglebot.train;

import com.almworks.sqlite4java.SQLiteConnection;
import com.dylowen.tinglebot.brain.Brain;

import java.io.File;

/**
 * TODO add description
 *
 * @author dylan.owen
 * @since Feb-2016
 */
public class SkypeDatabaseTrainer implements Trainer {

    final String dbPath;
    final int gramSize;

    public SkypeDatabaseTrainer(final String dbPath, final int gramSize) {
        this.dbPath = dbPath;
        this.gramSize = gramSize;
    }

    @Override
    public Brain train() {
        final Brain brain = new Brain();

        final SQLiteConnection db = new SQLiteConnection(new File(dbPath));




        db.dispose();

        return brain;
    }

}
