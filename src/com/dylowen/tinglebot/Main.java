package com.dylowen.tinglebot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * TODO add description
 *
 * @author dylan.owen
 * @since Feb-2016
 */
public class Main {

    public static void main(String[] args) {
        Map<BiGram, String> dictionary = new HashMap<>();

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader("");

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String word;

            int i = 0;
            LinkedList<String> words = new LinkedList<>();
            while ((word = readWord(bufferedReader)) != null) {
                words.addLast(word);

                System.out.println(word);

                if (words.size() < 3) {
                    continue;
                }

                System.out.println(words.get(0) + " " + words.get(1) + " " + words.get(2));

                final BiGram biGram = new BiGram(words.get(0), words.get(1));
                if(dictionary.containsKey(biGram)) {
                    System.out.println(biGram.toString() + " " + biGram.hashCode());
                }
                else {
                    dictionary.put(biGram, words.get(2));
                }

                words.removeFirst();
            }


            System.out.println(dictionary.size());

        }
        catch (Exception e) {
            //whatever
        }
    }

    //this is super crude
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

        return sb.toString();
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
