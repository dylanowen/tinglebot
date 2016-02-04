package com.dylowen.tinglebot.brain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * TODO add description
 *
 * @author dylan.owen
 * @since Feb-2016
 */
public class Brain
    implements Serializable {
    public static final String END_STRING = new String();
    private final Map<NGram, WeightedSet<String>> dictionary = new HashMap<>();

    private final transient Random rand = new Random();

    public void add(final String... words) {
        final NGram nGram = new NGram(Arrays.copyOfRange(words, 0, words.length - 1));
        final String word = words[words.length - 1];

        WeightedSet<String> set = this.dictionary.get(nGram);
        if (set == null) {
            set = new WeightedSet<>();
            this.dictionary.put(nGram, set);
        }
        else {
            //System.out.println(nGram.toString() + " [" + set.toString() + "]");
        }
        set.add(word);
    }

    public void add(final List<String> words) {
        add(words.toArray(new String[words.size()]));
    }

    public LinkedList<String> start() {
        @SuppressWarnings("unchecked")
        Map.Entry<NGram, WeightedSet<String>> entry = (Map.Entry<NGram, WeightedSet<String>>) this.dictionary.entrySet().toArray()[rand.nextInt(
                this.dictionary.size())];

        System.out.println(entry.getKey().toString() + " [" + entry.getValue().toString() + "]");

        LinkedList<String> sentenceStart = new LinkedList<>();
        Collections.addAll(sentenceStart, entry.getKey().getWords());

        sentenceStart.add(entry.getValue().get());

        return sentenceStart;
    }

    public String get(final List<String> words) {
        return get(words.toArray(new String[words.size()]));
    }

    public String get(final String... words) {
        final NGram nGram = new NGram(words);

        final WeightedSet<String> set = this.dictionary.get(nGram);
        if (set != null) {
            System.out.println(nGram.toString() + " [" + set.toString() + "]");

            return set.get();
        }
        else {
            return END_STRING;
        }
    }

    public int stateCount() {
        return this.dictionary.size();
    }
}
