package com.dylowen.tinglebot.train;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.dylowen.tinglebot.Timer;
import org.apache.commons.lang3.StringEscapeUtils;

import com.dylowen.tinglebot.brain.Brain;

/**
 * TODO add description
 *
 * @author dylan.owen
 * @since Feb-2016
 */
public class TextTrainer
    extends Trainer {

    private final String filePath;
    private final int gramSize;

    public TextTrainer(final String filePath, final int gramSize) {
        this.filePath = filePath;
        this.gramSize = gramSize;
    }

    @Override
    public Brain train() {
        final Timer timer = new Timer();
        final Brain brain = new Brain(this.gramSize);

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
            System.out.println("Brain feed time: " + timer.getS() + "s");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        return brain;
    }

    /*
    @Override
    protected String readLine(final BufferedReader buffer)
            throws IOException {

        String str = super.readLine(buffer);

        return str;
    }
    */




}
