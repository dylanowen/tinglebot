package com.dylowen.tinglebot;

import java.util.List;

import com.dylowen.tinglebot.brain.Brain;
import com.dylowen.tinglebot.train.SkypeDatabaseTrainer;
import com.dylowen.tinglebot.train.TextTrainer;
import com.dylowen.tinglebot.train.Trainer;

/**
 * TODO add description
 *
 * @author dylan.owen
 * @since Feb-2016
 */
public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Please enter a training file location");
            return;
        }

        //default to 3 for the gram size
        final int GRAM_SIZE = (args.length < 2) ? 3 : Integer.parseInt(args[1]);

        final String path = args[0];
        final int extensionIndex = path.lastIndexOf('.');
        final String extension = (extensionIndex < 0) ? "" : path.substring(extensionIndex + 1);

        final Trainer trainer;
        if ("json".equals(extension)) {
            trainer = new SkypeDatabaseTrainer(path, GRAM_SIZE);
        }
        else if ("txt".equals(extension)) {
            trainer = new TextTrainer(path, GRAM_SIZE);
        }
        /*
        else if ("brain".equals(extension)) {
            //SerializedBrainTrainer
        }
        */
        else {
            throw new UnsupportedOperationException("Unknown file type");
        }

        final Brain brain = trainer.train();

        final StringBuilder sb = new StringBuilder();

        List<String> sentence = brain.getSentenceList();
        sb.append(Brain.concatSentence(sentence));
        while (sb.length() < 140) {
            sb.append("\n");
            sentence = brain.getSentenceList();
            sb.append(Brain.concatSentence(sentence));
        }

        System.out.println(sb.toString());
    }
}
