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

        setColumnCount(a.getInt(R.styleable.ColumnLayout_minColumnCount, 1));
        setColumnOffset(a.getInt(R.styleable.ColumnLayout_columnOffset, 0));
        setColumnWidth(a.getInt(R.styleable.ColumnLayout_columnWidth, 0));

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
        int currentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int currentHeight = MeasureSpec.getSize(heightMeasureSpec);
//        if (currentWidth != lastMeasuredWidth || currentHeight != lastMeasuredHeight) {
//            lastMeasuredWidth = MeasureSpec.getSize(widthMeasureSpec);
//            lastMeasuredHeight = MeasureSpec.getSize(heightMeasureSpec);
//            this.removeAllViews();
//            appendColumns(widthMeasureSpec, heightMeasureSpec);
//        } else {
//        }


        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            ColumnLayout.LayoutParams lp = (LayoutParams) v.getLayoutParams();
            int columnIndex = lp.getColumnRules()[COLUMN];
            lp.addRule(RIGHT_OF, 123);
            lp.addRule(ALIGN_PARENT_TOP);
            //v.setLayoutParams(lp);
            updateViewLayout(v, lp);
        }

        int width = columnWidth;
        int height = currentHeight;

        int computedWidth = 0;
        int id = 123;

        int i = 0;


        FlowableTextView view = new FlowableTextView(getContext());
        view.setTag("column_0");
        view.setTypeface(Typeface.MONOSPACE);
        view.setText(text);
        view.setId(id);
        view.setViewFactory(this);
        view.requestLayout();

        Log.i("TEST", " => " + view.getMeasuredHeight());


        addFlowableView(view, columnWidth, currentHeight, id);
//        LayoutParams lp = getChildLayoutParams(columnWidth, currentHeight, id);
//        addView(view, lp);

//        view.requestLayout();

        computedWidth += 2500;
        id += 1;


        requestLayout();

        debug(100);
        super.onMeasure(MeasureSpec.makeMeasureSpec(computedWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(currentHeight, MeasureSpec.EXACTLY));
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(computedWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(currentHeight, MeasureSpec.EXACTLY));
    }

    private void addFlowableView(FlowableTextView view, int width, int h, int id) {
        Log.i("TEST", this + "addFlowableView should be after is this called ");
        LayoutParams lp = getChildLayoutParams(width, h, id);

        Log.i("TEST", " 2 => " + view.getMeasuredHeight() + " " + view.next);

        addView(view, lp);
        if (view.next != null) {
            Log.i("TEST", "!AD ");
            addFlowableView(view.next, width, h, id);
        }
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return i;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    private LayoutParams getChildLayoutParams(int width, int height, int id) {
        LayoutParams p = new LayoutParams(width, height);
        if (id == 123) {
            p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            p.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        } else {
            p.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            p.addRule(RelativeLayout.RIGHT_OF, id - 1);
        }
        p.setMargins(0, 0, columnGap, 0);
        return p;
    }

    private void addTextView(CharSequence tv) {

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
        //v.setLayoutParams(getChildLayoutParams(400, 727, i));
        v.setId(i);
        //addView(v, getChildLayoutParams(400, 727, i));
        i++;
        v.requestLayout();
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