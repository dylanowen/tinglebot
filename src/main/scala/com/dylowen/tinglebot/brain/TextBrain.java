package com.dylowen.tinglebot.brain;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO add description
 *
 * @author dylan.owen
 * @since Feb-2016
 */
public class TextBrain
    extends Brain<String, String> {

    private transient static final int MAX_WORDS_IN_SENTENCE = 100;

    public TextBrain(final int gramSize) {
        super(gramSize);
    }

    @Override
    protected String getNextWord(final LinkedList<String> input) {
        final String nextWord = super.getNextWord(input);

        return (nextWord == null) ? "." : nextWord;
    }

    @Override
    public String concatSentence(final List<String> words) {
        final StringBuilder sb = new StringBuilder();
        final Iterator<String> it = words.listIterator();

        GenericWordType.START_SENTENCE.appendSpacedWord(it.next(), sb);

        while (it.hasNext()) {
            GenericWordType.appendUntypedWord(it.next(), sb);
        }

        return sb.toString();
    }

    @Override
    protected boolean shouldContinueSentence(final String nextWord, final LinkedList<String> sentence) {
        return GenericWordType.getType(nextWord) != GenericWordType.END_SENTENCE
                && sentence.size() < MAX_WORDS_IN_SENTENCE;
    }
}
