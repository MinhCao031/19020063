package RawDictionary;

import FxGraphics.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DictionaryManagement {
    private final Dictionary dictionary;

    public DictionaryManagement() {
        this.dictionary = new Dictionary();
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void insertFromFile(String fileName, boolean isFavoriteFile) {
        try {
            File wordSource = new File(fileName);
            Scanner myReader = new Scanner(wordSource);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                int separate = data.indexOf("\t");
                int secondSeparate = data.indexOf("\t", separate + 1);
                //System.out.println(separate);
                if (separate < 0) {
                    System.out.println(data);
                    continue;
                }
                String eng = data.substring(0, separate).toLowerCase();
                eng = eng.substring(0,1).toUpperCase().concat(eng.substring(1));
                if (secondSeparate > -1) {
                    String vie = data.substring(separate + 1, secondSeparate);
                    String longvie = data.substring(secondSeparate + 1).replace("\\n", "\n");
                    dictionary.add(new Word(eng, vie, longvie),isFavoriteFile);
                } else {
                    String vie = data.substring(separate + 1);
                    dictionary.add(new Word(eng, vie),isFavoriteFile);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred. File may not be found.");
            e.printStackTrace();
        }
    }

    public String dictionaryLookup(String wordToFind, boolean engToVie) {
        for (Word word : dictionary.getListOfWord()) {
            if (wordToFind.equalsIgnoreCase(word.getWord_target()) && engToVie) {
                return word.getWord_explain();
            }
            if (wordToFind.equalsIgnoreCase(word.getWord_explain()) && !engToVie) {
                return word.getWord_target();
            }
        }
        return "*** Error: No explanation found for " + wordToFind + " ***";
    }

    public Word dictionaryLookupLongExplain(String eng) {
        for (Word word : dictionary.getListOfWord()) {
            if (eng.equalsIgnoreCase(word.getWord_target())) {
                return word;
            }
        }
        return new Word("eng", null, null);
    }

    public boolean autoAddNewWord(Word newWord, boolean toFavorite) {
        return dictionary.add(newWord,toFavorite);
    }

    public boolean autoDeleteAWord(Word oldWord, boolean fromFavorite) {
        if(fromFavorite) return dictionary.removeFromFav(oldWord);
        else return dictionary.removeFromList(oldWord);
    }

    public boolean removeLongExplanation(String eng) {
        for (Word word: dictionary.getListOfWord()) {
            if (word.getWord_target().equalsIgnoreCase(eng)) {
                dictionary.removeFromList(word);
                return dictionary.add(new Word(word.getWord_target(), word.getWord_explain()), false);
            }
        }
        return false;
    }

    public boolean dictionaryExportToFile(String fileToExport, boolean exportToFavorite) {
        try {
            File file = new File("temp.txt");
            if (!file.createNewFile() && file.delete()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating a new temporary file.");
            e.printStackTrace();
            return false;
        }

        try {
            FileWriter writeNewList = new FileWriter("temp.txt");
            if(!exportToFavorite) {
                for (Word w: dictionary.getListOfWord()) {
                    writeNewList.write(w.getWord_target().concat("\t"));
                    writeNewList.write(w.getWord_explain());
                    if(w.getLong_explain() != null && w.getLong_explain().length() > 0) {
                        writeNewList.write("\t");
                        writeNewList.write(w.getLong_explain().replace("\n", "\\n"));
                    }
                    writeNewList.write("\n");
                }
            } else {
                for (Word w: dictionary.getMyFav()) {
                    writeNewList.write(w.getWord_target().concat("\t"));
                    writeNewList.write(w.getWord_explain());
                    if(w.getLong_explain() != null && w.getLong_explain().length() > 0) {
                        writeNewList.write("\t");
                        writeNewList.write(w.getLong_explain().replace("\n", "\\n"));
                    }
                    writeNewList.write("\n");
                }
            }
            writeNewList.close();
            File oldWordSource = new File(fileToExport);
            if(oldWordSource.delete()) {
                File newWordSource = new File("temp.txt");
                System.out.println("Trying to replace...");
                return newWordSource.renameTo(oldWordSource);
            }
            return false;
        } catch (Exception e) {
            System.out.println("An error occurred while replacing the old file with the new file.");
            e.printStackTrace();
            return false;
        }
    }
}
