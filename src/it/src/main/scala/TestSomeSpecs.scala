import android.graphics.Bitmap
import android.os.Environment
import android.test.ActivityInstrumentationTestCase2
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import java.io.{FileOutputStream, File}
import java.util.concurrent.CountDownLatch
import novoda.android.test.ViewMatchers

import novoda.widget.{TextLayoutUtil, ActivityStub}
import org.scalatest.matchers.{MatchResult, ShouldMatchers}
import org.scalatest.WordSpec


// http://www.scalatest.org/scaladoc/1.7.1/#org.scalatest.WordSpec@AfterWords
class TestSomeSpec extends ViewTestCase with WordSpec with ShouldMatchers {

  import ViewMatchers._

  "A Columnist article" should {
    "have a title" in {

      super.setUp()
      val t = new TextView(getActivity);
      val lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
      val t2 = new TextView(getActivity);
      t.setText("Hello World")
      t2.setText("hekll ");

      val latch = new CountDownLatch(1)
      runTestOnUiThread {
        () =>
          getActivity.setContentView(t, lp)
          getActivity.addContentView(t2, lp)
          latch.countDown()
      }
      t2 should be(below(t))
      latch.await()
      import novoda.android.test.RichView._
      Log.i("TEST", t2.toS + " " + t.toS)
      Thread.sleep(5000)
      "title" should be("title")
      super.tearDown()
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
    val mPath = Environment.getExternalStorageDirectory + "/" + "test.png";

    getActivity.getWindow.getDecorView.getRootView
    val v1 =  getActivity.getWindow.getDecorView.getRootView;
    v1.setDrawingCacheEnabled(true);
    val bitmap = Bitmap.createBitmap(v1.getDrawingCache());
    v1.setDrawingCacheEnabled(false);
    val imageFile = new File(mPath);
    val fout = new FileOutputStream(imageFile);
    bitmap.compress(Bitmap.CompressFormat.PNG, 90, fout);
    fout.flush();
    fout.close();
  }
}


abstract class ViewTestCase
  extends ActivityInstrumentationTestCase2[ActivityStub](classOf[ActivityStub])
  with TextLayoutUtil