package RawDictionary;

public class Word {
    private String word_target;
    private String word_explain;
    private String long_explain;

    public Word(String word_target, String word_explain) {
        this.word_target = word_target;
        this.word_explain = word_explain;
        this.long_explain = null;
    }

    public Word(String word_target, String word_explain, String long_explain) {
        this.word_target = word_target;
        this.word_explain = word_explain;
        this.long_explain = long_explain;
    }

    public String getWord_target() {
        return this.word_target;
    }

    public String getWord_explain() {
        return this.word_explain;
    }

    public String getLong_explain() {
        return long_explain;
    }

    public void setWord_target(String word_target) {
        this.word_target = word_target;
    }

    public void setWord_explain(String word_explain) {
        this.word_explain = word_explain;
    }

    public void setLong_explain(String long_explain) {
        this.long_explain = long_explain;
    }

    public boolean sameExplanation(Word another) {
        return another.word_explain.equalsIgnoreCase(this.word_explain);
    }

    public boolean sameTarget(Word another) {
        return another.word_target.equalsIgnoreCase(this.word_target);
    }

    private static boolean isnotVieLetter(int n) {
        if (n > 64 && n < 91) return false;
        if (n > 96 && n < 123) return false;
        if (n > 191 && n < 433) return false;
        return n <= 7839 || n >= 7930;
    }

    public int indexOfExactTarget(String eng) {
        if(eng.equalsIgnoreCase(this.word_target)) return 0;

        String eng1 = eng.toLowerCase();
        String wordTarget = this.word_target.toLowerCase();
        int Pos = wordTarget.indexOf(eng1);
        if(Pos < 0) return -1;

        int n = eng1.length();
        do {
            int left = Pos - 1;
            int right = Pos + n;
            //System.out.println(Pos + " " + left + " " + right);
            if (left < 0 || isnotVieLetter(wordTarget.charAt(left)))
                if (right >= wordTarget.length() || isnotVieLetter(wordTarget.charAt(right))) {
                    //System.out.print(Pos + "\n");
                    return Pos;
                }
            Pos = wordTarget.indexOf(eng1, Pos + 1);
        } while (Pos > -1);
        //System.out.print(Pos + "\n");
        return Pos;
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

/*
 * Setup on Google Script to use API translate
 * var mock = {
 *  parameter:{
 *    q:'hello',
 *    source:'en',
 *    target:'vi'
 *  }
 *};
 *
 *
 *function doGet(e) {
 *  e = e || mock;
 *
 *  var sourceText = ''
 *  if (e.parameter.q){
 *    sourceText = e.parameter.q;
 *  }
 *
 *  var sourceLang = '';
 *  if (e.parameter.source){
 *    sourceLang = e.parameter.source;
 *  }
 *
 *  var targetLang = 'en';
 *  if (e.parameter.target){
 *    targetLang = e.parameter.target;
 *  }
 *
 *  var translatedText = LanguageApp.translate(sourceText, sourceLang, targetLang, {contentType: 'html'});
 *
 *  return ContentService.createTextOutput(translatedText).setMimeType(ContentService.MimeType.JSON);
 *}

 */
