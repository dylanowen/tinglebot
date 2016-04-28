package com.dylowen.tinglebot.train;

import java.io.BufferedReader;
import java.io.FileReader;

import com.dylowen.tinglebot.Timer;
import com.dylowen.tinglebot.brain.LogicalTextBrain;
import com.dylowen.tinglebot.brain.TextBrain;

/**
 * TODO add description
 *
 * @author dylan.owen
 * @since Feb-2016
 */
public class TextTrainer
    extends JavaTrainer<TextBrain> {

    private final String filePath;

    public TextTrainer(final String filePath) {
        this.filePath = filePath;
    }

    @Override
    public TextBrain train() {
        final Timer timer = new Timer();
        final TextBrain brain = new LogicalTextBrain(GRAM_SIZE);

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(this.filePath);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                wordsFromLine(line).stream().forEach(brain::feed);
            }
            timer.stop();

            System.out.println("Brain stateCount: " + brain.stateCount());

            brain.compress();

            System.out.println("Brain stateCount: " + brain.stateCount() + " after compression");
            System.out.println("Brain feed time: " + timer.getS() + "s");
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return brain;
    }
}
