package novoda.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.MeasureSpec.makeMeasureSpec;


public class ColumnLayout extends ViewGroup {

    private int columnCount;
    private int columnWidth;
    private int columnGap;

    private List<Column> columns;

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

    public int getColumnGap() {
        return columnGap;
    }

    public void setColumnGap(int columnGap) {
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
        columns = new ArrayList<Column>();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColumnLayout);
        try {
            setColumnCount(a.getInt(R.styleable.ColumnLayout_minColumnCount, 1));
            setColumnGap(a.getInt(R.styleable.ColumnLayout_columnGap, 0));
            setColumnWidth(a.getInt(R.styleable.ColumnLayout_columnWidth, 0));
        } finally {
            a.recycle();
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int columnWidth = computeColumnWidth(width);
        int columnCount = computeColumnCount();

        int m = makeMeasureSpec(columnWidth, MeasureSpec.AT_MOST);

        // TODO why we need to measure children first?
        // measureChildren(m, heightMeasureSpec);

        for (int i = 0; i < computeColumnCount(); i++) {
            measureChildrenInColumn(i, widthMeasureSpec, heightMeasureSpec);
        }

        measureChildren(m, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }


    SparseArray<View> spans = new SparseArray<View>();

    private void measureChildrenInColumn(final int column, final int widthMeasureSpec, final int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);
        final int columnWidth = computeColumnWidth(width);
        final int columnWidthSpec = makeMeasureSpec(columnWidth, MeasureSpec.AT_MOST);

        applyToChildren(new ViewFunctor() {

            private int occupiedHeight = 0;

            @Override
            public void apply(View child) {
                LayoutParams params = getLayoutParams(child);

                if (params.alignment == LayoutParams.ALIGN_PARENT_BOTTOM) {
                    Log.i("TEST", "should align bottom");
                }

                if (params.columnIndex == column) {

                    if (params.columnsSpan > 1) {
                        spans.put(params.columnIndex, child);
                    }


                    View v = spans.get(params.columnIndex - 1);
                    if (v != null) {
                        occupiedHeight += v.getMeasuredHeight();
                    }

                    int heightSpec = makeMeasureSpec(height - occupiedHeight, MeasureSpec.AT_MOST);
                    child.measure(columnWidthSpec, heightSpec);

                    Log.i("TEST", child + "should add: " + child.getMeasuredHeight() + " " + occupiedHeight + " " + height);

                    occupiedHeight += child.getMeasuredHeight();
                }
            }

        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.i("TEST", "hello world" + left + " " + top + " r" + right + " " + bottom);

        int width = Math.abs(left + right);
        int height = Math.abs(top + bottom);
        int columnWidth = computeColumnWidth(width);

        for (int i = 0; i < computeColumnCount(); i++) {
            layoutChildrenInColumn(i, changed, left, top, right, bottom);
        }
    }

    private void layoutChildrenInColumn(final int column, boolean changed, final int left, final int top, final int right, final int bottom) {

        int width = Math.abs(left + right);
        int height = Math.abs(top + bottom);
        final int columnWidth = computeColumnWidth(width);
        applyToChildren(new ViewFunctor() {

            private int currentTop = 0;

            private int currentBottom = bottom;

            @Override
            public void apply(View child) {
                LayoutParams params = getLayoutParams(child);
                if (params.columnIndex == column) {
                    int offset = params.columnIndex * columnWidth;
                    int spans2 = params.columnsSpan;
                    Log.i("TEST", child.getId() + " " + params.height + " H " + child.getMeasuredHeight());

                    boolean laid = false;
                    if (params.alignment == LayoutParams.ALIGN_PARENT_BOTTOM) {
                        int measuredHeight = child.getMeasuredHeight();
                        child.layout(offset, bottom - measuredHeight, offset + (columnWidth * spans2), bottom);
                        laid = true;
                        currentBottom -= measuredHeight;
                    }

                    View v = spans.get(params.columnIndex - 1);
                    if (v != null) {
                        Log.i("TEST", v + "should add: " + v.getMeasuredHeight());
                        currentTop += v.getMeasuredHeight();
                    }

                    int bottomComputed = currentTop + child.getMeasuredHeight();
                    if (params.height == LayoutParams.FILL_PARENT) {
                        bottomComputed = currentBottom;
                    }

                    if (!laid) {
                        child.layout(offset, currentTop, offset + (columnWidth * spans2), bottomComputed);
                        currentTop += child.getMeasuredHeight();
                    }
                }
            }
        });
    }

    private LayoutParams getLayoutParams(View c) {
        return (LayoutParams) c.getLayoutParams();
    }

    private int computeColumnWidth(int viewWidth) {
        if (columnCount == -1 && columnWidth == -1) {
            throw new IllegalStateException("need to set column count or columnWidth");
        }
        if (columnWidth > 0) {
            return columnWidth;
        } else {
            return (int) Math.floor((viewWidth - columnCount + columnGap) / columnCount);
        }
    }

    private int computeColumnCount() {
        int c = 0;
        for (int i = 0, N = getChildCount(); i < N; i++) {
            View v = getChildAt(i);

            int t = getLayoutParams(v).columnIndex;
            if (t > c) {
                c = t;
            }
        }
        return c + 1;
    }


    public static class LayoutParams extends ViewGroup.MarginLayoutParams {

        private static final int COLUMN_SPAN = 1;
        public static final int ALIGN_PARENT_BOTTOM = 1;
        public static final int ALIGN_PARENT_TOP = 2;

        private int alignment;
        private int columnIndex;
        private int columnsSpan = 1;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
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

        public void setColumn(int index) {
            this.columnIndex = index;
        }

        public void setColumnSpan(int columns) {
            this.columnsSpan = columns;
        }

        public void alignBottom() {
            alignment = ALIGN_PARENT_BOTTOM;
        }
    }

    interface Functor<T> {
        void apply(T data);
    }

    interface FilterFunctor<T, R> {
        R apply(T data);
    }

    interface ViewFunctor extends Functor<View> {
    }

    interface ViewFilterFunctor<R> extends FilterFunctor<View, R> {
    }

    private void applyToChildren(ViewFunctor functor) {
        for (int i = 0, N = getChildCount(); i < N; i++) {
            View v = getChildAt(i);
            functor.apply(v);
        }
    }


    private class Span {

    }

    private class Alignment {

    }

    private class Column {

        //private SparseArray<>

        private List<View> inColumn;

        private int topSpanPadding = 0;

        private int bottomSpanPadding = 0;

        private final int height;

        private Column(int height) {
            inColumn = new ArrayList<View>();
            bottomSpanPadding = height;
            this.height = height;
        }

        public int getTop(View view) {
            LayoutParams params = getLayoutParams(view);
            if (params.alignment == LayoutParams.ALIGN_PARENT_TOP) {
                return 0;
            } else {
                return topSpanPadding;
            }
        }

        public int getBottom(View view) {
            LayoutParams params = getLayoutParams(view);
            if (params.alignment == LayoutParams.ALIGN_PARENT_BOTTOM) {
                return height;
            } else {
                return bottomSpanPadding;
            }
        }

        public void add(int i, View view) {
            inColumn.add(i, view);
        }

        public boolean add(View view) {
            LayoutParams params = getLayoutParams(view);
            if (params.alignment == LayoutParams.ALIGN_PARENT_BOTTOM) {
                bottomSpanPadding -= view.getMeasuredHeight();
            } else if (params.alignment == LayoutParams.ALIGN_PARENT_TOP) {
                topSpanPadding += view.getMeasuredHeight();
            }
            return inColumn.add(view);
        }

        public boolean contains(Object o) {
            return inColumn.contains(o);
        }

        public View get(int i) {
            return inColumn.get(i);
        }

        public int indexOf(Object o) {
            return inColumn.indexOf(o);
        }

        public boolean isEmpty() {
            return inColumn.isEmpty();
        }
    }
}
