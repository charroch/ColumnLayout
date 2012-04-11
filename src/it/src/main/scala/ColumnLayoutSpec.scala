import android.graphics.{BitmapFactory, Bitmap}
import android.test.AndroidTestCase
import android.text._
import android.text.style.ImageSpan
import android.util.Log
import novoda.widget.layout.ColumnTextLayout
import novoda.widget.TextLayoutUtil
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.WordSpec

class ColumnTextLayoutSpec extends AndroidSpec with WordSpec with ShouldMatchers with TextLayoutUtil {

  "A Columnist layout" should {


    "take into account pictures" in {
      val builder = new SpannableStringBuilder()
      builder.append("\u25A0")
      builder.setSpan(new ImageSpan(bitmap("image_400_200.png")), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
      builder.append('\n')

      val cl = new ColumnTextLayout(builder, new TextPaint);
      cl.hasNext should be(true)

      val column = cl.next(100, 100)
      column.getText should not be ("\u25A0");

      // cl.next(5000, 10000).getText should be ("\u25A0")
    }

    "hum" in {
      val builder = new SpannableStringBuilder()
      val text = "hello world"
      builder.append(text)
      builder.append("\u25A0")
      builder.setSpan(new ImageSpan(bitmap("image_400_200.png")), text.length, text.length + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
      val cl = new ColumnTextLayout(builder, new TextPaint);

      Log.i("TEST", builder.toString)
      val c1 = cl.next(410, 30)
      val c2 = cl.next(410, 500)

      Log.i("TEST", " c1 " + c1.getText.toString)
      Log.i("TEST", " c2 " + c2.getText.toString)


      c1.getText.toString should not include ("■")
      c2.getText should be("\u25A0")
    }
  }


  def bitmap(file: String): Bitmap = {
    BitmapFactory.decodeStream(getContext.getAssets().open(file));
  }

}

trait AndroidSpec extends AndroidTestCase
