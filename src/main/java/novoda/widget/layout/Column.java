package novoda.widget.layout;

import android.text.Layout;

public class Column {

    /**
     * The start index within the initial text
     */
    private final int charStart;
    private final Layout layout;
    private final int index;

    public Column(Layout layout, int charStart, int index) {
        this.layout = layout;
        this.charStart = charStart;
        this.index = index;
    }

    public int getLineCount() {
        return layout.getLineCount();
    }

    public CharSequence getText() {
        return layout.getText();
    }

    public int getLastCharPosition() {
        return charStart + layout.getText().length();
    }

    public String toString() {
        return String.format(
                "Column: first character at %s, last character at %s: \n %s",
                charStart, getLastCharPosition(), layout.getText());
    }
}