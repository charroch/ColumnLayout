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
        layout = new ColumnTextLayout(getText(), getPaint());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        layout = new ColumnTextLayout(getText(), getPaint());
        int width = right - left;
        int height = bottom - top;
        column = layout.next(width, height);

        setText(column.getText());
        setLaidText(column.getText());

        Log.i("TEST", "hello world from: -> " + getId());
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
        this.next = next;
    }

    public FlowableTextView getNextFlowableTextView() {
        return next;
    }
}
