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

    /**
     * Change int n to char, if it's a Vietnamese letter, return true.
     * Else (most case), return false.
     */
    private boolean isVieLetter(int n) {
        if (n > 64 && n < 91) return true;
        if (n > 96 && n < 123) return true;
        if (n > 191 && n < 433) return true;
        return n > 7839 && n < 7930;
    }

    public boolean doesTargetHave(String eng) {
        if(eng.equalsIgnoreCase(word_target)) return true;

        eng = eng.toLowerCase();
        word_target = word_target.toLowerCase();
        int Pos = word_target.indexOf(eng);
        if(Pos < 0) return false;

        int n = eng.length();
        do {
            int left = Pos - 1;
            int right = Pos + n;
            //System.out.println(Pos + " " + left + " " + right);

            if (left < 0 || !isVieLetter(word_target.charAt(left)))
                if (right >= word_target.length() || !isVieLetter(word_target.charAt(right))) {
                    //System.out.print(Pos + "\n");
                    return true;
                }
            Pos = word_target.indexOf(eng, Pos + 1);
        } while (Pos > -1);
        //System.out.print(Pos + "\n");
        return false;
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

