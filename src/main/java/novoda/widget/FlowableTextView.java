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
    CharSequence fluidText;
    CharSequence originalText;
    CharSequence laidText;
    private ColumnTextLayout layout;
    int textComputedWidth;
    int textComputedHeight;
    private Column column;
    boolean isRoot;

    public FlowableTextView(Context context) {
        this(context, null);
    }

    public FlowableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public FlowableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
        super.onSizeChanged(w, h, oldw, oldh);
        textComputedWidth = getLayoutWidth(w);
        textComputedHeight = getLayoutHeight(h);
        layText();
    }

    private boolean hasNext() {
        return next != null;
    }

    private ColumnTextLayout getFlowableText() {
        return getRoot().layout;
    }


    protected void layText() {
        ColumnTextLayout layout = getFlowableText();
        layout.setCurrentColumn(column);
        column = layout.next(textComputedWidth, textComputedHeight);
        setText(column.getText());
        if (hasNext()) {
            next.layText();
        }
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
        if (isRoot) {
            return this;
        } else {
            return root.getRoot();
        }
    }

    public static interface FlowableViewFactory {
        FlowableTextView createView(ColumnTextLayout layout);
    }
}

