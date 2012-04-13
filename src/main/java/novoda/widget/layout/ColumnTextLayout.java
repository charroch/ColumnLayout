package novoda.widget.layout;

import android.graphics.Rect;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import java.util.Stack;

public class ColumnTextLayout {

    /**
     * Original text for all the columns.
     */
    private final CharSequence text;

    /**
     * Same paint will be used for all columns
     */
    private final TextPaint textPaint;

    Stack<Column> columns;

    public ColumnTextLayout(CharSequence text, TextPaint textPaint) {
        this.text = text;
        this.textPaint = textPaint;
        columns = new Stack<Column>();
    }

    public CharSequence getText() {
        return text;
    }

    synchronized public Column next(int width, int height) {
        int firstCharPosition = getFirstChar();
        int lastLine = getLastLine(width, height);
        int nbChar = getLastChar(lastLine, width);
        StaticLayout columnLayout = new StaticLayout(
                text.subSequence(firstCharPosition, firstCharPosition + nbChar),
                textPaint,
                width,
                Alignment.ALIGN_NORMAL, 1, 1, true);
        Column column = new Column(columnLayout, firstCharPosition);
        columns.push(column);
        return column;
    }

    public int getTextHeight() {
        Rect r = new Rect();
        textPaint.getTextBounds("A", 0, 1, r);
        return Math.abs(r.top);
    }

    public boolean hasNext() {
        if (columns.empty() && text.length() > 0) return true;
        if (columns.empty() && text.length() == 0) return false;
        return text.length() > columns.peek().getLastCharPosition();
    }

    public int size() {
        return columns.size();
    }

    private int getLastChar(int lastLine, int width) {
        if (columns.isEmpty()) {
            return getLayout(0, width).getLineEnd(lastLine);
        } else {
            int lastChar = columns.peek().getLastCharPosition();
            return getLayout(lastChar, width).getLineEnd(lastLine);
        }
    }

    private int getLastLine(int width, int height) {
        if (columns.isEmpty()) {
            return getLayout(0, width).getLineForVertical(height);
        } else {
            int lastChar = columns.peek().getLastCharPosition();
            return getLayout(lastChar, width).getLineForVertical(height);
        }
    }

    private int getFirstChar() {
        if (columns.isEmpty()) {
            return 0;
        } else {
            return columns.peek().getLastCharPosition();
        }
    }

    private StaticLayout getLayout(int fromChar, int width) {
        return new StaticLayout(
                text.subSequence(fromChar, text.length()), textPaint, width, Alignment.ALIGN_NORMAL, 1,
                1, true);
    }

    synchronized public void setCurrentColumn(Column column) {
        int index = columns.indexOf(column);
        int size = columns.size();
        int removeNb = size - index;
        if (removeNb > 0) {
            for (int i = 0; i < removeNb; i++) {
                columns.remove(index + i);
            }
        }
    }
}
