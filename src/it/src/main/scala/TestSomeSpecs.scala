import android.graphics.Color
import android.test.ActivityInstrumentationTestCase2
import android.util.Log
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
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

    "take a screenshot" in {
      val t = new TextView(getActivity);
      be(below(t))
    }

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
}

abstract class ViewTestCase
  extends ActivityInstrumentationTestCase2[ActivityStub](classOf[ActivityStub])
  with TextLayoutUtil