
package com.novoda.widget.columnLayout;

import android.graphics.Rect;
import android.text.Layout;

public class Column {

    private final Layout layout;

    public Column(Layout layout) {
        this.layout = layout;
    }

    public int getLineCount() {
        return layout.getLineCount();
    }

    public int getLineHeight(int line) {
        Rect bounds = new Rect();
        layout.getLineBounds(line, bounds);
        return (bounds.bottom - bounds.top);
    }

    public int getHeight() {
        return layout.getHeight();
    }
}
