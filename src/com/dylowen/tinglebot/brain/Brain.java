package com.dylowen.tinglebot.brain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * TODO add description
 *
 * @author dylan.owen
 * @since Feb-2016
 */
public class Brain
    implements Serializable {

    private transient static final int MIN_GRAM_SIZE = 2;
    private transient static final int MAX_WORDS_IN_SENTENCE = 100;

    private final Map<NGram, WeightedSet<String>> dictionary = new HashMap<>();
    private final int gramSize;

    private final transient Random rand = new Random();
    private transient Map.Entry<NGram, WeightedSet<String>>[] cachedEntryArray = null;
    private transient LinkedList<String> input = null;

    public Brain(final int gramSize) {
        this.gramSize = gramSize;
    }

    public void feed(final String word) {
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
            final List<String> subList = this.input.subList(i, this.input.size() - 1);
            final NGram nGram = new NGram(subList);

            WeightedSet<String> set = this.dictionary.get(nGram);
            if (set == null) {
                set = new WeightedSet<>();

                this.dictionary.put(nGram, set);
            }

            set.add(word);

            //System.out.println(nGram.toString() + " [" + set.toString() + "]");
        }

        this.input.removeFirst();
    }

    public String getSentence() {
        final List<String> words = getSentenceWords();

        return Brain.concatSentence(words);
    }

    public List<String> getSentenceWords() {
        final Map.Entry<NGram, WeightedSet<String>> first = getRandomEntry();

        final LinkedList<String> sentence = new LinkedList<>(first.getKey().getWords());
        continueSentence(sentence);

        return sentence;
    }

    public List<String> getSentenceWords(final LinkedList<String> input) {
        final LinkedList<String> sentence = new LinkedList<>(input.subList(input.size() - this.gramSize, input.size()));

        continueSentence(sentence);

        //strip out our input
        return sentence.subList(this.gramSize - 1, sentence.size());
    }

    private void continueSentence(final LinkedList<String> sentence) {
        String nextWord;
        do {
            nextWord = getNextWord(sentence);
            sentence.add(nextWord);
        } while (GenericWordType.getType(nextWord) != GenericWordType.END_SENTENCE && sentence.size() < MAX_WORDS_IN_SENTENCE);
        System.out.println(GenericWordType.getType(nextWord).name() + " " + nextWord);
    }

    private String getNextWord(final LinkedList<String> input) {
        final int startIndex = (this.gramSize > input.size()) ? 0 : input.size() - this.gramSize;
        final List<String> operatingList = new LinkedList<>(input.subList(startIndex, input.size()));

        WeightedSet<String> set;
        do {
            final NGram operatingGram = new NGram(operatingList);
            set = this.dictionary.get(operatingGram);
            
            System.out.println(operatingGram.toString() + ((set != null) ? " [" + set.toString() + "]" : ""));
            System.out.println(operatingList.size() + " " + ((set != null) ? set.size() + " " : "") + (operatingList.size() > MIN_GRAM_SIZE) + " " + (set == null || set.size() <= 1));

            operatingList.remove(0);
            //loop if we get a null set or the set size is uninteresting and we still have an operating list
        } while (operatingList.size() > MIN_GRAM_SIZE - 1 && (set == null || set.size() <= 1));

        //we've run out of stuff to say
        if (set == null) {
            return ".";
        }

        return set.get();
    }

    //this could be better
    private Map.Entry<NGram, WeightedSet<String>> getRandomEntry() {
        if (this.cachedEntryArray == null || this.cachedEntryArray.length != this.dictionary.size()) {

            final Set<Map.Entry<NGram, WeightedSet<String>>> entrySet = this.dictionary.entrySet();
            //TODO can I get rid of this unchecked?
            @SuppressWarnings("unchecked")
            final Map.Entry<NGram, WeightedSet<String>>[] entryArray = new Map.Entry[entrySet.size()];

            this.cachedEntryArray = entrySet.toArray(entryArray);
        }

        Map.Entry<NGram, WeightedSet<String>> entry;
        do {
            entry = this.cachedEntryArray[this.rand.nextInt(this.cachedEntryArray.length)];
        } while(entry.getKey().getWords().size() < this.gramSize);

        return entry;
    }

    public static String concatSentence(final List<String> words) {
        final StringBuilder sb = new StringBuilder();
        final Iterator<String> it = words.listIterator();

        GenericWordType.START_SENTENCE.appendSpacedWord(it.next(), sb);

        while (it.hasNext()) {
            GenericWordType.appendUntypedWord(it.next(), sb);
        }

        return sb.toString();
    }

    /*
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
    */

    /*
    public LinkedList<String> start() {
        @SuppressWarnings("unchecked")
        Map.Entry<NGram, WeightedSet<String>> entry =
    
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
    */

    public int stateCount() {
        return this.dictionary.size();
    }
}
