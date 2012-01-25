
package com.novoda.widget.columnLayout;

public class ColumnLayoutUtil {

    public static int getColumnWidth(int width, int columnCount, int gap) {
        return (width - (columnCount * gap)) / columnCount;
    }
}
