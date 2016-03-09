package com.dylowen.tinglebot.brain;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * this brain is more compressed than the default brain and leans more heavily on logic
 *
 * @author dylan.owen
 * @since Feb-2016
 */
public class LogicalTextBrain extends TextBrain {
    public LogicalTextBrain(final int gramSize) {
        super(gramSize);
    }

    //TODO some of this isn't very extensible, Brain should be refactored slightly
    @Override
    public void compress() {
        final Iterator<Map.Entry<NGramJava<String>, WeightedSet<String>>> it = this.dictionary.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry<NGramJava<String>, WeightedSet<String>> entry = it.next();
            final NGramJava<String> nGram = entry.getKey();

            //only compress things above the min gram size
            if (nGram.size() > MIN_GRAM_SIZE) {
                final WeightedSet<String> minSet = getMinGramSet(entry.getKey());
                entry.getValue().forEachEntry(((word, count) -> {
                    final int weight = nGram.size() - 1;
                    minSet.add(word, count * weight);

                    return true;
                }));

                it.remove();
            }
        }

        //optimize the lookup
        this.gramSize = MIN_GRAM_SIZE;
    }

    private WeightedSet<String> getMinGramSet(final NGramJava<String> gram) {
        final List<String> subList = gram.getWords().subList(gram.size() - MIN_GRAM_SIZE, gram.size());
        final NGramJava<String> minGram = new NGramJava<>(subList);

        return this.dictionary.get(minGram);
    }
}
