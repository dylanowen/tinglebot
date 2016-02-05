package com.dylowen.tinglebot.brain;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO add description
 *
 * @author dylan.owen
 * @since Feb-2016
 */
class NGram
    implements Serializable {
    private final List<String> words;

    NGram(final List<String> words) {
        //defensively copy
        this.words = new LinkedList(words);
    }

    public List<String> getWords() {
        return this.words;
    }

    @Override
    public String toString() {
        final Iterator<String> it = this.words.listIterator();
        final StringBuilder sb = new StringBuilder(it.next());

        while (it.hasNext()) {
            sb.append(" ");
            sb.append(it.next());
        }

        return sb.toString();
    }

    @Override
    public int hashCode() {
        return this.words.hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof NGram)) {
            return false;
        }

        return this.words.equals(((NGram) object).getWords());

        //TODO compare without capitalization?
    }
}
