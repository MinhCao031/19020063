package RawDictionary;

public class DictionaryCommandline {
    private final DictionaryManagement dictionaryManagement;

    public DictionaryCommandline() {
        dictionaryManagement = new DictionaryManagement();
    }

    public DictionaryManagement getDictionaryManagement() {
        return dictionaryManagement;
    }

    public Dictionary dictionarySearcher(String find, boolean startWithItOnly) {
        if(find.length() == 0) return this.getDictionaryManagement().getDictionary();
        find = find.toLowerCase();
        String finD = find.substring(0, 1).toUpperCase().concat(find.substring(1));

        if(startWithItOnly) {
            Dictionary[] startWithTarget = new Dictionary[10];
            for (int i = 0; i < 10; ++i) {
                startWithTarget[i] = new Dictionary();
            }

            find = finD;
            for (Word word : dictionaryManagement.getDictionary().getListOfWord()) {
                if (word.getWord_target().contains(find)) {
                    int priority = word.getWord_target().length() - find.length();
                    //if (priority > 20) continue;
                    if (priority > 9) priority = 9;
                    startWithTarget[priority].add(word,false);
                }
            }
            for (int i = 1; i < 10; ++i) {
                startWithTarget[0].addList(startWithTarget[i]);
            }
            if (startWithTarget[0].getListSize() == 0) {
                startWithTarget[0].add(new Word(find,find),false);
            }
            return startWithTarget[0];
        } else {
            Dictionary[] containTarget = new Dictionary[10];
            Dictionary[] startWithTarget = new Dictionary[10];
            for (int i = 0; i < 10; ++i) {
                startWithTarget[i] = new Dictionary();
                containTarget[i] = new Dictionary();
            }

            for (Word word : dictionaryManagement.getDictionary().getListOfWord()) {
                if (word.getWord_target().contains(finD)) {
                    int priority = word.getWord_target().length() - finD.length();
                    //if (priority > 20) continue;
                    if (priority > 9) priority = 9;
                    startWithTarget[priority].add(word,false);
                } else if (word.getWord_target().contains(find)) {
                    int priority = word.getWord_target().length() - finD.length() - 1;
                    //if (priority > 20) continue;
                    if (priority > 9) priority = 9;
                    containTarget[priority].add(word,false);
                }
            }
            for (int i = 1; i < 10; ++i) {
                startWithTarget[0].addList(startWithTarget[i]);
                containTarget[0].addList(containTarget[i]);
            }
            startWithTarget[0].addList(containTarget[0]);
            if (startWithTarget[0].getListSize() == 0) {
                startWithTarget[0].add(new Word(find,find),false);
            }
            return startWithTarget[0];
        }
    }

    public Dictionary dictionarySearchExact(String find, boolean startWithItOnly) {
        if(find.length() == 0) return this.getDictionaryManagement().getDictionary();

        if(startWithItOnly) {
            Dictionary[] startWithTarget = new Dictionary[10];
            for (int i = 0; i < 10; ++i) {
                startWithTarget[i] = new Dictionary();
            }
            for (Word word : dictionaryManagement.getDictionary().getListOfWord()) {
                if (word.indexOfExactTarget(find) == 0) {
                    int priority = word.getWord_target().length() - find.length();
                    //if (priority > 20) continue;
                    if (priority > 9) priority = 9;
                    startWithTarget[priority].add(word,false);
                }
            }
            for (int i = 1; i < 10; ++i) {
                startWithTarget[0].addList(startWithTarget[i]);
            }
            if (startWithTarget[0].getListSize() == 0) {
                startWithTarget[0].add(new Word(find.toLowerCase(),find.toLowerCase()),false);
            }
            return startWithTarget[0];
        } else {
            Dictionary[] containTarget = new Dictionary[10];
            Dictionary[] startWithTarget = new Dictionary[10];
            for (int i = 0; i < 10; ++i) {
                startWithTarget[i] = new Dictionary();
                containTarget[i] = new Dictionary();
            }

            for (Word word : dictionaryManagement.getDictionary().getListOfWord()) {
                if (word.indexOfExactTarget(find) == 0) {
                    int priority = word.getWord_target().length() - find.length();
                    //if (priority > 20) continue;
                    if (priority > 9) priority = 9;
                    startWithTarget[priority].add(word,false);
                } else {
                    int priority = word.indexOfExactTarget(find);
                    if (priority < 0 || priority > 20) continue;
                    if (priority > 9) priority = 9;
                    containTarget[priority].add(word,false);
                }
            }
            for (int i = 1; i < 10; ++i) {
                startWithTarget[0].addList(startWithTarget[i]);
                containTarget[0].addList(containTarget[i]);
            }
            startWithTarget[0].addList(containTarget[0]);
            if (startWithTarget[0].getListSize() == 0) {
                startWithTarget[0].add(new Word(find.toLowerCase(),find.toLowerCase()),false);
            }
            return startWithTarget[0];
        }
    }
}
