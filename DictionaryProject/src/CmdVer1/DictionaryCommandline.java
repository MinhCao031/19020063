package CmdVer1;

interface DCmdLine {
    void showAllWords();
    void dictionaryBasic();
    void dictionaryIntermediate();
    void dictionaryAdvanced();
    StringBuilder dictionarySearcher(String find);
}

public class DictionaryCommandline implements DCmdLine{
    private final DictionaryManagement dictionaryManagement;

    DictionaryCommandline() {
        dictionaryManagement = new DictionaryManagement();
    }
/*
    DictionaryCommandline(DictionaryCommandline dictionaryCommandline) {
        this.dictionaryManagement = dictionaryCommandline.dictionaryManagement;
    }
*/
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

    public StringBuilder dictionarySearcher(String find) {
        StringBuilder ans = new StringBuilder();
        find = find.toLowerCase();
        for (Word word : dictionaryManagement.getDictionary().getListOfWord()) {
            if (word.getWord_target().indexOf(find) == 0) {
                ans.append(word.getWord_target());
                ans.append("\t");
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        System.out.println("Progress: 2/6");
        DictionaryCommandline DCml = new DictionaryCommandline();
        DCml.getDictionaryManagement().insertFromFile();
        DCml.getDictionaryManagement().autoAddNewWord("cOOl","Mat");
        if (DCml.getDictionaryManagement().dictionaryExportToFile()) {
            System.out.println("Exported successfully.");
        }
        DCml.showAllWords();
    }
}
