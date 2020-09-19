package CmdVer1;

import static CmdVer1.Dictionary.listOfWord;

public class DictionaryCommandline {

    public static void showAllWords() {
        int numOfFirstLineTabs = 3;
        System.out.print("No\t| English");
        for (int j = 0; j < numOfFirstLineTabs; j++) {
            System.out.print("\t");
        }
        System.out.println("| Vietnamese");
        int currentWords = listOfWord.size();
        for(int i = 0; i < currentWords; i++) {
            System.out.print( (i + 1) + "\t| " + listOfWord.get(i).getWord_target() );
            int numOfTabs = ( 9 + numOfFirstLineTabs * 4 - listOfWord.get(i).getWord_target().length() ) / 4;
            for (int j = 0; j < numOfTabs; j++) {
                System.out.print("\t");
            }
            System.out.println("| " + listOfWord.get(i).getWord_explain());
        }
    }

    public static void dictionaryBasic() {
        DictionaryManagement.insertFromCommandline();
        showAllWords();
    }

    public static void dictionaryIntermediate() {
        DictionaryManagement.insertFromFile();
        showAllWords();
    }

    public static void dictionaryAdvanced() {
        DictionaryManagement.insertFromFile();
        showAllWords();
        DictionaryManagement.dictionaryLookup();
    }

    public static void main(String[] args) {
        System.out.println("Progress: 2/6");
        dictionaryIntermediate();
    }
}
