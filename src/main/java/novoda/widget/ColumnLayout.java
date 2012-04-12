package novoda.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import novoda.widget.layout.ColumnTextLayout;

import static android.view.View.MeasureSpec.makeMeasureSpec;

public class ColumnLayout extends ViewGroup {

    private int columnCount;
    private float columnWidth;
    private float columnGap;
    private int paragraphStyle;

    private Column first;

    private CharSequence text;
    private ColumnTextLayout columnTextLayout;
    private static final float DEFAULT_COLUMN_GAP = 25.0f;
    private static final float DEFAULT_COLUMN_WIDTH = 300.0f;
    int fullWidth;

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public float getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(float columnWidth) {
        this.columnWidth = columnWidth;
    }

    public float getColumnGap() {
        return columnGap;
    }

    public void setColumnGap(float columnGap) {
        this.columnGap = columnGap;
    }

    public ColumnLayout(Context context) {
        this(context, null);
    }

    public ColumnLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColumnLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColumnLayout);
        try {
            setColumnCount(a.getInt(R.styleable.ColumnLayout_columnCount, 1));
            setColumnGap(a.getDimension(R.styleable.ColumnLayout_columnGap, DEFAULT_COLUMN_GAP));
            setColumnWidth(a.getDimension(R.styleable.ColumnLayout_columnWidth, DEFAULT_COLUMN_WIDTH));
            setParagraphStyle(a.getResourceId(R.styleable.ColumnLayout_paragraphStyle, -1));
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int width = MeasureSpec.getSize(widthSpec);
        int height = MeasureSpec.getSize(heightSpec);

        fullWidth = width;
        int widthMode = MeasureSpec.getMode(widthSpec);


        int columnWidth = computeColumnWidth(width);

        first = new Column(0, columnWidth, height, (int) getColumnGap(), getColumnMargin());

        for (int i = 0, N = getChildCount(); i < N; i++) {
            first.measure(getChildAt(i));
        }

        int hPadding = getPaddingLeft() + getPaddingRight();
        int vPadding = getPaddingTop() + getPaddingBottom();

        int measuredWidth = Math.max(hPadding + width, getSuggestedMinimumWidth());
        int measuredHeight = Math.max(vPadding + height, getSuggestedMinimumHeight());

        int pages = (int) Math.ceil(first.getComputedColumnCount() / getColumnCount()) + 1;

        int layoutWidth = pages * width;

        int a = makeMeasureSpec(layoutWidth, MeasureSpec.UNSPECIFIED);

        setMeasuredDimension(
                //resolveSizeAndState(layoutWidth, a, 0),
                resolveSizeAndState(width, widthSpec, 0),
                resolveSizeAndState(height, heightSpec, 0)
        );
    }

    private MarginLayoutParams getColumnMargin() {
        int width = makeMeasureSpec((int) getColumnWidth(), MeasureSpec.UNSPECIFIED);
        MarginLayoutParams params = new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(20, 20, 20, 20);
        return params;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        for (int i = 0, N = getChildCount(); i < N; i++) {
            first.layout(getChildAt(i), left, top, right, bottom);
        }
        first.fillIn(columnTextLayout);
    }


    private boolean hasText() {
        return !TextUtils.isEmpty(text);
    }

    private String debugView(View v) {
        try {
            return getContext().getResources().getResourceName(v.getId());
        } catch (Exception e) {
            return "NO_ID [ " + v + " ]";
        }
    }

    class Page2 extends ViewGroup {
        private final ColumnLayout l;

        public Page2(Context context, ColumnLayout l) {
            super(context);
            this.l = l;
        }

        @Override
        protected void onMeasure(int widthSpec, int heightSpec) {
            l.onMeasure(widthSpec, heightSpec);
            setMeasuredDimension(l.getMeasuredWidthAndState(), l.getMeasuredHeightAndState());
            super.onMeasure(widthSpec, heightSpec);
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            l.onLayout(changed, left, top, right, bottom);
        }
    }


    public ViewGroup getPage() {

        Page2 p = new Page2(getContext(), this);
        int cc = getChildCount();
        for (int i = 0; i < cc; i++) {
            View v = getChildAt(i);
            if (v != null) {
                int ci = getLayoutParams(v).columnIndex;
                if (ci < 3) {
                    removeView(v);
                    p.addView(v);
                }
            }
        }
        return p;
    }

    private LayoutParams getLayoutParams(View c) {
        ViewGroup.LayoutParams p = c.getLayoutParams();
        if (p instanceof LayoutParams) {
            return (LayoutParams) c.getLayoutParams();
        } else {
            return new LayoutParams(p);
        }
    }

    private int computeColumnWidth(int viewWidth) {

        if (viewWidth == 0) {
            return 0;
        }

        if (columnCount == -1 && columnWidth == -1) {
            throw new IllegalStateException("need to set column count or columnWidth");
        }

        if (columnWidth > 0) {
            return (int) columnWidth;
        } else {
            //int hPadding = getPaddingLeft() + getPaddingRight();
            return (int) Math.floor((viewWidth - (columnCount * columnGap)) / columnCount);
        }
    }

    @Override
    public void addView(View child) {
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        ViewGroup.LayoutParams p = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return new LayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }


    public void setText(CharSequence text) {
        this.text = text;
        TextView v = new TextView(this.getContext());
        if (getParagraphStyle() != -1) {
            v.setTextAppearance(getContext(), getParagraphStyle());
        }
        columnTextLayout = new ColumnTextLayout(text, v.getPaint());
    }

    public int getParagraphStyle() {
        return paragraphStyle;
    }

    public void setParagraphStyle(int paragraphStyle) {
        this.paragraphStyle = paragraphStyle;
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {

        public static final int ALIGN_PARENT_BOTTOM = 1;
        public static final int ALIGN_PARENT_TOP = 2;

        private static final int COLUMN = R.styleable.ColumnLayout_Layout_layout_column;
        private static final int COLUMN_SPAN = R.styleable.ColumnLayout_Layout_layout_columnSpan;
        private static final int COLUMN_NO_MARGIN = R.styleable.ColumnLayout_Layout_layout_noTopMargin;
        private int alignment;
        public int columnIndex;
        private int columnsSpan = 1;
        private static final int DEFAULT_COLUMN = 0;
        private static final int DEFAULT_COLUMN_SPAN = 1;
        private boolean noTopMargin;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            init(c, attrs);
        }

        public LayoutParams(int w, int h) {
            super(w, h);

        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        private void init(Context context, AttributeSet attrs) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColumnLayout_Layout);
            try {
                columnIndex = a.getInt(COLUMN, DEFAULT_COLUMN);
                columnsSpan = a.getInt(COLUMN_SPAN, DEFAULT_COLUMN_SPAN);
                noTopMargin = a.getBoolean(COLUMN_NO_MARGIN, false);
            } finally {
                a.recycle();
            }
        }

        public void setColumn(int index) {
            this.columnIndex = index;
        }

        public void setColumnSpan(int columns) {
            this.columnsSpan = columns;
        }

        public void alignBottom() {
            alignment = ALIGN_PARENT_BOTTOM;
        }

        public void setAlignment(int alignment) {
            if (alignment == ALIGN_PARENT_BOTTOM) {
                alignBottom();
            }
        }
    }

    final class Page extends ViewGroup {

        private Page next;

        private Column column;

        public Page(Context context, Column column) {
            super(context);
            this.column = column;
        }

        @Override
        public void onLayout(boolean changed, int left, int top, int right, int bottom) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                column.layout(getChildAt(i), left, top, right, bottom);
            }
        }
    }

    Page page;

    public final class Column {
        private final int index, width, height, gap;

        private final MarginLayoutParams params;

        public int measuredUsedHeight, layoutCurrentBottom;

        private int offsetTop, offsetBottom, currentTop;

        boolean filled = false;

        /**
         * Keep track of the column to the left to ensure spanning height is pushed to its siblings
         */
        private Column next;

        Column(int index, int width, int height, int gap, MarginLayoutParams params) {
            this.index = index;
            this.width = width;
            this.height = height;
            this.gap = gap;
            this.params = params;
        }

        public boolean hasSpaceForLayout() {
            if (filled) return false;
            return getAvailableHeight() > columnTextLayout.getTextHeight();
        }

        public void fillIn(ColumnTextLayout with) {
            if (with != null && with.hasNext()) {
                if (hasSpaceForLayout()) {
                    append(with);
                } else {
                    getNext().fillIn(with);
                }
            }
        }

        private void append(ColumnTextLayout with) {
            novoda.widget.layout.Column c = with.next(width, getAvailableHeight());
            int l = c.getText().length();

            TextView view = new TextView(getContext());

            if (getParagraphStyle() != -1) {
                view.setTextAppearance(getContext(), getParagraphStyle());
            }

            view.setText(c.getText());
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setColumn(index);
            addView(view, params);

            getNext().fillIn(with);
        }

        /**
         * @return the available height for a view to draw in
         */
        public int getAvailableHeight() {
            return height - measuredUsedHeight - ((measuredUsedHeight == 0) ? (this.params.topMargin + this.params.bottomMargin) : 0);
        }

        public void measure(View child) {

            LayoutParams params = getLayoutParams(child);

            if (params.columnIndex == index) {
                if (measuredUsedHeight == 0) {
                    measuredUsedHeight += this.params.topMargin;
                }
                int spannedWidth = width * params.columnsSpan + (params.columnsSpan - 1) * gap;

                final int columnWidthSpec = makeMeasureSpec(spannedWidth, MeasureSpec.AT_MOST);
                int widthSpec = makeMeasureSpec(columnWidthSpec, MeasureSpec.AT_MOST);
                int heightSpec = makeMeasureSpec(getMeasuredHeight(), MeasureSpec.AT_MOST);

                if (params.height == LayoutParams.FILL_PARENT) {
                    filled = true;
                    heightSpec = makeMeasureSpec(height - measuredUsedHeight, MeasureSpec.EXACTLY);
                }

                int childWidthSpec = getChildMeasureSpec(MeasureSpec.AT_MOST, 0, widthSpec);
                int childHeightSpec = getChildMeasureSpec(MeasureSpec.AT_MOST, 0, heightSpec);

                if (params.height == LayoutParams.FILL_PARENT) {
                    childHeightSpec = getChildMeasureSpec(MeasureSpec.UNSPECIFIED, 0, heightSpec);
                }

                child.measure(childWidthSpec, childHeightSpec);


                measuredUsedHeight += child.getMeasuredHeight();
                measuredUsedHeight += getPaddingBottom();
                measuredUsedHeight += params.bottomMargin;
                measuredUsedHeight += params.topMargin;

                if (params.columnsSpan > 1) {
                    spanMeasuredRight(params.columnsSpan, child.getMeasuredHeight(), params);
                }
            } else {
                getNext().measure(child);
            }
        }

        private int getMeasuredHeight() {
            return height - measuredUsedHeight - ((isTopOnPage()) ? this.params.topMargin : 0);
        }


        private void spanMeasuredRight(int spansNbColumn, int measuredHeight, LayoutParams params) {
            if (spansNbColumn == 1) {

            } else {

//                if (params.alignment == LayoutParams.ALIGN_PARENT_BOTTOM) {
//                      getNext().offsetBottom = measuredHeight;
//                } else {

                if (params.height == LayoutParams.FILL_PARENT) {
                    getNext().filled = true;
                }
                getNext().measuredUsedHeight += measuredHeight;
                getNext().spanMeasuredRight(spansNbColumn - 1, measuredHeight, params);

//                }
            }
        }

        public void layout(View child, int l, int t, int r, int b) {
            LayoutParams params = getLayoutParams(child);
            if (params.columnIndex == index) {


                int left = this.getLeft();
                /**
                 * Takes into account spanning over several columns
                 */
                int right = left + (width * params.columnsSpan) + (params.columnsSpan - 1) * gap;

                int top = this.getTop(t);

                if (params.noTopMargin) {
                    top -= this.params.topMargin;
                }


                //t + getPaddingTop() + layoutCurrentBottom + this.params.topMargin;
                int bottom = b - getPaddingBottom();

                int bottomComputed = 0;
                int topComputed = 0;

                if (params.alignment == LayoutParams.ALIGN_PARENT_BOTTOM) {
                    bottomComputed = bottom;
                    topComputed = bottomComputed - child.getMeasuredHeight();
                    offsetBottom = top;
                } else {
                    if (params.height == LayoutParams.FILL_PARENT) {
                        bottomComputed = bottom;
                    } else {
                        bottomComputed = top + child.getMeasuredHeight();
                    }
                    topComputed = top;
                }

                //if (params.columnIndex == 3) {
                Log.i("TEST", " Layout with =====> " + topComputed + " " + bottomComputed + " " + params.bottomMargin + " <");

                int width = 0;//getWidth();
//                if  (params.columnIndex == 3) {
//                    width += getWidth()/3;
//                }
                child.layout(left + width, topComputed, right + width, bottomComputed);
                //}

                if (params.alignment == LayoutParams.ALIGN_PARENT_BOTTOM) {

                } else {
                    offsetTop = bottomComputed + params.bottomMargin;
                }

                if (params.columnsSpan > 1) {
                    spanRight(params.columnsSpan, offsetTop, params);
                }
            } else {
                getNext().layout(child, l, t, r, b);
            }
        }

        /**
         * Taking care of the spanning across the columns for layout.
         *
         * @param spansNbColumn
         * @param howMuch
         * @param params
         */
        private void spanRight(int spansNbColumn, int howMuch, LayoutParams params) {
            if (spansNbColumn > 1) {
                Column next = getNext();
                if (params.alignment == LayoutParams.ALIGN_PARENT_BOTTOM) {
                    next.offsetBottom = howMuch;
                } else if (params.height == LayoutParams.FILL_PARENT) {
                    //next.offsetTop = 0;
                } else {
                    next.offsetTop = howMuch;
                    next.spanRight(spansNbColumn - 1, howMuch, params);
                }
            }
        }

        public Column getNext() {
            if (next == null) {
                next = new Column(index + 1, width, height, gap, params);
            }
            return next;
        }

        public int getComputedColumnCount() {
            if (next != null) {
                return next.getComputedColumnCount();
            } else {
                return index;
            }
        }

        public int getTop(int top) {
            return top + getPaddingTop() + offsetTop + ((offsetTop == 0) ? this.params.topMargin : 0);
        }

        public int getLeft() {
            getPageMarginLeft();
            int left = 0;
            int nbPage = getPage();

            // add left padding - 1 time per page
            left += getPageMarginLeft();

            // add width times the previous column width
            left += (index * width);

            // same goes with gap
            left += (index * gap) - (getPage() - 1) * gap;
            return left;
        }

        // 1 indexed
        public int getPage() {
            double v = columnCount;
            double i = index;
            return (int) Math.floor(i / v) + 1;
        }

        private int getPageMarginLeft() {
            float width = getColumnWidth();
            float gap = getColumnGap();
            int columnCount = getColumnCount();
            int marginLeft = (int) (fullWidth - (columnCount * width) - ((columnCount - 1) * gap)) / 2;
            return getPage() * marginLeft + (getPage() - 1) * marginLeft;
        }

        private boolean isLeftOnPage() {
            return false;
        }

        private boolean isRightOnPage() {
            return false;
        }

        boolean isTopOnPage = true;

        private boolean isTopOnPage() {
            try {
                return isTopOnPage;//&& (offsetTop == 0);
            } finally {
                isTopOnPage = false;
            }
        }

        private boolean isBottomOnPage() {
            return false;
        }

    }

    private int getPageMarginRight() {
        return -1;
    }
}

