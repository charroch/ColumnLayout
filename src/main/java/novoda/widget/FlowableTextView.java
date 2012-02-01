package novoda.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import novoda.widget.layout.Column;
import novoda.widget.layout.ColumnTextLayout;

public class FlowableTextView extends TextView {

    FlowableTextView next;

    FlowableTextView root;

    CharSequence originalText;

    CharSequence laidText;

    public FlowableViewFactory getViewFactory() {
        return factory;
    }

    public void setViewFactory(FlowableViewFactory factory) {
        this.factory = factory;
    }

    FlowableViewFactory factory;

    private ColumnTextLayout layout;

    private Column column;

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

        if (layout == null && getText() != null) {
            layout = new ColumnTextLayout(getText(), getPaint());
        } else if (layout.hasNext() && factory != null) {

            Log.i("TEST", layout.size() + " = > is this called? " + width + " " + height);

            Column column = layout.next(width, height);
            this.setText(column.getText());
            next = factory.createView(layout);
            next.factory = this.factory;
            next.layout = layout;
            next.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        if (layout == null) {
//            layout = new ColumnTextLayout(getText(), getPaint());
//            if (next != null) {
//                next.layout = layout;
//            }
//        }
//        int width = right - left;
//        int height = bottom - top;
//        column = layout.next(width, height);
//        setText(column.getText());
//        setLaidText(column.getText());
//        if (layout.hasNext() && factory != null) {
//            next = factory.createView(layout);
//            next.layout = layout;
//        }
        super.onLayout(changed, left, top, right, bottom);
    }

    public void setLaidText(CharSequence text) {
        this.laidText = text;
        // invalidate();
    }

    public CharSequence getLaidText() {
        return laidText;
    }

    public void setNextFlowableTextView(FlowableTextView next) {
        next.root = this;
        next.layout = this.layout;
        this.next = next;
    }

    public FlowableTextView getNextFlowableTextView() {
        return next;
    }

    public static interface FlowableViewFactory {
        FlowableTextView createView(ColumnTextLayout layout);
    }
}

