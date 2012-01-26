package novoda.widget.tests

import _root_.android.test.AndroidTestCase
import novoda.widget.layout.ColumnTextLayout
import android.text._

import style.TypefaceSpan
import android.graphics.{Typeface, Paint}
import junit.framework.Assert._
import android.util.Log

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
    """.stripMargin.trim

  def testPackageIsCorrect {
    assertEquals("novoda.widget", getContext.getPackageName)
  }

  def testSingleLineLayout {
    //    val (s, ll, nbLine) = compute(column);
    //    val c = column.next(ll.toInt, 250);
    //    assertEquals(2, c.getLineCount());
  }

  def testMultiLineLayout {
    given(
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
    ) {
      (c: ColumnTextLayout, singleCharacterWidth: Int, lineHeight: Int) =>

        assertEquals("A 5x2 column with single char lenght " + singleCharacterWidth + " and lineHeight " + lineHeight,
          c.next(5 * singleCharacterWidth, 20).getLineCount, 2)

        assertEquals(c.next(11 * singleCharacterWidth, 4 * lineHeight).getLineCount, 4)
        assertEquals(c.next(2 * singleCharacterWidth, 3 * lineHeight).getLineCount, 3)
      //c.hasNext false
    }
  }


  val column = "aaaa\naaaa"

  def compute(s: String) = {

    val paint = new Paint();
    paint.setAntiAlias(true);
    paint.setTypeface(Typeface.MONOSPACE);
    val p = new TextPaint(paint);

    val span = new SpannableStringBuilder(s.substring(0, 1));


    val height = new StaticLayout(span, p, 100, Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, true).getHeight

    val span2 = new SpannableStringBuilder("a");
    val desiredWidth = Layout.getDesiredWidth(span2, p)

    Log.i("TEST", " ================ > " +desiredWidth);
    //    val nbLine = s.split('\n').length
    //    val lineLength = s.split('\n')(0).length() * dw
    //    (s.filter(_ != '\n'), lineLength, nbLine)
    (desiredWidth, height)
  }

  implicit def string2charSequence(s: String): ColumnTextLayout = {
    val paint = new Paint();
    paint.setAntiAlias(true);
    paint.setTypeface(Typeface.MONOSPACE);
    val p = new TextPaint(paint);
    val span = new SpannableStringBuilder(s);
    new ColumnTextLayout(span, p)
  }

  private def given(s: String)(assertion: (ColumnTextLayout, Int, Int) => Any)(implicit s2c: String => ColumnTextLayout) {
    val (x, y) = compute(s)
    assertion(s2c(s), x.toInt, y.toInt)
  }
}