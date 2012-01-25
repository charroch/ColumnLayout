package novoda.widget.tests

import junit.framework.Assert._
import _root_.android.test.AndroidTestCase
import novoda.widget.layout.ColumnTextLayout
import android.text._

import style.{TypefaceSpan, StyleSpan, AbsoluteSizeSpan}
import android.graphics.{Typeface, Paint}

class UnitTests extends AndroidTestCase {

  val columns =
    """
    |aaaaaa
    |aaaaaa
    |
    |aaaaaaaaaaa
    |aaaaaaaaaaa
    |aaaaaaaaaaa
    |aaaaaaaaaaa
    |
    |aa
    |aa
    |aa
    """

  def testPackageIsCorrect {
    assertEquals("novoda.widget", getContext.getPackageName)
  }

  def testSingleLineLayout {
    val (s, ll, nbLine) = compute(column);
    val c = column.next(ll.toInt + 1, 100);
    assertEquals(3, c.getLineCount());
  }

  val column = """aaaa
                 |aaaa"""

  def compute(s: String) = {
    val paint = new Paint();
    paint.setAntiAlias(true);
    paint.setTypeface(Typeface.MONOSPACE);
    val p = new TextPaint(paint);
    val span = new SpannableStringBuilder(s.charAt(0).toString);
    span.setSpan(new TypefaceSpan("monospace"), 0, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    val dw = Layout.getDesiredWidth(span, p)
    val nbLine = s.split('\n').length
    val lineLength = s.split('\n')(0).length() * dw
    (s.filter(_ != '\n'), lineLength, nbLine)
  }

  implicit def string2charSequence(s: String): ColumnTextLayout = {
    val paint = new Paint();
    paint.setAntiAlias(true);
    paint.setTypeface(Typeface.MONOSPACE);
    val p = new TextPaint(paint);
    val span = new SpannableStringBuilder(s.substring(3));
    span.setSpan(new TypefaceSpan("monospace"), 0, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    new ColumnTextLayout(span, p)
  }
}