package novoda.widget.layout;

import android.content.Context;
import android.view.ViewGroup;

/**
 * Created by IntelliJ IDEA.
 * User: Carl-Gustaf Harroch (carl@novoda.com)
 * Date: 20/03/12
 * Time: 08:36
 */
public class Page extends ViewGroup {

    Column column;

    public Page(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
    }
}
