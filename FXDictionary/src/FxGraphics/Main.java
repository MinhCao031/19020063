package FxGraphics;

import RawDictionary.*;
import com.darkprograms.speech.synthesiser.SynthesiserV2;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    Stage primaryStage;
    Scene mainMenu;
    BorderPane originalLayout;
    Button quitApp, dictionarySearchButton, translateButton, translateOnlButton, myFavButton, loadData;

    public static final String fileName = "tabNeutralVer.txt";
    public static final String favfile = "tabFavorite.txt";
    static SynthesiserV2 speaker = new SynthesiserV2("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");
    static Word temporaryWord = new Word("", "");
    static Label findWord = new Label("Search in the small field");
    static final boolean[] translateSucceeded = {false};
    Dictionary searchResult;
    Font font20 = new Font("Ink Free", 20);
    Font font15 = new Font("System Regular", 15);
    Boolean dataLoaded = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {
        DictionaryCommandline dictionaryCommandline = new DictionaryCommandline();
        primaryStage = window;
        primaryStage.setTitle("My dictionary by MinhCao");

        Label menuText = new Label("My dictionary");
        menuText.setFont(new Font("Ink Free", 36));
        loadData = new Button("Load Offline Data");
        loadData.setFont(font20);
        dictionarySearchButton = new Button("Dictionary Searcher");
        dictionarySearchButton.setFont(font20);
        translateButton = new Button("Translate offline");
        translateButton.setFont(font20);
        translateOnlButton = new Button("Translate online");
        translateOnlButton.setFont(font20);
        myFavButton = new Button("See my favorite words");
        myFavButton.setFont(font20);
        quitApp = new Button("Exit Application");
        quitApp.setFont(font20);

        loadData.setOnAction(event -> {
            if (!dataLoaded) {
                CustomControl.showBox("Please wait","The application may get laggy for a while...");
                dataLoaded = true;
                dictionaryCommandline.getDictionaryManagement().insertFromFile(Main.fileName, false);
                dictionaryCommandline.getDictionaryManagement().insertFromFile(Main.favfile, true);
                CustomControl.showBox("Successful", "Offline data has been loaded!");
                loadData.setText("Offline data loaded");
            } else CustomControl.showBox("Hmm...", "You don't need to click here anymore!");
        });
        dictionarySearchButton.setOnAction(event -> {
            if (!dataLoaded) CustomControl.showBox("Offline data is currently empty",
                    "Please load Offline data first!");
            else mainMenu.setRoot(buildDictionarySearchPane(dictionaryCommandline));
        });
        myFavButton.setOnAction(event -> {
            if (!dataLoaded) CustomControl.showBox("Offline data is currently empty",
                    "Please load Offline data first!");
            else mainMenu.setRoot(buildMyFavHere(dictionaryCommandline));
        });
        translateButton.setOnAction(event -> {
            if (!dataLoaded) CustomControl.showBox("Offline data is currently empty",
                    "Please load Offline data first!");
            else mainMenu.setRoot(buildTranslateOffPane(dictionaryCommandline));
        });
        translateOnlButton.setOnAction(event -> mainMenu.setRoot(buildTranslateOnlPane(dictionaryCommandline)));
        quitApp.setOnAction(event -> {
            if (dataLoaded) {
                System.out.println(dictionaryCommandline.getDictionaryManagement().dictionaryExportToFile(fileName, false));
                System.out.println(dictionaryCommandline.getDictionaryManagement().dictionaryExportToFile(favfile, true));
            }
            primaryStage.close();
        });

        VBox colOfButton = new VBox(30);
        colOfButton.getChildren().addAll(menuText, loadData, dictionarySearchButton,
                translateButton, translateOnlButton, myFavButton, quitApp);
        colOfButton.setAlignment(Pos.CENTER);

        originalLayout = new BorderPane();
        originalLayout.setCenter(colOfButton);

        mainMenu = new Scene(originalLayout, WIDTH, HEIGHT);
        loadData.setPrefWidth(mainMenu.getWidth() / 2.4);
        dictionarySearchButton.setPrefWidth(mainMenu.getWidth() / 2.4);
        translateButton.setPrefWidth(mainMenu.getWidth() / 2.4);
        translateOnlButton.setPrefWidth(mainMenu.getWidth() / 2.4);
        myFavButton.setPrefWidth(mainMenu.getWidth() / 2.4);
        quitApp.setPrefWidth(mainMenu.getWidth() / 3);

        primaryStage.setScene(mainMenu);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public BorderPane buildDictionarySearchPane(DictionaryCommandline dictionaryCommandline) {
        VBox filters = new VBox(5);
        CheckBox filter1 = new CheckBox("Start with inserted word only");
        CheckBox filter2 = new CheckBox("Contain the exact inserted word");
        filters.getChildren().addAll(filter1, filter2);

        HBox buttons = new HBox(5);
        Button back = new Button("Back to menu");
        Button remove = new Button("Remove from offline data");
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(remove, back);

        HBox hBox = new HBox(20);
        hBox.getChildren().addAll(filters, buttons);

        TextField search = new TextField();
        search.setText("Insert a word here, or leave blank to see all words.");

        TableView<Word> searchResultView = new TableView<>();

        search.setOnAction(event -> {
            String toFind = search.getText();
            if (!filter2.isSelected())
                searchResult = dictionaryCommandline.dictionarySearcher(toFind, filter1.isSelected());
            else searchResult = dictionaryCommandline.dictionarySearchExact(toFind, filter1.isSelected());
            System.out.println(toFind);
            if (toFind.length() == 0) {
                //System.out.println("TextField Empty");
                searchResultView.setItems(CustomControl.getWordList(dictionaryCommandline.getDictionaryManagement().getDictionary(), false));
            } else {
                //System.out.println("TextField not Empty");
                searchResultView.setItems(CustomControl.getWordList(searchResult, false));
            }
        });
        remove.setOnAction(event -> {
            ObservableList<Word> wordSelected, allWords;
            wordSelected = searchResultView.getSelectionModel().getSelectedItems();
            allWords = searchResultView.getItems();
            for(Word s: wordSelected) {
                System.out.println(s.getWord_target() + " has been deleted.");
                dictionaryCommandline.getDictionaryManagement().autoDeleteAWord(s,false);
            }
            wordSelected.forEach(allWords::remove);
        });
        back.setOnAction(event -> mainMenu.setRoot(originalLayout));

        TableColumn<Word, String> engCol = new TableColumn<>("English");
        engCol.setMinWidth(290); engCol.setPrefWidth(900);
        engCol.setCellValueFactory(new PropertyValueFactory<>("word_target"));
        TableColumn<Word, String> vieCol = new TableColumn<>("Vietnamese");
        vieCol.setMinWidth(290); vieCol.setPrefWidth(900);
        vieCol.setCellValueFactory(new PropertyValueFactory<>("word_explain"));
        searchResultView.getColumns().add(engCol);
        searchResultView.getColumns().add(vieCol);
        searchResultView.setMinWidth(600);
        searchResultView.setMaxWidth(1810);

        BorderPane layout1 = new BorderPane();
        layout1.setTop(search);
        layout1.setCenter(searchResultView);
        layout1.setBottom(hBox);

        return layout1;
    }

    public BorderPane buildTranslateOffPane(DictionaryCommandline dictionaryCommandline) {
        BorderPane layout1 = new BorderPane();
        HBox center = new HBox(10);
        VBox leftColumn = new VBox(10);
        AutoCompleteTextField input = new AutoCompleteTextField(dictionaryCommandline.getDictionaryManagement());
        input.setMinWidth(295);
        input.setMaxWidth(500);
        input.setPrefHeight(50);
        input.setFont(font15);
        input.setStyle("-fx-text-inner-color: Blue;");

        VBox buttons = new VBox(20);
        Button speak = new Button("Pronounce");
        Button addToFav = new Button("Add to my Favorite");
        Button addToDic = new Button("Add to Dictionary");
        Button changeWord = new Button("Change or Remove Word");
        Button back = new Button("Back to memu");
        speak.setFont(font20);
        addToFav.setFont(font20);
        addToDic.setFont(font20);
        changeWord.setFont(font20);
        back.setFont(font20);
        buttons.getChildren().addAll(speak, addToFav, addToDic, changeWord, back);
        buttons.setAlignment(Pos.CENTER);

        leftColumn.getChildren().addAll(input, buttons);
        leftColumn.setMinWidth(295);
        leftColumn.setMaxWidth(500);

        TextArea explain = new TextArea();
        explain.setFont(font15);
        explain.textProperty().bind(findWord.textProperty());
        explain.setMinWidth(295);
        explain.setMaxWidth(500);
        explain.setWrapText(true);

        center.getChildren().addAll(leftColumn, explain);
        center.setAlignment(Pos.CENTER);
        layout1.setCenter(center);
        // Detailed
        translateSucceeded[0] = false;
        speak.setMinWidth(250);
        addToFav.setMinWidth(250);
        addToDic.setMinWidth(250);
        changeWord.setMinWidth(250);
        back.setMinWidth(200);

        speak.setOnAction(event -> CustomControl.speak(input.getText()));
        addToFav.setOnAction(event -> {
            if (!translateSucceeded[0] || input.getText().length() == 0) {
                CustomControl.showBox("Hmm...", "You should translate a word first.");
            } else {
                mainMenu.setRoot(confirmAddWord(dictionaryCommandline, layout1, input.getText(), explain.getText(), true));
            }
        });
        addToDic.setOnAction(event -> mainMenu.setRoot(manuallyAddWord(dictionaryCommandline, layout1)));
        changeWord.setOnAction(event -> {
            if (!translateSucceeded[0] || input.getText().length() == 0) {
                CustomControl.showBox("Hmm...", "You should translate a word first.");
            } else {
                mainMenu.setRoot(confirmUpdateWord(dictionaryCommandline, layout1, input.getText(), false));
            }
        });
        back.setOnAction(event -> mainMenu.setRoot(originalLayout));

        return layout1;
    }

    public BorderPane buildTranslateOnlPane(DictionaryCommandline dictionaryCommandline) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setMinSize(600, 600);
        grid.setMaxSize(700, 600);
        grid.setVgap(10);
        grid.setHgap(10);
        VBox buttons = new VBox(20);
        buttons.setAlignment(Pos.CENTER);

        Label eng = new Label("English:");
        Label vie = new Label("Vietnamese:");
        Label instruction = new Label("Translate online here\n(Require internet connection)");
        TextField engInput = new TextField();
        TextField vieOutput = new TextField();
        Button speakeng = new Button("Speak");
        Button speakvie = new Button("Nói");
        Button addToFav = new Button("Add to my Favorite");
        Button addToDic = new Button("Add to Dictionary");
        Button back = new Button("Back");

        buttons.getChildren().addAll(addToDic, addToFav, back);
        grid.getChildren().addAll(instruction, eng, vie, engInput, vieOutput, speakeng, speakvie, buttons);
        BorderPane layout1 = new BorderPane();
        layout1.setCenter(grid);
        //detailed
        GridPane.setConstraints(instruction, 1, 0);
        GridPane.setConstraints(engInput, 1, 1);
        GridPane.setConstraints(vieOutput, 1, 2);
        GridPane.setConstraints(eng, 0, 1);
        GridPane.setConstraints(vie, 0, 2);
        GridPane.setConstraints(buttons, 1, 3);
        GridPane.setConstraints(speakeng, 2, 1);
        GridPane.setConstraints(speakvie, 2, 2);

        instruction.setPrefSize(400, 80);
        engInput.setPrefColumnCount(15);
        vieOutput.setPrefColumnCount(15);
        eng.setPrefSize(150, 30);
        vie.setPrefSize(150, 30);
        speakvie.setPrefSize(100,30);
        speakeng.setPrefSize(100,30);
        addToDic.setPrefSize(250, 30);
        addToFav.setPrefSize(250, 30);
        back.setPrefSize(250, 30);

        instruction.setFont(font20);
        eng.setFont(font20);
        vie.setFont(font20);
        addToDic.setFont(font20);
        addToFav.setFont(font20);
        back.setFont(font20);
        engInput.setFont(font15);
        vieOutput.setFont(font15);
        engInput.setStyle("-fx-text-inner-color: Blue;");
        vieOutput.setStyle("-fx-text-inner-color: Green;");
        engInput.setPromptText("Insert here then Enter to translate to Vietnamese");
        vieOutput.setPromptText("Insert here then Enter to translate to English");

        translateSucceeded[0] = false;
        final String[] temp = {"", ""};
        engInput.setOnAction(event -> {
            translateSucceeded[0] = false;
            if (engInput.getText().length() == 0 || engInput.getText().indexOf("***") == 0) {
                vieOutput.setText("*** You haven't typed any letters ***");
            } else {
                vieOutput.setText(CustomControl.translate("en", "vi", engInput.getText()));
                if (vieOutput.getText().charAt(0) != '*' && !vieOutput.getText().equals(engInput.getText())) {
                    temp[0] = engInput.getText();
                    temp[1] = vieOutput.getText();
                    translateSucceeded[0] = true;
                }
            }
        });
        vieOutput.setOnAction(event -> {
            translateSucceeded[0] = false;
            if (vieOutput.getText().length() == 0 || vieOutput.getText().indexOf("***") == 0) {
                engInput.setText("*** You haven't typed any letters ***");
            } else {
                engInput.setText(CustomControl.translate("vi", "en", vieOutput.getText()));
                if (vieOutput.getText().charAt(0) != '*' && !vieOutput.getText().equals(engInput.getText())) {
                    temp[0] = engInput.getText();
                    temp[1] = vieOutput.getText();
                    translateSucceeded[0] = true;
                }
            }
        });
        speakeng.setOnAction(event -> CustomControl.speak(engInput.getText()));
        speakvie.setOnAction(event -> CustomControl.speak(vieOutput.getText()));
        addToDic.setOnAction(event -> {
            if (!translateSucceeded[0] || temp[0].length() * temp[1].length() == 0) {
                CustomControl.showBox("Hmm...", "You should translate a word first.");
            } else mainMenu.setRoot(confirmAddWord(dictionaryCommandline, layout1, temp[0], temp[1], false));
        });
        addToFav.setOnAction(event -> {
            if (!translateSucceeded[0] || temp[0].length() * temp[1].length() == 0) {
                CustomControl.showBox("Hmm...", "You should translate a word first.");
            } else mainMenu.setRoot(confirmAddWord(dictionaryCommandline, layout1, temp[0], temp[1], true));
        });
        back.setOnAction(event -> mainMenu.setRoot(originalLayout));

        return layout1;
    }

    public BorderPane buildMyFavHere(DictionaryCommandline dictionaryCommandline) {
        BorderPane layout1 = new BorderPane();

        TableColumn<Word, String> engFavCol = new TableColumn<>("English");
        engFavCol.setMinWidth(293);
        engFavCol.setCellValueFactory(new PropertyValueFactory<>("word_target"));
        TableColumn<Word, String> vieFavCol = new TableColumn<>("Vietnamese");
        vieFavCol.setMinWidth(292);
        vieFavCol.setCellValueFactory(new PropertyValueFactory<>("word_explain"));
        TableView<Word> tableFavView = new TableView<>();
        ObservableList<Word> temp = CustomControl.getWordList(dictionaryCommandline.getDictionaryManagement().getDictionary(),true);
        tableFavView.setItems(temp);
        tableFavView.getColumns().add(engFavCol);
        tableFavView.getColumns().add(vieFavCol);

        HBox bottom = new HBox(10);
        Button remove = new Button("Remove selected word from my favorite word.");
        Button back = new Button("Back to menu");
        bottom.getChildren().addAll(remove, back);

        back.setFont(font20);
        remove.setFont(font20);
        remove.setOnAction(event -> {
            ObservableList<Word> wordSelected, allWords;
            wordSelected = tableFavView.getSelectionModel().getSelectedItems();
            allWords = tableFavView.getItems();
            wordSelected.forEach(allWords::remove);
            for(Word s: wordSelected) {
                System.out.println(dictionaryCommandline.getDictionaryManagement().autoDeleteAWord(s,false));
            }
        });
        back.setOnAction(event -> mainMenu.setRoot(originalLayout));

        layout1.setCenter(tableFavView);
        layout1.setBottom(bottom);
        return layout1;
    }

    public BorderPane manuallyAddWord(DictionaryCommandline dictionaryCommandline, BorderPane whereToBack) {
        BorderPane layout2 = new BorderPane();
        VBox center = new VBox(20);
        StackPane bottom = new StackPane();
        Label askToConfirm = new Label();
        TextField engField = new TextField();
        TextField vieField = new TextField();
        TextField longField = new TextField();
        Button confirm = new Button("Confirm Change");
        Button back = new Button("Go back");
        CheckBox addTofav = new CheckBox("Also add to my favorite words.");

        engField.setPrefColumnCount(30);
        vieField.setPrefColumnCount(30);
        longField.setPrefColumnCount(30);
        askToConfirm.setPrefSize(600, 50);
        confirm.setPrefSize(300, 30);
        back.setPrefSize(300, 30);

        engField.setPromptText("New English Word here...");
        vieField.setPromptText("New Short Explanation here...");
        longField.setPromptText("New Long Explanation here. Use \"\\n\" to separate into multiple lines.");
        askToConfirm.setFont(font20); addTofav.setFont(font20);
        confirm.setFont(font20); back.setFont(font20);

        askToConfirm.setText("You want to add a new word? Here.\n");
        askToConfirm.setAlignment(Pos.CENTER);
        confirm.setOnAction(event -> {
            if (engField.getText().equals("") || (vieField.getText().equals("") && longField.getText().equals("")) ) {
                CustomControl.showBox("Failed", "Not enough information. Please try again.");
            } else {
                Word update = new Word(engField.getText(), vieField.getText(),
                    longField.getText().replace("\\n", "\n"));
                CustomControl.showBox("Success", "This word has been added to your offline data"
                        + (addTofav.isSelected()? " and your favorite words: \n": ":\n")
                        + "    New English:\n" + update.getWord_target()
                        + "    New Short Explanation:\n" + update.getWord_explain()
                        + "    New Long Explanation:\n" + update.getLong_explain());
                dictionaryCommandline.getDictionaryManagement().autoAddNewWord(update, false);
                if(addTofav.isSelected()) {
                    dictionaryCommandline.getDictionaryManagement().autoAddNewWord(update, true);
                    dictionaryCommandline.getDictionaryManagement().dictionaryExportToFile(favfile,true);
                }
            }
            dictionaryCommandline.getDictionaryManagement().autoDeleteAWord(temporaryWord, addTofav.isSelected());
            mainMenu.setRoot(whereToBack);
        });
        back.setOnAction(event -> mainMenu.setRoot(whereToBack));

        center.getChildren().addAll(askToConfirm, engField, vieField, longField, confirm, back);
        center.setAlignment(Pos.CENTER);
        bottom.getChildren().add(addTofav);
        layout2.setCenter(center);
        layout2.setBottom(bottom);

        return layout2;
    }

    public BorderPane confirmAddWord(DictionaryCommandline dictionaryCommandline, BorderPane whereToBack, String eng, String vie, Boolean toFavorite) {
        BorderPane layout2 = new BorderPane();
        VBox center = new VBox(20);
        Label askToConfirm = new Label();
        TextField longInput = new TextField();
        Button confirm = new Button("Yes");
        Button back = new Button("No");

        askToConfirm.setPrefSize(600, 150);
        confirm.setPrefSize(300, 30);
        back.setPrefSize(300, 30);
        longInput.setPrefColumnCount(50);
        longInput.setPromptText("Insert here to add as this word's long explanation...");

        askToConfirm.setFont(font15);
        confirm.setFont(font20);
        back.setFont(font20);

        center.getChildren().addAll(askToConfirm, longInput, confirm, back);
        center.setAlignment(Pos.CENTER);
        layout2.setCenter(center);
        //detailed
        askToConfirm.setText("Are you sure you want to add this word "
                + (toFavorite ? "as your favorite word?" : "to your offline data?")
                + "\nClick Yes to confirm, or No to cancel and go back."
                + "\n    Curent English: \n" + eng + "\n    Curent Explanation:\n" + vie);
        askToConfirm.setAlignment(Pos.CENTER);

        final String longExplain = (longInput.getText().length() == 0? null: longInput.getText().replace("\\n", "\n"));
        confirm.setOnAction(event -> {
            if (!toFavorite) {
                CustomControl.showBox("Success", "The word \"" + eng + "\" has been added to your offline data.");
                dictionaryCommandline.getDictionaryManagement().autoAddNewWord(new Word(eng, vie, longExplain), false);
            } else {
                CustomControl.showBox("Success", "The word \"" + eng + "\" has been added to your favorite word.");
                dictionaryCommandline.getDictionaryManagement().autoAddNewWord(new Word(eng, vie, longExplain), true);
                dictionaryCommandline.getDictionaryManagement().dictionaryExportToFile(favfile, true);
            }
            mainMenu.setRoot(whereToBack);
        });
        back.setOnAction(event -> mainMenu.setRoot(whereToBack));

        return layout2;
    }

    public BorderPane confirmUpdateWord(DictionaryCommandline dictionaryCommandline, BorderPane whereToBack, String eng, Boolean favoriteChange) {
        BorderPane layout2 = new BorderPane();
        VBox center = new VBox(20);
        Label askToConfirm = new Label();
        TextField engField = new TextField(eng);
        TextField vieField = new TextField();
        TextField longField = new TextField();
        Button confirm = new Button("Confirm Change");
        Button back = new Button("Go back");

        engField.setPrefColumnCount(30);
        vieField.setPrefColumnCount(30);
        longField.setPrefColumnCount(30);
        askToConfirm.setPrefSize(600, 110);
        confirm.setPrefSize(300, 30);
        back.setPrefSize(300, 30);

        engField.setPromptText("New English Word here. Type \"@keep\" if you don't wanna change");
        vieField.setPromptText("New Short Explanation here. Type \"@keep\" if you don't wanna change");
        longField.setPromptText("New Long Explanation here. Type \"@keep\" if you don't wanna change");
        askToConfirm.setFont(font15); confirm.setFont(font20); back.setFont(font20);

        center.getChildren().addAll(askToConfirm, engField, vieField, longField, confirm, back);
        center.setAlignment(Pos.CENTER);
        layout2.setCenter(center);

        askToConfirm.setText("You can change this word's meaning below."
                + "\nLeave all fields blank to remove this word from offline data."
                + "\nClick Confirm Change to confirm, or Cancel to cancel and go back.\n"
                + "\nCurent English Word: " + eng);
        askToConfirm.setAlignment(Pos.CENTER);
        confirm.setOnAction(event -> {
            if (engField.getText().equals("") && vieField.getText().equals("") && longField.getText().equals("")) {
                CustomControl.showBox("Success", "The word \"" + eng + "\" has been removed from your offline data.");
            } else {
                Word update = new Word(
                    (engField.getText().equals("@keep")? Main.temporaryWord.getWord_target(): engField.getText()),
                    (vieField.getText().equals("@keep")? Main.temporaryWord.getWord_explain(): vieField.getText()),
                   (longField.getText().equals("@keep")? Main.temporaryWord.getLong_explain(): longField.getText().replace("\\n","\n")));
                CustomControl.showBox("Success", "The word \"" + eng + "\" has been changed to:"
                    + "    New English:\n" + update.getWord_target()
                    + "    New Short Explanation:\n" + update.getWord_explain()
                    + "    New Long Explanation:\n" + update.getLong_explain());
                dictionaryCommandline.getDictionaryManagement().autoAddNewWord(update, favoriteChange);
            }
            dictionaryCommandline.getDictionaryManagement().autoDeleteAWord(temporaryWord, favoriteChange);
            mainMenu.setRoot(whereToBack);
        });
        back.setOnAction(event -> mainMenu.setRoot(whereToBack));

        return layout2;
    }
}

