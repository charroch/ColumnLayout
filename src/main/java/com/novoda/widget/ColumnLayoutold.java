
package com.novoda.widget;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout.Alignment;
import android.text.style.DynamicDrawableSpan;
import android.text.style.LineBackgroundSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class ColumnLayoutold extends LinearLayout {

    // TypedArray indices
    // private static final int COLUMN_WIDTH =
    // R.styleable.GridLayout_columnCount;
    // private static final int COLUMN_GAP =
    // R.styleable.GridLayout_useDefaultMargins;

    private int widthPerColumn;

    private int columnGap;

    private int marginTop;

    private int marginBottom;

    public ColumnLayoutold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColumnLayout);

        marginTop = a.getDimensionPixelSize(R.styleable.ColumnLayout_android_layout_margin, -1);

        // padding = a.getInt(R.styleable.ColumnLayout_, -1);

        Log.i("TEST", "bottom: " + marginBottom + " top " + marginTop);
        // try {
        // setRowCount(a.getInt(ROW_COUNT, DEFAULT_COUNT));
        // setColumnCount(a.getInt(COLUMN_COUNT, DEFAULT_COUNT));
        // setOrientation(a.getInt(ORIENTATION, DEFAULT_ORIENTATION));
        // setUseDefaultMargins(a.getBoolean(USE_DEFAULT_MARGINS,
        // DEFAULT_USE_DEFAULT_MARGINS));
        // setAlignmentMode(a.getInt(ALIGNMENT_MODE, DEFAULT_ALIGNMENT_MODE));
        // setRowOrderPreserved(a.getBoolean(ROW_ORDER_PRESERVED,
        // DEFAULT_ORDER_PRESERVED));
        // setColumnOrderPreserved(a.getBoolean(COLUMN_ORDER_PRESERVED,
        // DEFAULT_ORDER_PRESERVED));
        // } finally {
        // a.recycle();
        // }
    }

    public ColumnLayoutold(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColumnLayoutold(Context context) {
        this(context, null);
    }

    public String convertStreamToString(InputStream is) {
        return new Scanner(is).useDelimiter("\\A").next();
    }

    public int getPageCount() {
        return -1;
    }

    public Page getPage(int index) {
        return null;
    }

    public int getColumnPerPage() {
        return -1;
    }

    public int getColumnWidth() {
        return -1;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        Log.i("TEST", "HERE " + (getTop() - getBottom()) + " AND " + (t - b) + getLayoutParams());

        int avalaibleHeight = getMeasuredHeight();
        int availableWidth = getMeasuredWidth();
        Log.i("TEST", "Actual width: " + getWidth() + " " + getPaddingLeft() + " "
                + getPaddingRight() + " ");

        int desiredColumn = 4;
        columnGap = 40;

        widthPerColumn = (availableWidth - desiredColumn * columnGap) / desiredColumn;

        Log.i("TEST", String.format("l %s, t %s, t %s, b %s with %s , %s with: %s", l, t, r, b,
                availableWidth, avalaibleHeight, widthPerColumn));

        // TextView text =
        // LayoutInflater.from(getContext()).inflate(R.layout.single_column,
        // this);
        TextView text = (TextView) getChildAt(0);
        text.setWidth(widthPerColumn);
        Spanned tt = null;

        try {
            InputStream in = getContext().getAssets().open("letters.xhtml");
            tt = new SpannableXML(this.getContext()).get(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        text.setText(tt, BufferType.SPANNABLE);
        TextPaint p = text.getPaint();
        StaticLayout lay = new StaticLayout(tt, p, widthPerColumn, Alignment.ALIGN_NORMAL, 1, 1,
                true);
        ColumnLayoutIterator columnLayoutIterator = new ColumnLayoutIterator(lay, l, t, r, b);

        CharSequence cs = text.getText();
        boolean first = true;
        for (ColumnTextLayout o : columnLayoutIterator) {
            Log.i("TEST", "o => " + o.toString());
            if (first) {
                text.setText(cs.subSequence(o.charStart, o.charEnd));
            } else {
                TextView v = (TextView) LayoutInflater.from(getContext()).inflate(
                        R.layout.single_column, null);

                LayoutParams layoutParams = new LinearLayout.LayoutParams(widthPerColumn,
                        LinearLayout.LayoutParams.FILL_PARENT);
                layoutParams.setMargins(0, 0, columnGap, 0);
                v.setLayoutParams(layoutParams);
                v.setText(cs.subSequence(o.charStart, o.charEnd));
                this.addView(v);
            }
            first = false;
        }
        // addViewFlowable(text);
    }

    private void addViewFlowable(TextView view) {
        int viewHeight = view.getMeasuredHeight();
        Layout layout = view.getLayout();
        CharSequence cs = view.getText();

        int nbColumn = (int) Math.ceil(layout.getHeight() / viewHeight);
        int current = 0;
        for (int i = 1; i <= nbColumn; i++) {
            int lV = layout.getLineForVertical(current + viewHeight);

            int lineN = lV - 2;
            current += layout.getLineBottom(lineN);
            int charAt = layout.getLineEnd(lineN);
            view.setText(cs.subSequence(0, charAt));
            CharSequence remaining = cs.subSequence(charAt, cs.length() - 1);
            TextView v = (TextView) LayoutInflater.from(getContext()).inflate(
                    R.layout.single_column, null);

            LayoutParams layoutParams = new LinearLayout.LayoutParams(widthPerColumn,
                    LinearLayout.LayoutParams.FILL_PARENT);
            layoutParams.setMargins(0, 0, columnGap, 0);
            v.setLayoutParams(layoutParams);
            v.setText(remaining);
            this.addView(v);
        }
    }
}
