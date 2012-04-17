package novoda.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import novoda.widget.layout.Column;
import novoda.widget.layout.ColumnTextLayout;

import java.util.Stack;

public class FlowableTextView extends TextView {
    Root root;

    Column column;

    int textComputedWidth;
    int textComputedHeight;

    public FlowableTextView(Context context) {
        this(context, null);
    }

    public FlowableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public FlowableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        root = new Root();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * We should resolve to the greatest size. If unspecified, it will yield the textview measurement
         * which is the size of the text. If AT_MOST is the mode then we return the most amount we can use.
         *
         * This is to ensure any view group will yield as much space as possible to this view.
         */
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);

        Log.i("TEST", this + " " + MeasureSpec.toString(heightMeasureSpec));

        if (mode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(
                    resolveSizeAndState(width, widthMeasureSpec, 0),
                    resolveSizeAndState(height, heightMeasureSpec, 0)
            );
        } else if (mode == MeasureSpec.UNSPECIFIED) {
            setMeasuredDimension(
                    resolveSizeAndState(width, widthMeasureSpec, 0),
                    resolveSizeAndState(1000, heightMeasureSpec, 0)
            );
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        textComputedWidth = getLayoutWidth(w);
        textComputedHeight = getLayoutHeight(h);
        root.sizeChange(this);
    }

    private int getLayoutHeight(int height) {
        return height - getPaddingTop() - getPaddingBottom();
    }

    private int getLayoutWidth(int width) {
        return width - getPaddingLeft() - getPaddingRight();
    }

    public void setNextFlowableTextView(FlowableTextView next) {
//        next.root = root;
//        next.setIndex(getIndex() + 1);
//        root.sizeChange(next);
    }

    public void setFlowableText(CharSequence text) {
        root.setText(text);
    }

    public static interface FlowableViewFactory {
        FlowableTextView createView(int index);
    }

    public void setFlowableViewFactory(FlowableViewFactory factory) {
        root.factory = factory;
    }

    private class Root {

        Stack<FlowableTextView> children;

        ColumnTextLayout layout;

        CharSequence text;

        FlowableViewFactory factory;

        public Root() {
            children = new Stack<FlowableTextView>();
        }

        void setText(CharSequence s) {
            text = s;
            layout = new ColumnTextLayout(s, getPaint());
        }

        public void sizeChange(FlowableTextView flowableTextView) {
            int index = children.indexOf(flowableTextView);
            if (index < 0) {
                children.push(flowableTextView);
                index = 0;
            }
            if (layout != null) {
                resizeFrom(index);
                fill();
            }
        }

        private void resizeFrom(int index) {
            Log.i("TEST", children.size() + " RESIZE FROM : " + index);
            layout.setCurrentIndex(index);
            int size = children.size();
            for (int i = index; i < size; i++) {
                FlowableTextView c = children.get(i);
                if (c != null) {
                    Column column = layout.next(c.textComputedWidth, c.textComputedHeight, c.getPaint());
                    c.setText(column.getText());
                    c.invalidate();
                }
            }
        }

        private void fill() {
            if (layout.hasNext()) {
                FlowableTextView c = factory.createView(children.size());
                c.root = this;
                c.setText(layout.getText());
                int height = c.getMeasuredHeight();
                int width = c.getMeasuredWidth();
                //Column column = layout.next(width, height, c.getPaint());
               // c.setText(column.getText());
                c.invalidate();
                children.add(c);
            }
        }
    }
}

