import android.graphics.Bitmap
import android.os.Environment
import android.test.ActivityInstrumentationTestCase2
import android.view.View
import android.widget.TextView
import java.io.{FileOutputStream, File}
import novoda.android.test.ViewMatchers
import novoda.widget.{TextLayoutUtil, ActivityStub}
import org.scalatest.matchers.{MatchResult, ShouldMatchers}
import org.scalatest.WordSpec


// http://www.scalatest.org/scaladoc/1.7.1/#org.scalatest.WordSpec@AfterWords
class TestSomeSpec extends ViewTestCase with WordSpec with ShouldMatchers {

  import ViewMatchers._

  "A Columnist article" should {
    "have a title" in {

      val t = new TextView(getActivity);
      runTestOnUiThread {
        () =>
          t.setText("Hello World")
          getActivity.setContentView(t)
          t should be(parent_aligned(top))
      }
      take(t)
      "title" should be("title")
    }

    "have a header" in {
      "title" should be("title")
    }

    "have images" in {
      "title" should be("title")
    }
  }

  def withScreenshot(file: String)(f: => MatchResult) {

  }


  def take(v: View) {
    val mPath = "/data/data/novoda.widget.tests/" + "test.png";

    val v1 = v.getRootView();
    v1.setDrawingCacheEnabled(true);
    val bitmap = Bitmap.createBitmap(v1.getDrawingCache());
    v1.setDrawingCacheEnabled(false);
    val imageFile = new File(mPath);
    val fout = new FileOutputStream(imageFile);
    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
    fout.flush();
    fout.close();
  }
}


abstract class ViewTestCase
  extends ActivityInstrumentationTestCase2[ActivityStub](classOf[ActivityStub])
  with TextLayoutUtil