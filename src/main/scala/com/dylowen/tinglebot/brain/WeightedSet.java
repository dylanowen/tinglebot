package com.dylowen.tinglebot.brain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Random;

import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.procedure.TObjectIntProcedure;

/**
 * TODO add description
 *
 * @author dylan.owen
 * @since Feb-2016
 */
class WeightedSet<T>
    implements Serializable {
    private final TObjectIntHashMap<T> map = new TObjectIntHashMap<>();
    private int total = 0;

    private transient Random rand = new Random();

    public void add(final T obj) {
        add(obj, 1);
    }

    public void add(final T obj, final int inc) {
        this.map.adjustOrPutValue(obj, inc, inc);

        this.total += inc;
    }

    public T get() {
        TObjectIntIterator<T> iterator = this.map.iterator();
        int offset = 0;

        for (int i = this.map.size(); i > 0; i--) {
            iterator.advance();

            final int rand = this.rand.nextInt(this.total) + 1;
            offset += iterator.value();
            if (rand <= offset) {
                return iterator.key();
            }
        }

        throw new AssertionError("we should never get here");
    }

    public void forEachEntry(TObjectIntProcedure<T> procedure) {
        this.map.forEachEntry(procedure);
    }

    public int size() {
        return this.map.size();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        TObjectIntIterator<T> iterator = this.map.iterator();

        for (int i = this.map.size(); i > 0; i--) {
            iterator.advance();

            sb.append(iterator.key());
            sb.append(":");
            sb.append(iterator.value());
            sb.append(" ");
        }

        return sb.toString();
    }

    private void readObject(final ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.rand = new Random();
    }
}
