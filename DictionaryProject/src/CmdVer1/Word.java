package CmdVer1;

public class Word {
    private String word_target;
    private String word_explain;

    public Word(String word_target, String word_explain) {
        this.word_target = word_target;
        this.word_explain = word_explain;
    }

    public String getWord_target() {
        return this.word_target;
    }

    public String getWord_explain() {
        return this.word_explain;
    }

    public void setWord_target(String word_target) {
        this.word_target = word_target;
    }

    public void setWord_explain(String word_explain) {
        this.word_explain = word_explain;
    }

    public boolean sameTarget(Word another) {
        return another.word_target.equalsIgnoreCase(this.word_target);
    }

    public boolean sameExplanation(Word another) {
        return another.word_explain.equalsIgnoreCase(this.word_explain);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return word_target.equalsIgnoreCase(word.word_target) &&
                word_explain.equalsIgnoreCase(word.word_explain);
    }
}

