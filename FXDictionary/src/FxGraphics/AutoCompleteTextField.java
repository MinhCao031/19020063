package FxGraphics;

import RawDictionary.DictionaryManagement;
import RawDictionary.Word;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class AutoCompleteTextField extends TextField {
    /**
     * The existing autocomplete entries.
     */
    private final SortedSet<String> entries;
    /**
     * The popup used to select an entry.
     */
    private final ContextMenu entriesPopup;

    /**
     * Construct a new AutoCompleteTextField.
     */
    public AutoCompleteTextField(DictionaryManagement dictionaryManagement) {
        super();
        entries = new TreeSet<>();
        for (Word word: dictionaryManagement.getDictionary().getListOfWord()) {
            entries.add(word.getWord_target().toLowerCase());
        }
        entriesPopup = new ContextMenu();
        textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                Main.translateSucceeded[0] = false;
                if (getText().length() == 0) {
                    entriesPopup.hide();
                } else {
                    //searchResult.addAll(entries.subSet(getText(), getText() + Character.MAX_VALUE));
                    LinkedList<String> searchResult = new LinkedList<>(entries.subSet(getText(), getText() + Character.MAX_VALUE));
                    if (entries.size() > 0) {
                        populatePopup(searchResult, dictionaryManagement);
                        if (!entriesPopup.isShowing()) {
                            entriesPopup.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
                        }
                    } else {
                        entriesPopup.hide();
                    }
                }
            }
        });

        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                //entriesPopup.hide();
            }
        });

    }

    /**
     * Get the existing set of autocomplete entries.
     *
     * @return The existing autocomplete entries.
     */
    public SortedSet<String> getEntries() {
        return entries;
    }

    /**
     * Populate the entry set with the given search results.  Display is limited to a few entries, for performance.
     *
     * @param searchResult The set of matching strings.
     */
    private void populatePopup(List<String> searchResult, DictionaryManagement dictionaryManagement) {
        List<CustomMenuItem> menuItems = new LinkedList<>();
        // If you'd like more entries, modify this line.
        int maxEntries = 28;
        int count = Math.min(searchResult.size(), maxEntries);
        for (int i = 0; i < count; ++i) {
            final String result = searchResult.get(i);
            Label entryLabel = new Label(result);
            CustomMenuItem item = new CustomMenuItem(entryLabel,true);
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    //System.out.println(actionEvent.getSource());
                    Main.findWord.setText(result);
                    //String explain = dictionaryManagement.dictionaryLookup(Main.findWord.getText(), true);
                    Main.temporaryWord = dictionaryManagement.dictionaryLookupLongExplain(Main.findWord.getText());
                    if (Main.temporaryWord.getLong_explain() != null)
                        Main.findWord.setText(Main.temporaryWord.getLong_explain());
                    else Main.findWord.setText(Main.temporaryWord.getWord_explain());
                    setText(result);
                    Main.translateSucceeded[0] = true;
                    entriesPopup.hide();
                }
            });
            menuItems.add(item);
        }
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);
    }
}
