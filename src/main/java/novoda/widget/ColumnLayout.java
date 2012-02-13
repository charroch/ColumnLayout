package novoda.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import novoda.widget.layout.ColumnTextLayout;

import static android.view.View.MeasureSpec.makeMeasureSpec;

public class ColumnLayout extends ViewGroup {

    private int columnCount;
    private int columnWidth;
    private float columnGap;

    private Column first;

    private CharSequence text;
    private ColumnTextLayout columnTextLayout;
    private static final float DEFAULT_COLUMN_GAP = 25.0f;

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public int getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(int columnWidth) {
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
            setColumnWidth(a.getInt(R.styleable.ColumnLayout_columnWidth, 0));
        } finally {
            a.recycle();
        }
    }


    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        //super.onMeasure(widthSpec, heightSpec);
        int width = MeasureSpec.getSize(widthSpec);
        int height = MeasureSpec.getSize(heightSpec);

        int widthMode = MeasureSpec.getMode(widthSpec);


        int columnWidth = computeColumnWidth(width);

        first = new Column(0, columnWidth, height, (int) getColumnGap());

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
                resolveSizeAndState(layoutWidth, a, 0),
                resolveSizeAndState(measuredHeight, heightSpec, 0)
        );
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
            return columnWidth;
        } else {
            //int hPadding = getPaddingLeft() + getPaddingRight();
            return (int) Math.floor((viewWidth - (columnCount * columnGap)) / columnCount);
        }
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
        //v.setTextAppearance(getContext(), R.style.paragraph);
        columnTextLayout = new ColumnTextLayout(text, v.getPaint());
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {

        public static final int ALIGN_PARENT_BOTTOM = 1;
        public static final int ALIGN_PARENT_TOP = 2;

        private static final int COLUMN = R.styleable.ColumnLayout_Layout_layout_column;
        private static final int COLUMN_SPAN = R.styleable.ColumnLayout_Layout_layout_columnSpan;

        private int alignment;
        private int columnIndex;
        private int columnsSpan = 1;
        private static final int DEFAULT_COLUMN = 0;
        private static final int DEFAULT_COLUMN_SPAN = 1;

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

    public int getPageCount() {
        return -1;
    }

    public Page getPage(int index) {
        return null;
    }

    final class Page extends ViewGroup {

        public Page(Context context) {
            this(context, null);
        }

        public Page(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public Page(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        protected void onLayout(boolean isChanged, int left, int top, int right, int bottom) {
        }
    }

    final class Column {
        private final int index, width, height, gap;

        private int measuredUsedHeight, layoutCurrentBottom;

        private int offsetTop, offsetBottom;

        int nbViews = 0;

        /**
         * Keep track of the column to the left to ensure spanning height is pushed to its siblings
         */
        private Column next;

        Column(int index, int width, int height, int gap) {
            this.index = index;
            this.width = width;
            this.height = height;
            this.gap = gap;

        }

        public boolean hasSpaceForLayout() {
            // should add some spacing like line spacing?
            return (height - measuredUsedHeight - getPaddingBottom() - getPaddingTop() - 15) > 0;
            //return (measuredUsedHeight < height);
        }

        public void fillIn(ColumnTextLayout with) {
            if (with.hasNext()) {
                if (hasSpaceForLayout()) {
                    append(with);
                } else {
                    getNext().fillIn(with);
                }
            }
        }

        private void append(ColumnTextLayout with) {
            novoda.widget.layout.Column c = with.next(width, getAvailableHeight());
            TextView view = new TextView(getContext());
            //view.setTextAppearance(getContext(), novoda.widget.R.style.paragraph);
            view.setText(c.getText());
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setColumn(index);
            addView(view, params);

            getNext().fillIn(with);
        }

        private int getAvailableHeight() {
            return height - measuredUsedHeight - getPaddingBottom() - getPaddingTop();
        }

        public void measure(View child) {
            LayoutParams params = getLayoutParams(child);
            if (params.columnIndex == index) {

                int spannedWidth = width * params.columnsSpan + (params.columnsSpan - 1) * gap;

                final int columnWidthSpec = makeMeasureSpec(spannedWidth, MeasureSpec.AT_MOST);
                int widthSpec = makeMeasureSpec(columnWidthSpec, MeasureSpec.AT_MOST);
                int heightSpec = makeMeasureSpec(height - measuredUsedHeight, MeasureSpec.AT_MOST);

                if (params.height == LayoutParams.FILL_PARENT) {
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

                if (params.columnsSpan > 1) {
                    spanMeasuredRight(params.columnsSpan, child.getMeasuredHeight());
                }

                // TODO remove this crap
                nbViews += 1;
            } else {
                getNext().measure(child);
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

                int top = t + getPaddingTop() + layoutCurrentBottom;
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

                child.layout(left, topComputed, right, bottomComputed);
                if (params.alignment == LayoutParams.ALIGN_PARENT_BOTTOM) {

                } else {
                    layoutCurrentBottom = bottomComputed;
                }
                if (params.columnsSpan > 1) {
                    spanRight(params.columnsSpan, layoutCurrentBottom);
                }
            } else {
                getNext().layout(child, l, t, r, b);
            }
        }

        private void spanMeasuredRight(int spansNbColumn, int measuredHeight) {
            if (spansNbColumn == 1) {

            } else {
                getNext().measuredUsedHeight += measuredHeight;
                getNext().spanRight(spansNbColumn - 1, measuredHeight);
            }
        }

        private void spanRight(int spansNbColumn, int howMuch) {
            if (spansNbColumn == 1) {

            } else {
                getNext().layoutCurrentBottom = howMuch;
                getNext().spanRight(spansNbColumn - 1, howMuch);
            }
        }

        public Column getNext() {
            if (next == null) {
                next = new Column(index + 1, width, height, gap);
            }
            return next;
        }

        private int getPotentialRightPadding() {
            return getPaddingRight() * index / columnCount;
        }

        public int getComputedWith(int initialWidth) {
            if (next != null) {
                return next.getComputedWith(width + initialWidth);
            } else {
                return width + initialWidth;
            }
        }

        public int getComputedColumnCount() {
            if (next != null) {
                return next.getComputedColumnCount();
            } else {
                return index;
            }
        }

        public int getLeft() {
            int left = 0;
            int nbPage = getPage();

            // add left padding - 1 time per page
            left += getPaddingLeft();

            // add width times the previous column width
            left += (index * width);

            // same goes with gap
            left += (index * gap);
            return left;
        }

        // 1 indexed
        public int getPage() {
            double v = columnCount;
            double i = index;
            return (int) Math.floor(i / v) + 1;
        }
    }
}
