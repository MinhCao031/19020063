package FxGraphics;

import RawDictionary.Dictionary;
import RawDictionary.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

public class CustomControl {
    public static String translate(String langFrom, String langTo, String wordToFind) {
        try {
            String urlStr = "https://script.google.com/macros/s/AKfycbxH5cy35LCnmYi3sZjLjUNNh37HLmbH1dThLr2AITL3Te0bTFW8/exec"
                    + "?q=" + URLEncoder.encode(wordToFind, "UTF-8") + "&target=" + langTo + "&source=" + langFrom;
            URL url = new URL(urlStr);
            StringBuilder response = new StringBuilder();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            return "*** " + e.getMessage() + " ***";
        }
    }

    public static void showBox(String title, String message) {
        Stage tempWin = new Stage();

        tempWin.initModality(Modality.WINDOW_MODAL);
        tempWin.setTitle(title);
        tempWin.setMinWidth(200);
        tempWin.setMinHeight(100);

        Label label = new Label(message);
        label.setFont(new Font("Ink Free",20));
        VBox info = new VBox(20);

        Button ok = new Button("   Ok   ");
        ok.setFont(new Font(15));
        ok.setOnAction(event -> tempWin.close());
        info.setAlignment(Pos.CENTER);
        info.getChildren().addAll(label, ok);

        tempWin.setScene(new Scene(info, 600, 150));
        tempWin.showAndWait();
    }

    public static ObservableList<Word> getWordList(Dictionary dictionary, boolean isFavoriteList) {
        if (dictionary == null) return null;
        ObservableList<Word> ans = FXCollections.observableArrayList();
        if (isFavoriteList) {
            //System.out.println(dictionary.getFavSize());
            ans.addAll(dictionary.getMyFav());
            return ans;
        } else ans.addAll(dictionary.getListOfWord());
        return ans;
    }

    public static void speak(String text) {
        //Create a new Thread because JLayer is running on the current Thread and will make the application to lag
        Thread thread = new Thread(() -> {
            try {
                //Create a JLayer instance
                AdvancedPlayer player = new AdvancedPlayer(Main.speaker.getMP3Data(text));
                player.play();
            } catch (IOException | JavaLayerException e) {
                e.printStackTrace();
            }
        });
        //We don't want the application to terminate before this Thread terminates
        thread.setDaemon(false);
        //Start the Thread
        thread.start();
    }

}
