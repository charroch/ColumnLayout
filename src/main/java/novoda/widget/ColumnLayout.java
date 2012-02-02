package novoda.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import novoda.widget.layout.ColumnTextLayout;

public class ColumnLayout extends RelativeLayout implements FlowableTextView.FlowableViewFactory {

    public static final int COLUMN = 21;

    private int columnCount;

    private CharSequence text;

    private int columnGap = 20;

    private TextView textView;

    // by how many column would you offset?
    private int columnOffset;
    private int columnWidth;

    public ColumnLayout(Context context) {
        this(context, null);
    }

    public ColumnLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColumnLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColumnLayout);

        setColumnCount(a.getInt(R.styleable.ColumnLayout_minColumnCount, -1));
        setColumnOffset(a.getInt(R.styleable.ColumnLayout_columnOffset, -1));
        setColumnWidth(a.getInt(R.styleable.ColumnLayout_columnWidth, -1));

        int tl = a.getResourceId(R.styleable.ColumnLayout_textViewLayout, -1);
        if (tl == -1) {
            textView = new TextView(context);
        } else {
            textView = (TextView) LayoutInflater.from(context).inflate(tl, null);
        }
        super.setChildrenDrawingOrderEnabled(true);
    }

    private int lastMeasuredWidth = -1;

    private int lastMeasuredHeight = -1;


    public void setColumnWidth(int width) {
        this.columnWidth = width;
    }

    public void setColumnGap(int gap) {
        this.columnGap = gap;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int columnWith = computeColumnWidth(width);
        int columnHeight = height;

        measureHeightChildren(columnWith, columnHeight);

        int computedWidth = 0;
        int id = 123;

        FlowableTextView view = new FlowableTextView(getContext());
        view.setTypeface(Typeface.MONOSPACE);
        view.setText(text);
        view.setId(id);
        view.setViewFactory(this);

        view.onMeasure(
                MeasureSpec.makeMeasureSpec(400, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(727, MeasureSpec.EXACTLY)
        );


        addFlowableView(view, columnWidth, height, id);
        computedWidth += 2500;

        debug(100);
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(computedWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        );
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(computedWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    private void measureHeightChildren(int columnWith, int columnHeight) {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.setId(6989);
            ColumnLayout.LayoutParams lp = (LayoutParams) v.getLayoutParams();
            v.measure(
                    MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY)
            );
            int columnIndex = lp.getColumnRules()[COLUMN];
            lp.addRule(RIGHT_OF, 123);
            lp.addRule(ALIGN_PARENT_TOP);
            updateViewLayout(v, lp);
        }
    }

    private int computeColumnWidth(int viewWidth) {
        if (columnCount == -1 && columnWidth == -1) {
            throw new IllegalStateException("need to set column count or columnWidth");
        }
        if (columnWidth > 0) {
            return columnWidth;
        } else {
            return (int) Math.floor(viewWidth / (columnCount + columnGap));
        }
    }

    private void addFlowableView(FlowableTextView view, int width, int h, int id) {
        LayoutParams lp = getChildLayoutParams(width, h, id);
        addView(view, lp);
//        if (view.next != null) {
//            Log.i("TEST", "!AD ");
//            addFlowableView(view.next, width, h, id);
//        }
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return i;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        getAvailableHeight(1);
    }


    private LayoutParams getChildLayoutParams(int width, int height, int id) {
        LayoutParams p = new LayoutParams(width, height);
        if (id == 123) {
            p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            p.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        } else if (id == 124) {
            p.addRule(RelativeLayout.BELOW, 6989);
            p.addRule(RelativeLayout.RIGHT_OF, id - 1);
        } else {
            p.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            p.addRule(RelativeLayout.RIGHT_OF, id - 1);
        }
        p.setMargins(0, 0, columnGap, 0);
        return p;
    }

    private void addTextView(CharSequence tv) {

    }

    private int getAvailableHeight(int column) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View v = getChildAt(i);
            ColumnLayout.LayoutParams lp = (LayoutParams) v.getLayoutParams();
            int columnIndex = lp.getColumnRules()[COLUMN];
            if (columnIndex == column) {
                Log.i("TEST", column + "<- column " + v + " measure " + v.getMeasuredHeight());
                return getColumnHeight() - v.getMeasuredHeight();
            }
        }
        return getColumnHeight();
    }

    private int getColumnHeight() {
        return 726;
    }

    public void setText(CharSequence text) {
        this.text = text;
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

    int i = 124;

    @Override
    public FlowableTextView createView(ColumnTextLayout layout) {
        Log.i("TEST", "creating view : " + i + " " + layout);
        FlowableTextView v = new FlowableTextView(getContext());
        v.setLayout(layout);
        v.setViewFactory(this);
        v.setId(i);

        int availableHeight = getAvailableHeight(layout.size());

        addView(v, getChildLayoutParams(400, availableHeight, i));
        i++;
        v.onMeasure(
                MeasureSpec.makeMeasureSpec(400, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(availableHeight, MeasureSpec.EXACTLY)
        );
        return v;
    }

    /**
     * Specific layout for column Span which extends relative's layout
     */
    public static class LayoutParams extends RelativeLayout.LayoutParams {

        public boolean alignWithParent;

        private int[] mRules;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            mRules = new int[100];
        }

        public LayoutParams(int w, int h) {
            super(w, h);
            mRules = new int[100];
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
            mRules = new int[100];
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
            mRules = new int[100];
        }

        public void addRule(int verb) {
            super.addRule(verb);
        }

        public void addRule(int verb, int anchor) {
            switch (verb) {
                case COLUMN:
                    mRules[verb] = anchor;
                    break;
                default:
                    super.addRule(verb, anchor);
                    break;
            }
        }

        public int[] getColumnRules() {
            return mRules;
        }
    }
}