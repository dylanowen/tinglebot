package com.dylowen.tinglebot.brain;

import java.io.Serializable;

/**
 * TODO add description
 *
 * @author dylan.owen
 * @since Feb-2016
 */
class NGram implements Serializable {
    private final String[] words;
    private transient int hash = 0;

    NGram(final String... words) {
        this.words = words;
    }

    public String[] getWords() {
        return this.words;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(this.words[0]);

        for (int i = 1; i < this.words.length; i++) {
            sb.append(" ");
            sb.append(this.words[i]);
        }

        return sb.toString();
    }

    @Override
    public int hashCode() {
        //copied right from String.hashCode()
        int h = hash;
        if (h == 0 && this.words.length > 0) {
            for (final String word : this.words) {
                h = 31 * h + word.hashCode();
            }
            this.hash = h;
        }
        return h;
    }

    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof NGram)) {
            return false;
        }

        final String[] rightWords = ((NGram) object).getWords();

        //these aren't equal if they're different lengths
        if (this.words.length != rightWords.length) {
            return false;
        }

        //loop over all the words checking their equality
        for (int i = 0; i < this.words.length; i++) {
            if (!this.words[i].equals(rightWords[i])) {
                return false;
            }
        }

        return true;
    }
}
