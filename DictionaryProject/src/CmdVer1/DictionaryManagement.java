package CmdVer1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

interface DictionaryMng0 {
    Dictionary getDictionary();
    void insertFromCommandline();
    void insertFromFile();
    void dictionaryLookup();
    void addNewWord();
    void replaceAWord();
    void deleteAWord();
    boolean dictionaryExportToFile();
}

public class DictionaryManagement implements DictionaryMng0 {
    private final Dictionary dictionary;

    DictionaryManagement() {
        this.dictionary = new Dictionary();
    }

/*
    DictionaryManagement(DictionaryManagement dictionaryManagement) {
        this.dictionary = dictionaryManagement.dictionary;
    }
*/

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void insertFromCommandline() {
        Scanner inp = new Scanner(System.in);
        int NumOfWord = inp.nextInt();
        inp.nextLine();
        for (int i = 0; i < NumOfWord; i++) {
            //print
            System.out.println("Insert English Word(s) and explanation here, each in 1 line:");
            String eng = inp.nextLine().toLowerCase();
            String vie = inp.nextLine();
            Word temp = new Word(eng, vie);
            dictionary.add(temp);
        }
    }

    public void insertFromFile() {
        try {
            File wordSource = new File("dictionaries.txt");
            Scanner myReader = new Scanner(wordSource);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                int separate = data.indexOf("\t");
                String eng = data.substring(0, separate).toLowerCase();
                String vie = data.substring(separate + 1);
                Word temp = new Word(eng, vie);
                dictionary.add(temp);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred. File may not be found.");
            e.printStackTrace();
        }
    }

    public void dictionaryLookup() {
        try {
            System.out.println("Insert a number of English words you want to search, then Enter.");
            Scanner inp = new Scanner(System.in);
            int numOfEng = inp.nextInt();
            for (int i = 0; i < numOfEng; ++i) {
                boolean continued = false;
                System.out.println("Insert English Word(s) here.");
                String wordTarget = inp.nextLine();
                for (Word word : dictionary.getListOfWord()) {
                    if (wordTarget.equalsIgnoreCase(word.getWord_target())) {
                        System.out.println(word.getWord_explain());
                        continued = true;
                        break;
                    } // to be continued
                }
                if(continued) continue;
                System.out.println("Error: Can not be found in dictionaries.txt, or the file has never been read");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addNewWord(){
        try {
            System.out.println("Insert a number of English words you want to add, then Enter.");
            Scanner inp = new Scanner(System.in);
            int numOfEng = inp.nextInt();
            for (int i = 0; i < numOfEng; ++i) {
                System.out.println("Insert English word you want to add, then Enter.");
                String newEng = inp.nextLine();
                System.out.println("Insert Explain here, then Enter.");
                String newVie = inp.nextLine();
                boolean answer = dictionary.add(new Word(newEng, newVie));
                if (answer) System.out.println("Added successfully.");
                else System.out.println("The word is already existed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void replaceAWord() {
        try {
            System.out.println("Insert a number of English words you want to change, then Enter.");
            Scanner inp = new Scanner(System.in);
            int numOfEng = inp.nextInt();
            for (int i = 0; i < numOfEng; ++i) {
                boolean continued = false;
                //print
                System.out.println("Insert English word you want to change, then Enter.");
                String oldEng = inp.nextLine();
                for (Word s : dictionary.getListOfWord()) {
                    if (oldEng.equalsIgnoreCase(s.getWord_target())) {
                        //print
                        System.out.println("New English word here, then Enter.");
                        String newEng = inp.nextLine();
                        System.out.println("New Explain here, then Enter.");
                        String newVie = inp.nextLine();
                        s.setWord_target(newEng.toLowerCase());
                        s.setWord_explain(newVie.toLowerCase());
                        //print
                        System.out.println("Replace successfully.");
                        continued = true;
                        break;
                    }
                }
                if(continued) continue;
                //print
                System.out.println("Replace failed. The word you want to change doesn't exist.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteAWord() {
        try {
            System.out.println("Insert a number of English words you want to delete, then Enter.");
            Scanner inp = new Scanner(System.in);
            int numOfEng = inp.nextInt();
            for (int i = 0; i < numOfEng; ++i) {
                //print
                boolean continued = false;
                System.out.println("Insert English word you want to delete, then Enter.");
                String wordToDelete = inp.nextLine();
                for (Word s : dictionary.getListOfWord()) {
                    if (wordToDelete.equalsIgnoreCase(s.getWord_target())) {
                        if (dictionary.remove(s)) {
                            System.out.println("Delete successfully.");
                            continued = true;
                            break;
                        } else {
                            System.out.println("Error: Unable to delete.");
                            continued = true;
                            break;
                        }
                    }
                }
                if(continued) continue;
                //print
                System.out.println("Delete failed. It's already not existed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean dictionaryExportToFile() {
        try {
            File file = new File("temp.txt");
            if (!(!file.createNewFile() && file.delete() && file.createNewFile())) return false;
        } catch (IOException e) {
            System.out.println("An error occurred while creating a new temporary file.");
            e.printStackTrace();
            return false;
        }

        try {
            FileWriter writeNewList = new FileWriter("temp.txt");
            writeNewList.write(dictionary.getStringOfWord());
            writeNewList.close();
            File oldWordSource = new File("dictionaries.txt");
            if(oldWordSource.delete()) {
                File newWordSource = new File("temp.txt");
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