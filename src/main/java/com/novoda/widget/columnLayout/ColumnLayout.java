
package com.novoda.widget.columnLayout;

import com.novoda.widget.ColumnLayoutIterator;
import com.novoda.widget.ColumnTextLayout;
import com.novoda.widget.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout.Alignment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class ColumnLayout extends RelativeLayout {

    private int columnCount;

    private CharSequence text;

    private int columnGap = 20;

    private TextView textView;

    // by how many column would you offset?
    private int columnOffset;

    public ColumnLayout(Context context) {
        this(context, null);
    }

    public ColumnLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColumnLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColumnLayout);
        setColumnCount(a.getInt(R.styleable.ColumnLayout_minColumnCount, 1));
        setColumnOffset(a.getInt(R.styleable.ColumnLayout_columnOffset, 0));
        int tl = a.getResourceId(R.styleable.ColumnLayout_textViewLayout, -1);
        if (tl == -1) {
            textView = new TextView(context);
        } else {
            textView = (TextView) LayoutInflater.from(context).inflate(tl, null);
        }
    }

    private int lastMeasuredWidth = -1;

    private int lastMeasuredHeight = -1;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int currentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int currentHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (currentWidth != lastMeasuredWidth || currentHeight != lastMeasuredHeight) {
            lastMeasuredWidth = MeasureSpec.getSize(widthMeasureSpec);
            lastMeasuredHeight = MeasureSpec.getSize(heightMeasureSpec);
            this.removeAllViews();
            appendColumns(widthMeasureSpec, heightMeasureSpec);
        } else {
        }

    }

    private void appendColumns(int widthMeasureSpec, int heightMeasureSpec) {

        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);

        int widthPerColumn = ColumnLayoutUtil.getColumnWidth(measuredWidth, getColumnCount(),
                columnGap);

        TextPaint p = textView.getPaint();

        StaticLayout lay = new StaticLayout(text, p, widthPerColumn, Alignment.ALIGN_NORMAL, 1, 1,
                true);

        ColumnLayoutIterator columnLayoutIterator = new ColumnLayoutIterator(lay, 0, 0,
                widthPerColumn, measuredHeight);

        CharSequence cs = text;
        int i = 2;
        int j = 0;
        for (ColumnTextLayout o : columnLayoutIterator) {
            if (j >= getColumnOffset()) {
                // append
                appendColumnTextView(widthPerColumn, cs, i, o);
            } else {
                // do nothing
            }
            j++;
            i++;
        }
    }

    private void appendColumnTextView(int widthPerColumn, CharSequence cs, int i, ColumnTextLayout o) {
        TextView v = new TextView(getContext());
        v.setTextColor(textView.getTextColors());
        v.setTextSize(textView.getTextSize());
        v.setId(i);
        v.setText(cs.subSequence(o.charStart, o.charEnd));
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(widthPerColumn,
                LinearLayout.LayoutParams.FILL_PARENT);
        layoutParams.setMargins(columnGap / 2, 0, columnGap / 2, 0);
        if (i - getColumnOffset() == 2) {
            layoutParams.addRule(ALIGN_PARENT_LEFT);
            this.addView(v, layoutParams);
        } else {
            View view = findViewById(i - 1);
            if (view == null) {
                // soemthing
            } else {
                layoutParams.addRule(RIGHT_OF, view.getId());
                this.addView(v, layoutParams);
            }
        }
    }

    public void setText(CharSequence text) {
        this.text = text;
        invalidate();
    }

    public CharSequence getText() {
        return text;
    }

    public void setColumnOffset(int columnOffset) {
        this.columnOffset = columnOffset;
        invalidate();
    }

    public int getColumnOffset() {
        return columnOffset;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
        invalidate();
    }

    public int getColumnCount() {
        return columnCount;
    }
}
