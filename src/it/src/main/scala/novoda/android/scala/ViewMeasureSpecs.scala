package novoda.android.scala

import android.test.ActivityInstrumentationTestCase2
import novoda.android.test.ViewMatchers
import novoda.widget.tests.ActivityStub
import novoda.widget.{ColumnLayout, TextLayoutUtil}
import android.view.ViewGroup.LayoutParams
import java.util.concurrent.{TimeUnit, CountDownLatch}
import org.scalatest.matchers.ShouldMatchers
import android.widget.ImageView
import org.scalatest.WordSpec
import android.graphics.BitmapFactory
import android.view.ViewGroup

class ViewMeasureSpec extends ActivitySpec {

  "A view" should {
    "consider padding, margin when measuring" in {

      val view = new ImageView(getActivity);
      val bitmap = BitmapFactory.decodeStream(getInstrumentation.getContext.getAssets.open("image_400_200.png"));
      view.setImageBitmap(bitmap);
      view.setPadding(0, 15, 0, 15);
      val lp = new ViewGroup.MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
      val latch = new CountDownLatch(1)
      runTestOnUiThread(() => {
        getActivity.setContentView(view, lp)
        latch.countDown()
      });
      Thread.sleep(1000)
      latch.await(10, TimeUnit.SECONDS)
      view.getHeight should be(230)
      view.getMeasuredHeight should be(230)

      lp.setMargins(0, 25, 0, 25);
      val latch2 = new CountDownLatch(1)
      runTestOnUiThread(() => {
        getActivity.setContentView(view, lp)
        latch2.countDown()
      });
      Thread.sleep(1000)
      latch2.await(10, TimeUnit.SECONDS)
      view.getMeasuredHeight should be(230)
      view.getHeight should be(230)
    }
  }

}

class ActivitySpec extends ActivityInstrumentationTestCase2[ActivityStub](classOf[ActivityStub]) with TextLayoutUtil with WordSpec with ShouldMatchers with ViewMatchers
