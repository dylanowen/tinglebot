package com.dylowen.tinglebot.train;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.dylowen.tinglebot.brain.Brain;

/**
 * TODO add description
 *
 * @author dylan.owen
 * @since Feb-2016
 */
public abstract class Trainer {

    protected static final int GRAM_SIZE = 3;

    public abstract Brain train();

    public static List<String> wordsFromLine(String line) {
        final List<String> dirtyWords = Arrays.asList(line.split(" ")).parallelStream().filter(
                word -> !(word.startsWith("http") || word.length() == 0)).collect(Collectors.toList());
        final List<String> cleanWords = new ArrayList<>();

        for (String dirtyWord : dirtyWords) {
            final StringBuilder sb = new StringBuilder();
            boolean emoji = '(' == dirtyWord.charAt(0) && ')' == dirtyWord.charAt(dirtyWord.length() - 1);
            if (emoji) {
                sb.append("(");
            }

            boolean foundEnd = false;
            for (int i = 0; i < dirtyWord.length() - 1; i++) {
                final char c = dirtyWord.charAt(i);
                if (isCharValid(c)) {
                    sb.append(c);

                    if (isCharEnd(c)) {
                        foundEnd = true;
                    }
                }
            }

            String nextWord = null;
            final char c = dirtyWord.charAt(dirtyWord.length() - 1);
            if (isCharValid(c)) {
                if (!isCharEnd(c) || foundEnd) {
                    sb.append(c);
                }
                else {
                    nextWord = Character.toString(c);
                }
            }

            if (emoji) {
                sb.append(")");
            }

            if (sb.length() > 0) {
                cleanWords.add(sb.toString());
            }
            if (nextWord != null) {
                cleanWords.add(nextWord);
            }
        }

        return cleanWords;
    }

    private static boolean isCharEnd(final char c) {
        return '!' == c || '?' == c || '.' == c;
    }

    private static boolean isCharValid(final char c) {
        return Character.isAlphabetic(c) || Character.isDigit(c) || '\'' == c || ':' == c || isCharEnd(c);
    }
}
