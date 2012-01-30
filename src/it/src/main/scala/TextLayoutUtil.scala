package novoda.widget

import android.graphics.{Typeface, Paint}
import android.text._
import android.text.style.TypefaceSpan

trait TextLayoutUtil {

  def compute(s: String = "a") = {
    val paint = new Paint();
    paint.setAntiAlias(true);
    paint.setTypeface(Typeface.MONOSPACE);
    val p = new TextPaint(paint);
    val span = new SpannableStringBuilder(s.substring(0, 1));
    val span2 = new SpannableStringBuilder("a");
    val desiredWidth = Layout.getDesiredWidth(span2, p)
    (scala.math.ceil(desiredWidth), scala.math.ceil(p.getFontSpacing))
  }


  def ■(s: String): CharSequence = {
    val paint = new Paint();
    paint.setAntiAlias(true);
    paint.setTypeface(Typeface.MONOSPACE);
    val p = new TextPaint(paint);
    val span = new SpannableStringBuilder(s);
    span.setSpan(new TypefaceSpan("monospace"), 0, s.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    span
  }
}