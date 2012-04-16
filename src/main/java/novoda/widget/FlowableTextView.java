package novoda.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import novoda.widget.layout.Column;
import novoda.widget.layout.ColumnTextLayout;

public class FlowableTextView extends TextView {

    FlowableTextView root;
    FlowableTextView next;

    public CharSequence getOriginalText() {
        return originalText;
    }

    public void setOriginalText(CharSequence originalText) {
        this.originalText = originalText;
    }

    CharSequence originalText;
    CharSequence laidText;

    FlowableViewFactory factory;

    private ColumnTextLayout layout;

    int textComputedWidth;
    int textComputedHeight;

    private Column column;

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    boolean isRoot;

    public FlowableViewFactory getViewFactory() {
        return factory;
    }

    public void setViewFactory(FlowableViewFactory factory) {
        this.factory = factory;
    }

    public FlowableTextView(Context context) {
        this(context, null);
    }

    public FlowableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public FlowableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void requestLayout() {
        super.requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        if (mode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(
                    resolveSizeAndState(width, widthMeasureSpec, 0),
                    resolveSizeAndState(height, heightMeasureSpec, 0)
            );
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void setTextWithinLayout(int textWidth, int textHeight) {
        if (layout != null && layout.hasNext() && needText(textWidth, textHeight)) {
            column = layout.next(textWidth, textHeight);
            textComputedWidth = textWidth;
            textComputedHeight = textHeight;
            setText(column.getText());
            if (next != null) {
                next.setLayout(layout);
            }
        }
    }

    private boolean needText(int textWidth, int textHeight) {
        return !isUpToDate(textWidth, textHeight);
    }

    private boolean isUpToDate(int textWidth, int textHeight) {
        return (textComputedHeight == textHeight) && (textComputedWidth == textWidth);
    }

    public void setLayout(ColumnTextLayout l) {
        this.layout = l;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.i("TEST", "WIDTH: " + w + " HEIGHT: " + h + " OLD W " + oldw + " oldh " + oldh);
        textComputedWidth = getLayoutWidth(w);
        textComputedHeight = getLayoutHeight(h);
        layout.setCurrentColumn(column);

        if (next != null) {
            next.onPreviousTextChange(layout);
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    protected void onPreviousTextChange(ColumnTextLayout layout) {
        column = layout.next(textComputedWidth, textComputedHeight);
        setText(column.getText());
        if (next != null) {
            next.onPreviousTextChange(layout);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int viewWidth = right - left;
        int viewHeight = bottom - top;
        int layoutWidth = getLayoutWidth(viewWidth);
        int layoutHeight = getLayoutHeight(viewHeight);
        //setText(layout.next(layoutWidth, layoutHeight).getText());
        super.onLayout(changed, left, top, right, bottom);
        if (isUpToDate(layoutWidth, layoutHeight)) {
            Log.i("TEST", "UP TO DATE");
        }
        requestLayout();
    }

    private int getLayoutHeight(int height) {
        return height - getPaddingTop() - getPaddingBottom();
    }

    private int getLayoutWidth(int width) {
        return width - getPaddingLeft() - getPaddingRight();
    }

    public void setLaidText(CharSequence text) {
        this.laidText = text;
        // invalidate();
    }

    public CharSequence getLaidText() {
        return laidText;
    }


    public void setNextFlowableTextView(FlowableTextView next) {
        next.root = getRoot();
        this.next = next;
    }

    public FlowableTextView getNextFlowableTextView() {
        return next;
    }

    public FlowableTextView getRoot() {
        if (isRoot()) {
            return this;
        } else {
            return root.getRoot();
        }
    }

    public static interface FlowableViewFactory {
        FlowableTextView createView(ColumnTextLayout layout);
    }

    @Override
    public void invalidate() {
        requestLayout();
        super.invalidate();
        if (isRoot() && next != null) {
            next.invalidate();
        }
    }
}

