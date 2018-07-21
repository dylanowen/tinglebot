package com.dylowen.tinglebot.brain;

/**
 * TODO add description
 *
 * @author dylan.owen
 * @since Feb-2016
 */
public enum GenericWordType implements WordType {
    WORD(true),
    START_SENTENCE(false),
    PUNCTUATION(false),
    END_SENTENCE(false);

    private final boolean leadingSpace;

    GenericWordType(final boolean leadingSpace) {
        this.leadingSpace = leadingSpace;
    }

    public void appendSpacedWord(final String word, final StringBuilder sb) {
        if (this.leadingSpace) {
            sb.append(" ");
        }

        sb.append(word);
    }

    public static void appendUntypedWord(final String word, final StringBuilder sb) {
        final GenericWordType type = getType(word);

        type.appendSpacedWord(word, sb);
    }

    public static String getSpacedWord(final String word) {
        return GenericWordType.getType(word).leadingSpace ? " " + word : word;
    }

    public static GenericWordType getType(final String word) {
        if (".".equals(word) || "?".equals(word) || "!".equals(word)) {
            return END_SENTENCE;
        }
        else if (",".equals(word) || ";".equals(word)) {
            return PUNCTUATION;
        }

        return WORD;
    }
}
