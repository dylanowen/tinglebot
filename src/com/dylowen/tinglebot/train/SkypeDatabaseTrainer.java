package com.dylowen.tinglebot.train;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.almworks.sqlite4java.SQLite;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import com.dylowen.tinglebot.Json;
import com.dylowen.tinglebot.brain.Brain;

/**
 * TODO add description
 *
 * @author dylan.owen
 * @since Feb-2016
 */
public class SkypeDatabaseTrainer
    implements Trainer {

    private final Settings settings;
    private final int gramSize;

    public SkypeDatabaseTrainer(final String dbSettingsPath, final int gramSize) {
        try {
            this.settings = Json.get().getObjectMapper().readValue(new File(dbSettingsPath), Settings.class);
            this.gramSize = gramSize;

            //make sure we can get the native libraries
            SQLite.getSQLiteVersion();
        }
        catch (SQLiteException | IOException e) {
            System.err.println("error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Brain train() {
        final Brain brain = new Brain();

        final SQLiteConnection db = new SQLiteConnection(new File(this.settings.dbPath));

        try {
            db.open();
            //I don't care about sql injection because you'll only be breaking your own database
            SQLiteStatement st = db.prepare("select body_xml from Messages where " + this.settings.sqlWhereClause
                    + " order by timestamp__ms asc limit 10");
            try {
                while (st.step()) {
                    final InputStream xml = st.columnStream(0);

                    System.out.println(xml);
                }
            }
            finally {
                st.dispose();
            }
        }
        catch (SQLiteException e) {
            System.err.println("sql error: " + e.getMessage());
            throw new RuntimeException(e);
        }
        finally {
            db.dispose();
        }

        return brain;
    }

    public static class Settings {
        public String dbPath;
        public String sqlWhereClause;
    }
}
