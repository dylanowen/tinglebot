package com.dylowen.tinglebot.train;

import com.dylowen.tinglebot.brain.Brain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * TODO add description
 *
 * @author dylan.owen
 * @since Feb-2016
 */
public class TextTrainer implements Trainer {

    private final String filePath;
    private final int gramSize;

    public TextTrainer(final String filePath, final int gramSize) {
        this.filePath = filePath;
        this.gramSize = gramSize;
    }

    @Override
    public Brain train() {
        final Brain brain = new Brain();

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(this.filePath);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String word;
            LinkedList<String> words = new LinkedList<>();
            while ((word = readWord(bufferedReader)) != null) {
                words.addLast(word);

                //System.out.println(word);

                if (words.size() < this.gramSize + 1) {
                    continue;
                }

                brain.add(words);

                words.removeFirst();
            }

            System.out.println("Brain stateCount: " + brain.stateCount());

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return brain;
    }

    //this is super crude
    //TODO &apos;
    private static String readWord(final BufferedReader buffer)
            throws IOException {
        StringBuilder sb = new StringBuilder();

        boolean foundBracket = false;

        while (true) {
            final int num = buffer.read();

            //end of the stream
            if (-1 == num) {
                break;
            }

            final char c = (char) num;

            //skip over brackets
            if (foundBracket) {
                //break out of our bracket search
                if ('>' == c) {
                    foundBracket = false;
                }
                continue;
            }
            if ('<' == c) {
                foundBracket = true;
                continue;
            }

            if (Character.isWhitespace(c)) {
                //if we have a word we're done, otherwise eat up white space
                if (sb.length() > 0) {
                    break;
                }
                else {
                    continue;
                }
            }

            //skip non alphabetic characters
            if (Character.isAlphabetic(c) || '(' == c || ')' == c) {
                sb.append(c);
            }
        }

        if (sb.length() <= 0) {
            return null;
        }

        return sb.toString().toLowerCase();
    }
}
