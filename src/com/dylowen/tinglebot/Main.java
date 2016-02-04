package com.dylowen.tinglebot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import com.dylowen.tinglebot.brain.Brain;

/**
 * TODO add description
 *
 * @author dylan.owen
 * @since Feb-2016
 */
public class Main {

    public static void main(String[] args) {
        final Brain brain = new Brain();

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader("/Users/dylan.owen/Desktop/war.txt");

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String word;

            LinkedList<String> words = new LinkedList<>();
            while ((word = readWord(bufferedReader)) != null) {
                words.addLast(word);

                //System.out.println(word);

                if (words.size() < 3) {
                    continue;
                }

                brain.add(words);

                words.removeFirst();
            }

            System.out.println("Brain stateCount: " + brain.stateCount());

            LinkedList<String> sentence = brain.start();

            for (int i = 0; i < 20; i++) {
                final String nextWord = brain.get(sentence.get(sentence.size() - 2), sentence.getLast());

                if (Brain.END_STRING != nextWord) {
                    sentence.add(nextWord);
                }
                else {
                    break;
                }
            }

            System.out.println();
            for (String sentenceWord : sentence) {
                System.out.print(sentenceWord + " ");
            }
            System.out.println();

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
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

    private static class BiGram {
        final String one;
        final String two;

        BiGram(final String one, final String two) {
            this.one = one;
            this.two = two;
        }

        @Override
        public String toString() {
            return this.one + " " + this.two;
        }

        @Override
        public int hashCode() {
            return this.one.hashCode() + 31 * this.two.hashCode();
        }

        @Override
        public boolean equals(final Object object) {
            if (!(object instanceof BiGram)) {
                return false;
            }

            final BiGram right = (BiGram) object;

            return this.one.equals(right.one) && this.two.equals(right.two);
        }

    }
}
