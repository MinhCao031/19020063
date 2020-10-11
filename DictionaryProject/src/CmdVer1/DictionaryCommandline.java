package CmdVer1;

import java.util.Scanner;

interface DCmdLine {
    void showAllWords();
    void dictionaryBasic();
    void dictionaryIntermediate();
    void dictionaryAdvanced();
    void dictionarySearcher(String find);
}

public class DictionaryCommandline implements DCmdLine{
    private final DictionaryManagement dictionaryManagement;

    public DictionaryCommandline() {
        dictionaryManagement = new DictionaryManagement();
    }

    DictionaryCommandline(DictionaryCommandline dictionaryCommandline) {
        this.dictionaryManagement = dictionaryCommandline.dictionaryManagement;
    }

    public DictionaryManagement getDictionaryManagement() {
        return dictionaryManagement;
    }

    public void showAllWords() {
        // English word(s): up to 40 characters.
        System.out.printf("%-8s| %-41s| %s\n","No", "English", "Vietnamese");
        int currentWords = dictionaryManagement.getDictionary().getSize();
        for(int i = 0; i < currentWords; i++) {
            System.out.printf("%-8d| %-41s| %s\n", i + 1,
                dictionaryManagement.getDictionary().getListOfWord().get(i).getWord_target(),
                dictionaryManagement.getDictionary().getListOfWord().get(i).getWord_explain());
        }
    }

    public void dictionaryBasic() {
        dictionaryManagement.insertFromCommandline();
        showAllWords();
    }

    public void dictionaryIntermediate() {
        dictionaryManagement.insertFromFile();
        showAllWords();
    }

    public void dictionaryAdvanced() {
        dictionaryManagement.insertFromFile();
        showAllWords();
        dictionaryManagement.dictionaryLookup();
    }

    public void dictionarySearcher(String find) {
        String ans = "";
        find = find.toLowerCase();
        for (Word word : dictionaryManagement.getDictionary().getListOfWord()) {
            if (word.doesTargetHave(find)) {
                ans = ans.concat(word.getWord_target().concat("\t"));
                ans = ans.concat(word.getWord_explain().concat("\n"));
            }
        }
        System.out.println(ans);
    }
}
