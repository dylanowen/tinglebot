package com.dylowen.tinglebot.brain;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.dylowen.tinglebot.Timer;

/**
 * T the word type
 * V the sentence type
 *
 * @author dylan.owen
 * @since Feb-2016
 */
public abstract class Brain<T, V>
    implements Serializable {

    protected transient static final int MIN_GRAM_SIZE = 2;

    protected final Map<NGram<T>, WeightedSet<T>> dictionary = new HashMap<>();
    protected int gramSize;

    private transient Random rand = new Random();
    private transient Map.Entry<NGram<T>, WeightedSet<T>>[] cachedEntryArray = null;
    private transient LinkedList<T> input = null;

    public Brain(final int gramSize) {
        this.gramSize = gramSize;
    }

    public void feed(final T word) {
        if (this.input == null) {
            this.input = new LinkedList<>();
        }
        this.input.add(word);
        //System.out.println(word);

        //not enough input
        if (this.input.size() <= this.gramSize) {
            return;
        }

        //create the NGrams
        for (int i = 0; i <= this.gramSize - MIN_GRAM_SIZE; i++) {
            final List<T> subList = this.input.subList(i, this.input.size() - 1);
            final NGram<T> nGram = new NGram<>(subList);

            WeightedSet<T> set = this.dictionary.get(nGram);
            if (set == null) {
                set = new WeightedSet<>();

                this.dictionary.put(nGram, set);
            }

            set.add(word);

            //System.out.println(nGram.toString() + " [" + set.toString() + "]");
        }

        this.input.removeFirst();
    }

    public void compress() {}

    public V getSentence() {
        final List<T> words = getSentenceWords();

        return concatSentence(words);
    }

    public abstract V concatSentence(final List<T> words);

    public List<T> getSentenceWords() {
        final Map.Entry<NGram<T>, WeightedSet<T>> first = getRandomEntry();

        final LinkedList<T> sentence = new LinkedList<T>(first.getKey().getWords());
        continueSentence(sentence);

        return sentence;
    }

    public List<T> getSentenceWords(final LinkedList<T> input) {
        final LinkedList<T> sentence = new LinkedList<>(input.subList(input.size() - this.gramSize, input.size()));

        continueSentence(sentence);

        //strip out our input
        return sentence.subList(this.gramSize - 1, sentence.size());
    }

    private void continueSentence(final LinkedList<T> sentence) {
        T nextWord;
        do {
            nextWord = getNextWord(sentence);
            sentence.add(nextWord);
        } while (shouldContinueSentence(nextWord, sentence));
        //System.out.println(GenericWordType.getType(nextWord).name() + " " + nextWord);
    }

    protected abstract boolean shouldContinueSentence(final T nextWord, final LinkedList<T> sentence);

    protected T getNextWord(final LinkedList<T> input) {
        final int startIndex = (this.gramSize > input.size()) ? 0 : input.size() - this.gramSize;
        final List<T> operatingList = new LinkedList<>(input.subList(startIndex, input.size()));

        WeightedSet<T> set;
        do {
            final NGram operatingGram = new NGram<>(operatingList);
            set = this.dictionary.get(operatingGram);

            //System.out.println(operatingGram.toString() + ((set != null) ? " [" + set.toString() + "]" : ""));
            //System.out.println(operatingList.size() + " " + ((set != null) ? set.size() + " " : "")
            //        + (operatingList.size() > MIN_GRAM_SIZE) + " " + (set == null || set.size() <= 1));

            operatingList.remove(0);
            //loop if we get a null set or the set size is uninteresting and we still have an operating list
        } while (operatingList.size() > MIN_GRAM_SIZE - 1 && (set == null || set.size() <= 1));

        //we've run out of stuff to say
        if (set == null) {
            return null;
        }

        return set.get();
    }

    //this could be better
    private Map.Entry<NGram<T>, WeightedSet<T>> getRandomEntry() {
        if (this.cachedEntryArray == null || this.cachedEntryArray.length != this.dictionary.size()) {

            final Set<Map.Entry<NGram<T>, WeightedSet<T>>> entrySet = this.dictionary.entrySet();
            //TODO can I get rid of this unchecked?
            @SuppressWarnings("unchecked")
            final Map.Entry<NGram<T>, WeightedSet<T>>[] entryArray = new Map.Entry[entrySet.size()];

            this.cachedEntryArray = entrySet.toArray(entryArray);
        }

        Map.Entry<NGram<T>, WeightedSet<T>> entry;
        do {
            entry = this.cachedEntryArray[this.rand.nextInt(this.cachedEntryArray.length)];
        } while (entry.getKey().getWords().size() < this.gramSize);

        return entry;
    }

    public int stateCount() {
        return this.dictionary.size();
    }

    public void export(final String exportPath) {
        final Timer timer = new Timer();

        try {
            FileOutputStream fileOut = new FileOutputStream(exportPath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            System.out.println("Serialized brain is saved in: " + exportPath);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        System.out.println("Brain export time: " + timer.getS() + "s");
    }

    private void readObject(final ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.rand = new Random();
    }
}
