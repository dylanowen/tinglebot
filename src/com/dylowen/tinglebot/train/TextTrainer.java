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
    implements Trainer {

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
            while ((line = readLine(bufferedReader)) != null) {
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

    private static String readLine(final BufferedReader buffer)
            throws IOException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            final int num = buffer.read();
            if (-1 == num) { //end
                return null;
            }
            final char c = (char) num;
            if (c == '\n') {
                break;
            }
            sb.append(c);
        }

        String str = sb.toString();
        str = str.replaceAll("\\<.*?\\> ?", "");
        str = str.replaceAll("\\[.*?\\] ?", "");
        return str;
    }

    public static ArrayList<String> wordsFromLine(String line) {
        ArrayList<String> words;
        words = new ArrayList<>(Arrays.asList(line.split(" ")));
        for (int i = 0; i < words.size(); i++) {
            words.set(i, prune(words.get(i)));
        }

        for (Iterator<String> iterator = words.iterator(); iterator.hasNext();) {
            String string = iterator.next();
            if (string.isEmpty()) {
                // Remove the current element from the iterator and the list.
                iterator.remove();
            }
        }
        return words;
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
            //            if (Character.isAlphabetic(c) || '(' == c || ')' == c) {
            //                sb.append(c);
            //            }
            sb.append(c);
        }

        if (sb.length() <= 0) {
            return null;
        }
        String prePruned = sb.toString();
        String pruned = prune(prePruned);

        //        if (!prePruned.equals(pruned)){
        //            System.out.println(prePruned + " -> " + pruned);
        //        }

        return pruned;
    }

    private static String prune(String word) {
        String prePruned = word;
        word = StringEscapeUtils.unescapeXml(word);
        word = word.toLowerCase();
        if (word.startsWith("http"))
            return "";
        boolean emoji = word.startsWith("(") && word.endsWith(")");
        word = word.replaceAll("[^a-z0-9'!?.]", "");
        if (emoji)
            word = "(" + word + ")";
        //        if (!prePruned.equals(word)){
        //            System.out.println(prePruned + " -> " + word);
        //        }
        return word;
    }
}
