import android.content.Context
import android.graphics.BitmapFactory
import android.test.ActivityInstrumentationTestCase2
import android.util.Log
import android.view.ViewGroup.LayoutParams
import android.view.{View, ViewGroup}
import android.widget.{RelativeLayout, ImageView}
import java.util.concurrent.{TimeUnit, CountDownLatch}
import novoda.widget.tests.R
import junit.framework.Assert._
import novoda.widget.{ActivityStub, ColumnLayout2, TextLayoutUtil}

class ColumnLayoutTest extends ActivityInstrumentationTestCase2[ActivityStub](classOf[ActivityStub]) with TextLayoutUtil {

  def context: Context = {
    getActivity.getApplicationContext
  }

  class Screen(d: View) {
    def height = d.getHeight

    def width = d.getWidth
  }

  def screen: Screen = {
    lazy val d = getActivity.getWindow.getDecorView.asInstanceOf[ViewGroup].getChildAt(0).asInstanceOf[ViewGroup].getChildAt(2)
    new Screen(d)
  }

  def `test should compute correct width` {
    val layout = new ColumnLayout2(context);
    layout.setText(4.columns(400, screen.height))
    layout.setColumnWidth(400)
    layout.setColumnGap(10)
    val lp = new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
    val latch = new CountDownLatch(1)
    runTestOnUiThread(() => {
      getActivity.setContentView(layout, lp)
      latch.countDown()
    });

    latch.await(10, TimeUnit.SECONDS)
    Thread.sleep(10000);
    assertEquals(screen.height, layout.getHeight)
    assertEquals(4 * 400 + 4 * 10, layout.getWidth)
  }

  def `test should handle column span` {
    val imageView = new ImageView(context)
    //imageView.setImageResource(R.drawable.image_400_200)

    val in = getInstrumentation.getContext.getAssets.open("image_400_200.png");
    val b = BitmapFactory.decodeStream(in)
    imageView.setImageBitmap(b)

    val ivl = new ColumnLayout2.LayoutParams(
      ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
    )

    ivl.addRule(RelativeLayout.ALIGN_PARENT_TOP)
    ivl.addRule(ColumnLayout2.COLUMN, 1);

    val layout = new ColumnLayout2(context);
    layout.setText(4.columns(400, screen.height))
    layout.setColumnWidth(400)
    layout.setColumnGap(10)
    layout.addView(imageView, ivl)
    val lp = new ViewGroup.LayoutParams(
      ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT
    );

    val latch = new CountDownLatch(1)
    runTestOnUiThread(() => {
      getActivity.setContentView(layout, lp)
      latch.countDown()
    });
    latch.await(10, TimeUnit.SECONDS)
    Thread.sleep(10000);
  }
}

