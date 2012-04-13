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

    CharSequence originalText;
    CharSequence laidText;

    private boolean isInitiated = false;

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
        }

        if (layout.hasNext() && !isInitiated) {

            Log.i("TEST", "=====================> width: " + width + " height: " + height);
            Column column = layout.next(width, height);
            this.setText(column.getText());
            // next = factory.createView(layout);
//
//            next.factory = this.factory;
//            next.layout = layout;
            isInitiated = true;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setLayout(ColumnTextLayout l) {
        this.layout = l;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
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

