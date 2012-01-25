package novoda.widget.layout;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout.Alignment;

public class ColumnTextLayout {
    public int charStart;

    public int charEnd;

    public int lineStart;

    public int lineEnd;

    private final CharSequence text;

    private final TextPaint textPaint;

    public ColumnTextLayout() {
        text = null;
        textPaint = null;
    }

    public ColumnTextLayout(CharSequence text, TextPaint textPaint) {
        this.text = text;
        this.textPaint = textPaint;
    }

    public Column next(int width, int height) {
        Layout layout = getLayout(width);
        int firstNextLine = layout.getLineForVertical(height);
        int lastLine = firstNextLine - 1;
        int end = layout.getLineEnd(lastLine);
        StaticLayout columnLayout = new StaticLayout(text.subSequence(0, end), textPaint, width,
                Alignment.ALIGN_NORMAL, 1, 1, true);
        Column column = new Column(columnLayout);
        return column;
    }

    private StaticLayout getLayout(int width) {
        return new StaticLayout(text, textPaint, width, Alignment.ALIGN_NORMAL, 1,
                1, true);
    }

    public int getColumnCount(int width, int height) {
        return (int) Math.ceil(getLayout(width).getHeight() / height);
    }

    @Override
    public String toString() {
        return String.format("first char: %s, for line: %s, last char: %s, for line: %s",
                charStart, lineStart, charEnd, lineEnd);
    }
}