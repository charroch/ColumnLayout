
package com.novoda.widget;

import android.graphics.Rect;
import android.text.Layout;

import java.util.Iterator;

public class ColumnLayoutIterator implements Iterable<ColumnTextLayout>, Iterator<ColumnTextLayout> {

    private final Layout layout;

    private final Rect container;

    private int index = 0;

    private int currentColumn = 0;

    private ColumnTextLayout previousColumn;

    public ColumnLayoutIterator(Layout layout, int left, int top, int right, int bottom) {
        this.layout = layout;
        container = new Rect(left, top, right, bottom);
    }

    public int getColumnCount() {
        throw new UnsupportedOperationException("iterate for now");
    }

    public ColumnTextLayout getColumnText(int column) {
        throw new UnsupportedOperationException("iterate for now");
    }

    @Override
    public boolean hasNext() {
        return (container.bottom * index) < layout.getHeight();
    }

    @Override
    public ColumnTextLayout next() {
        int firstNextLine = layout.getLineForVertical(container.bottom + (index++ * getHeight()));
        int lastLine = firstNextLine - 1;
        int end = layout.getLineEnd(lastLine);

        ColumnTextLayout ret = new ColumnTextLayout();
        ret.charEnd = end;
        ret.lineEnd = lastLine;

        if (previousColumn == null) {
            ret.lineStart = 0;
            ret.charStart = 0;
        } else {
            ret.lineStart = previousColumn.lineEnd + 1;
            ret.charStart = previousColumn.charEnd;
        }
        previousColumn = ret;
        return ret;
    }

    private int getHeight() {
        return container.bottom - container.top;
    }

    @Override
    public Iterator<ColumnTextLayout> iterator() {
        return this;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException(
                "This is bad design... as per Query Command pattern");
    }
}
