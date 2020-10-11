import CmdVer1.DictionaryCommandline;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("Progress: 3/6");
        DictionaryCommandline DCML = new DictionaryCommandline();
        DCML.getDictionaryManagement().insertFromFile();
        System.out.println("0: Show all words contain the next 1 String scanned");
        System.out.println("1: Show all words");
        System.out.println("2: Search words");
        System.out.println("3: Update words");
        System.out.println("4: Delete words");
        System.out.println("5: Add words");
        Scanner inp = new Scanner(System.in);
        while (inp.hasNextLine()) {
            String choose = "";
            int choice = 0, end;
            do {
                choose =  inp.nextLine();
                end = choose.length();
                if(end > 0) {
                    choice = (int)choose.charAt(0) - 48;
                }
            } while (choice < 0 || choice > 5 || end != 1);

            if (choice == 0) {
                System.out.println("Insert 1 String to find:");
                String find = inp.nextLine();
                DCML.dictionarySearcher(find);
            } else if (choice == 1) {
                DCML.showAllWords();
            } else if (choice == 2) {
                DCML.getDictionaryManagement().dictionaryLookup();
            } else if (choice == 3) {
                DCML.getDictionaryManagement().replaceAWord();
                DCML.getDictionaryManagement().dictionaryExportToFile();
            } else if (choice == 4) {
                DCML.getDictionaryManagement().deleteAWord();
                DCML.getDictionaryManagement().dictionaryExportToFile();
            } else {
                DCML.getDictionaryManagement().addNewWord();
                DCML.getDictionaryManagement().dictionaryExportToFile();
            }
            System.out.println("0: Show all words contain the next String scanned");
            System.out.println("1: Show all words");
            System.out.println("2: Search words");
            System.out.println("3: Update words");
            System.out.println("4: Delete words");
            System.out.println("5: Add words");
        }
    }

}
