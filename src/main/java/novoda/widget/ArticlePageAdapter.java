package novoda.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ArticlePageAdapter extends PagerAdapter {

    private Context context;

    private ColumnLayout layout;

    public ArticlePageAdapter(Context context, ColumnLayout layout) {
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return true;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (position == 0) {
            container.removeViewInLayout(layout);
            container.addView(layout);
        } else if (position == 2) {
//            ImageView im = new ImageView (context);
//            im.setImageResource(R.drawable.image);
//
//            ViewGroup.MarginLayoutParams p = new ViewGroup.MarginLayoutParams(im.getWidth(), im.getHeight());
//            p.setMargins(502,0,0,0);
//            container.addView(im, p);
            //container.removeViewInLayout(layout);
            //layout.
//            ColumnLayout l = new ColumnLayout(context);
//            l.setLayoutParams(layout.getLayoutParams());
//            for (int i = 0; i < layout.getChildCount(); i ++) {
//                l.addView(layout.getChildAt(i));
//            }
//
//
//            container.addView(l);
        }
        return new TextView(context); // super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container, position, object);
    }
}
