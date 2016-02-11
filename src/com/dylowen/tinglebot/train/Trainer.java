package com.dylowen.tinglebot.train;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.dylowen.tinglebot.brain.Brain;
import com.sun.deploy.util.StringUtils;

/**
 * TODO add description
 *
 * @author dylan.owen
 * @since Feb-2016
 */
public abstract class Trainer {

    private static final Pattern INVALID_CHARS = Pattern.compile("[^a-zA-Z0-9'!?.]");

    public abstract Brain train();

    public static List<String> wordsFromLine(String line) {
        final List<String> dirtyWords = Arrays.asList(line.split(" ")).parallelStream().filter(
                word -> !(word.startsWith("http") || word.length() == 0)).collect(Collectors.toList());
        final List<String> cleanWords = new ArrayList<>();

        for (String word : dirtyWords) {
            final StringBuilder sb = new StringBuilder();
            boolean emoji = '(' == word.charAt(0) && ')' == word.charAt(word.length() - 1);
            if (emoji) {
                sb.append("(");
            }

            boolean foundEnd = false;
            for (int i = 0; i < word.length() - 1; i++) {
                final char c = word.charAt(i);
                if (Character.isAlphabetic(c) || Character.isDigit(c) || '\'' == c) {
                    sb.append(c);
                }
                else if ('!' == c || '?' == c || '.' == c) {
                    foundEnd = true;
                    sb.append(c);
                }
            }

            String nextWord = null;
            final char c = word.charAt(word.length() - 1);
            if (Character.isAlphabetic(c) || Character.isDigit(c) || '\'' == c) {
                sb.append(c);
            }
            else if ('!' == c || '?' == c || '.' == c) {
                if (foundEnd) {
                    sb.append(c);
                }
                else {
                    nextWord = Character.toString(c);
                }
            }

            if (emoji) {
                sb.append(")");
            }

            cleanWords.add(sb.toString());
            if (nextWord != null) {
                cleanWords.add(nextWord);
            }
        }

        return cleanWords;
    }
}
