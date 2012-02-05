package novoda.widget

import android.test.ActivityInstrumentationTestCase2
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import org.scalatest.junit.ShouldMatchersForJUnit
import java.util.concurrent.{TimeUnit, CountDownLatch}
import novoda.android.test.ViewMatchers


class ColumnLayoutTest extends ActivityInstrumentationTestCase2[ActivityStub](classOf[ActivityStub])
with TextLayoutUtil with ShouldMatchersForJUnit with ViewMatchers {

  import novoda.android.test.RichView._

  def `test simple text column` {
    implicit val context = getActivity.getApplicationContext
    val layout = new ColumnLayout(context)
    layout.setColumnCount(3)

    val v1: TextView = "hello world"
    val lp = new ColumnLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    lp.setColumn(0)

    val v2: TextView = "Lorem Ipsum"
    val lp2 = new ColumnLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    lp2.setColumn(1)


    layout.addView(v1, lp);
    layout.addView(v2, lp2);

    val latch = new CountDownLatch(1)
    runTestOnUiThread(() => {
      getActivity.setContentView(layout, new ColumnLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
      latch.countDown()
    });

    Thread.sleep(5000)
    latch.await(10, TimeUnit.SECONDS)

    v1 should be(left_of(v2))
  }

}