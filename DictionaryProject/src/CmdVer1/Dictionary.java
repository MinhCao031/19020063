package CmdVer1;

import java.util.ArrayList;

interface SmallDictionary {
    ArrayList<Word> getListOfWord();
    String getStringOfWord();
    int getSize();
    boolean add(Word newWord);
    boolean remove(Word oldWord);
}

public class Dictionary implements SmallDictionary {
    private final ArrayList<Word> listOfWord;

    Dictionary() {
        listOfWord = new ArrayList<>(0);
    }
/*
    Dictionary(Dictionary dictionary) {
        this.listOfWord = dictionary.listOfWord;
    }
*/
    public ArrayList<Word> getListOfWord() {
        return listOfWord;
    }

    public String getStringOfWord() {
        String longString = "";
        for(Word s: listOfWord) {
            longString += s.getWord_target().concat("\t");
            longString += s.getWord_explain().concat("\n");
        }
        return longString;
    }

    public int getSize() {
        return listOfWord.size();
    }

    public boolean add(Word newWord) {
        for(Word s: listOfWord) {
            if (!s.sameTarget(newWord)) {
                continue;
            }
            if(s.sameExplanation(newWord)) {
                //print
                System.out.println("No need to add, it's already in the dictionary.");
                return false;
            } else {
                String newVie = s.getWord_explain().concat(", ".concat(newWord.getWord_explain()));
                s.setWord_explain(newVie);
                //print
                System.out.println("The english word is available, so we update that word's explanation.");
                return true;
            }
        }
        //print
        System.out.println("New word added successfully.");
        listOfWord.add(newWord);
        return true;
    }

    public boolean remove(Word oldWord) {
        return listOfWord.remove(oldWord);
    }

}
