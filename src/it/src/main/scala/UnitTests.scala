package novoda.widget.tests

import _root_.android.test.AndroidTestCase
import android.text._

import style.TypefaceSpan
import android.graphics.{Typeface, Paint}
import junit.framework.Assert._
import android.util.Log
import novoda.widget.layout.ColumnTextLayout

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
    val simpleText = """aaa
                       |aaa""".stripMargin

    val column = new ColumnTextLayout(simpleText, p).next(Integer.MAX_VALUE, Integer.MAX_VALUE);
    val tl = new StaticLayout(simpleText, p, Integer.MAX_VALUE, Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, true);
    assertEquals(1, tl.getLineForVertical(Integer.MAX_VALUE));
    assertEquals(2, tl.getLineCount);
    assertEquals(tl.getText, simpleText);
    assertEquals(simpleText.length(), tl.getText.length());
    assertEquals(2, column.getLineCount);
    assertEquals(7, column.getText().length());
    assertEquals(7, column.getLastCharPosition());
  }

  def testMultiLineLayout {
    given(
      """
      |aooooo
      |oooooa
      |
      |boooooooooo
      |ooooooooooo
      |ooooooooooo
      |oooooooooob
      |
      |co
      |oo
      |oc
      """.stripMargin.trim.replace("\n", "")
    ) {
      (c: ColumnTextLayout, singleCharacterWidth: Int, lineHeight: Int) =>

        Log.i("TEST", "::: " + c.getText());

        val column = c.next(6 * singleCharacterWidth, (lineHeight * 2));
        val t = column.getText();
        assertEquals("should be " + t, 'a', t.charAt(0));
        assertEquals("should be " + t, 'a', t.charAt(t.length() - 1));
        assertEquals("A 5x2 column with single char lenght " + singleCharacterWidth + " and lineHeight " + lineHeight,
          column.getLineCount, 2)

        val b = c.next(11 * singleCharacterWidth, (lineHeight * 4));
        val tb = b.getText();
        assertEquals("should be " + tb, 'b', tb.charAt(0));
        assertEquals("should be " + tb, 'b', tb.charAt(tb.length() - 1));
        assertEquals(b.getLineCount, 4)

        val cc = c.next(2 * singleCharacterWidth, (lineHeight * 3));
        val tc = cc.getText();
        assertEquals("should be " + tc, 'c', tc.charAt(0));
        assertEquals("should be " + tc, 'c', tc.charAt(tc.length() - 1));
        assertEquals(cc.getLineCount, 3)
        
        assertFalse(c.hasNext())
    }
  }

  val column = "aaaa\naaaa"

  implicit val p = {
    val paint = new Paint();
    paint.setAntiAlias(true);
    paint.setTypeface(Typeface.MONOSPACE);
    new TextPaint(paint);
  }

  def compute(s: String) = {
    val paint = new Paint();
    paint.setAntiAlias(true);
    paint.setTypeface(Typeface.MONOSPACE);
    val p = new TextPaint(paint);
    val span = new SpannableStringBuilder("a");
    val desiredWidth = Layout.getDesiredWidth(span, p)
    (scala.math.ceil(desiredWidth), scala.math.ceil(p.getFontSpacing))
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