package CmdVer1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import static CmdVer1.Dictionary.listOfWord;

public class DictionaryManagement {

    public static void insertFromCommandline() {
        Scanner inp = new Scanner(System.in);
        int NumOfWord = inp.nextInt();
        String trash = inp.nextLine();
        for (int i = 0; i < NumOfWord; i++) {
            System.out.println("Insert English Word(s) and explanation here, each in 1 line:");
            String eng = inp.nextLine();
            String vie = inp.nextLine();
            Word temp = new Word(eng, vie);
            listOfWord.add(temp);
        }
    }

    public static void insertFromFile() {
        try {
            File wordSource = new File("dictionaries.txt");
            Scanner myReader = new Scanner(wordSource);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                int separate = data.indexOf("\t");
                String eng = data.substring(0, separate);
                String vie = data.substring(separate + 1);
                Word temp = new Word(eng, vie);
                listOfWord.add(temp);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred. File may not be found.");
            e.printStackTrace();
        }
    }

    public static void dictionaryLookup() {
        System.out.println("Insert English Word(s) here.");
        Scanner inp = new Scanner(System.in);
        String wordTarget = inp.nextLine();
        int currentWords = listOfWord.size();
        for (int i = 0; i < currentWords; i++) {
            if (wordTarget.equalsIgnoreCase(listOfWord.get(i).getWord_target())) {
                System.out.println(listOfWord.get(i).getWord_explain());
                return;
            }
        }
        System.out.println("Error: Can not be found in dictionaries.txt, or the file has never been read");
    }
}