package novoda.widget.layout;

/**
 * Created by IntelliJ IDEA.
 * User: Carl-Gustaf Harroch (carl@novoda.com)
 * Date: 19/03/12
 * Time: 10:28
 */
public interface LayoutDispatcher {
    void onLayout(boolean changed, int left, int top, int right, int bottom);
}
