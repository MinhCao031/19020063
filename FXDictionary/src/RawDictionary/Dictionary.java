package RawDictionary;

import java.util.ArrayList;

public class Dictionary{
    private final ArrayList<Word> listOfWord;
    private final ArrayList<Word> myFav;

    Dictionary() {
        listOfWord = new ArrayList<>(0);
        myFav = new ArrayList<>(0);
    }

    public ArrayList<Word> getListOfWord() {
        return listOfWord;
    }

    public ArrayList<Word> getMyFav() {
        return myFav;
    }

    public int getListSize() {
        return listOfWord.size();
    }

    public int getFavSize() {
        return myFav.size();
    }

    public boolean add(Word newWord, boolean addToFav) {
        if(addToFav) {
            myFav.remove(newWord);
            myFav.add(newWord);
        }
        listOfWord.remove(newWord);
        return listOfWord.add(newWord);
    }

    public void addList(Dictionary another) {
        for(Word s: another.listOfWord) {
            this.add(s,false);
        }
    }

    public boolean removeFromList(Word oldWord) {
        return listOfWord.remove(oldWord);
    }

    public boolean removeFromFav(Word oldWord)  {
        return myFav.remove(oldWord);
    }

}
