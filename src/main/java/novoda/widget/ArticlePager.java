package novoda.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;

public class ArticlePager extends ViewPager {

    public ArticlePager(Context context) {
        this(context, null);
    }

    public ArticlePager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i("TEST", "left: " + l + " right " + r);
        super.onLayout(changed, l, t, r, b);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Log.i("TEST", width + " " + height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);    //To change body of overridden methods use File | Settings | File Templates.
    }


}
