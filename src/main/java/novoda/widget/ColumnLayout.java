package novoda.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import static android.view.View.MeasureSpec.makeMeasureSpec;


public class ColumnLayout extends ViewGroup {

    private int columnCount;
    private int columnWidth;
    private int columnGap;

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

        for (int i = 0; i < computeColumnCount(); i++) {
            measureChildrenInColumn(i, widthMeasureSpec, heightMeasureSpec);
        }

        //setMeasuredDimension(width, height);

        int m = makeMeasureSpec(columnWidth, MeasureSpec.AT_MOST);
        measureChildren(m, heightMeasureSpec);
        setMeasuredDimension(width, height);

        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


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
                if (params.columnIndex == column) {
                    int heightSpec = makeMeasureSpec(height - occupiedHeight, MeasureSpec.AT_MOST);
                    child.measure(columnWidthSpec, heightSpec);
                    Log.i("TEST", occupiedHeight + "  " + child + " " + column + " " + child.getMeasuredHeight() + " measured for child");
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

            @Override
            public void apply(View child) {
                Log.i("TEST", "tioo " + top + " " + currentTop);
                LayoutParams params = getLayoutParams(child);
                if (params.columnIndex == column) {
                    int offset = params.columnIndex * columnWidth;
                    int spans = params.columnsSpan;   
                    Log.i("TEST",child.getId() + " " +params.height +  " H " + child.getMeasuredHeight());
                    child.layout(offset, currentTop, offset + (columnWidth * spans), currentTop + child.getMeasuredHeight());
                    currentTop += child.getMeasuredHeight();
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
}
