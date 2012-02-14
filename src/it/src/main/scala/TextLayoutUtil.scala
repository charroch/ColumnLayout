package novoda.widget

import android.graphics.{Typeface, Paint}
import android.text._
import android.text.style.TypefaceSpan
import android.util.Log

trait TextLayoutUtil {

  def compute(s: String = "a") = {
    val paint = new Paint();
    paint.setAntiAlias(true);
    paint.setTypeface(Typeface.MONOSPACE);
    val p = new TextPaint(paint);
    val span = new SpannableStringBuilder("a");
    val desiredWidth = Layout.getDesiredWidth(span, p)
    (scala.math.ceil(desiredWidth), scala.math.ceil(p.getFontSpacing))
  }

  def ■(s: String): CharSequence = {
    val paint = new Paint();
    paint.setAntiAlias(true);
    paint.setTypeface(Typeface.MONOSPACE);
    val span = new SpannableStringBuilder(s);
    span.setSpan(new TypefaceSpan("monospace"), 0, s.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    span
  }


  def >>(s: String): SpannableStringBuilder = {
    val paint = new Paint();
    paint.setAntiAlias(true);
    paint.setTypeface(Typeface.MONOSPACE);
    val span = new SpannableStringBuilder(s);
    span.setSpan(new TypefaceSpan("monospace"), 0, s.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    span
  }

  implicit def runnable(f: ()=> Unit): Runnable =
    new Runnable() {
      def run() = f()
    }

  class RichInt(i: Int) {
    def columns(width: Int, height: Int): CharSequence = {
      val (w, h) = compute("A")
      val nbPerLine = scala.math.floor(width / (w + 1))
      val linePerHeight = height / (h + 2)
      1.to(4).foldLeft(new SpannableStringBuilder())(
        (r: SpannableStringBuilder, letter: Int) => {
          var a = 'a'

          letter match {
            case 2 => a = 'b'
            case 3 => a = 'c'
            case 4 => a = 'd'
            case _ => a = 'a'
          }
          r.append(>>((a.toString * (nbPerLine.toInt * linePerHeight.toInt)).toString))
        }
      )
    }
  }

  implicit def int2RichInt(i: Int) = new RichInt(i)
}