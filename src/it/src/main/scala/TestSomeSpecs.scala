import android.graphics.{Color, Bitmap}
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

trait Screenshot {
  suite: org.scalatest.Suite =>
  def t() {
    suite.suiteName
  }

  def withScreenshot(f: MatchResult) {

  }
}
// http://www.scalatest.org/scaladoc/1.7.1/#org.scalatest.WordSpec@AfterWords
class TestSomeSpec extends ViewTestCase with WordSpec with ShouldMatchers with Screenshot {

  import ViewMatchers._

  "A Columnist article" should {
    "have a title" in {

      super.setUp()
      val t = new TextView(getActivity);
      t.setBackgroundColor(Color.RED)
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

    //  withScreenshot {
        t2 should be(below(t))
     // }

      take(t2)
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



  def take(v: View) {

    val mPath = Environment.getExternalStorageDirectory + "/" + "test2.png";


    getActivity.getWindow.getDecorView.getRootView
    val v1 = v.getRootView;
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