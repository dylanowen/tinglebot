package com.dylowen.tinglebot.brain;

/**
 * TODO add description
 *
 * @author dylan.owen
 * @since Feb-2016
 */
public enum WordType {
    WORD(true),
    START_SENTENCE(false),
    PUNCTUATION(false),
    END_SENTENCE(false);


    private final boolean leadingSpace;

    WordType(final boolean leadingSpace) {
        this.leadingSpace = leadingSpace;
    }

    public String getSpacedWord(final String word) {
        return ((this.leadingSpace) ? " " : "" ) + word;
    }

    public void appendSpacedWord(final String word, final StringBuilder sb) {
        if (this.leadingSpace) {
            sb.append(" ");
        }

        sb.append(word);
    }

    public static void appendUntypedWord(final String word, final StringBuilder sb) {
        final WordType type = getType(word);

        type.appendSpacedWord(word, sb);
    }

    public static WordType getType(final String word) {
        if (".".equals(word) || "?".equals(word) || ".".equals(word)) {
            return END_SENTENCE;
        }
        else if (",".equals(word) || ";".equals(word)) {
            return PUNCTUATION;
        }

        return WORD;
    }
}
