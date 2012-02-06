package novoda.widget

import android.test.ActivityInstrumentationTestCase2
import android.view.ViewGroup.LayoutParams
import org.scalatest.junit.ShouldMatchersForJUnit
import java.util.concurrent.{TimeUnit, CountDownLatch}
import novoda.android.test.ViewMatchers
import android.widget.{ImageView, TextView}
import android.content.Context
import android.widget.ImageView.ScaleType


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
    v2 should be(right_of(v1))
  }

  def `test 1 columns AND 1 image spanning 2 columns` {
    val (layout, textView, imageView) = one_columns_1_image_spanning_2_columns
    val latch = new CountDownLatch(1)
    runTestOnUiThread(() => {
      getActivity.setContentView(
        layout,
        new ColumnLayout.LayoutParams(
          LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
      )
      latch.countDown()
    });

    Thread.sleep(5000)
    latch.await(10, TimeUnit.SECONDS)

    textView should be(left_of(imageView))
    imageView should be(parent_aligned(top))
    imageView should be(parent_aligned(right))
    imageView should be(parent_aligned(bottom))
  }

  def `test 1 image AND 2 text columns` {
    val (layout, textView, imageView) = one_image_spanning_2_AND_2_columns_text
    val latch = new CountDownLatch(1)
    runTestOnUiThread(() => {
      getActivity.setContentView(
        layout,
        new ColumnLayout.LayoutParams(
          LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
      )
      latch.countDown()
    });

    Thread.sleep(5000)
    latch.await(10, TimeUnit.SECONDS)

    textView should be(left_of(imageView))
    imageView should be(parent_aligned(top))
    imageView should be(parent_aligned(right))
    imageView should be(parent_aligned(bottom))
  }

  def `test 1 image AND 2 text columns above` {
    val (layout, textView, imageView) = one_image_spanning_2_AND_2_columns_text_above
    val latch = new CountDownLatch(1)
    runTestOnUiThread(() => {
      getActivity.setContentView(
        layout,
        new ColumnLayout.LayoutParams(
          LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
      )
      latch.countDown()
    });

    Thread.sleep(5000)
    latch.await(10, TimeUnit.SECONDS)

    textView should be(left_of(imageView))
    imageView should be(parent_aligned(top))
    imageView should be(parent_aligned(right))
    imageView should be(parent_aligned(bottom))
  }

  lazy val one_columns_1_image_spanning_2_columns = {
    implicit val context = getActivity.getApplicationContext
    val layout = new ColumnLayout(context)
    layout.setColumnCount(3)

    val v1: TextView = ipsum
    val lp = new ColumnLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    lp.setColumn(0)

    val imageLayout = new ColumnLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT)
    imageLayout.setColumn(1)
    imageLayout.setColumnSpan(2)
    val img = image(500, 500)
    img.setScaleType(ScaleType.FIT_XY)
    layout.addView(v1, lp)
    layout.addView(img, imageLayout)
    (layout, v1, img)
  }

  lazy val one_image_spanning_2_AND_2_columns_text = {
    implicit val context = getActivity.getApplicationContext
    val layout = new ColumnLayout(context)
    layout.setColumnCount(2)

    val v1: TextView = ipsum
    val lp = new ColumnLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    lp.setColumn(0)

    val v2: TextView = ipsum
    val lp2 = new ColumnLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    lp2.setColumn(1)

    val imageLayout = new ColumnLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 200)
    imageLayout.setColumn(0)
    imageLayout.setColumnSpan(2)
    imageLayout.alignBottom();

    val img = image(500, 500)
    img.setScaleType(ScaleType.FIT_XY)
    layout.addView(img, imageLayout)
    layout.addView(v1, lp)
    layout.addView(v2, lp2)

    (layout, v1, img)
  }

  lazy val one_image_spanning_2_AND_2_columns_text_above = {
    implicit val context = getActivity.getApplicationContext
    val layout = new ColumnLayout(context)
    layout.setColumnCount(2)

    val v1: TextView = ipsum
    val lp = new ColumnLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    lp.setColumn(0)

    val v2: TextView = ipsum
    val lp2 = new ColumnLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    lp2.setColumn(1)

    val imageLayout = new ColumnLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 200)
    imageLayout.setColumn(0)
    imageLayout.setColumnSpan(2)

    val img = image(500, 500)
    img.setScaleType(ScaleType.FIT_XY)
    layout.addView(v1, lp)
    layout.addView(v2, lp2)
    layout.addView(img, imageLayout)

    (layout, v1, img)
  }

  lazy val ipsum =
    """Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in adipiscing sapien. Nunc id diam nec purus dictum pretium. Proin pharetra tempor nisi eget fringilla. Phasellus sollicitudin lorem eu odio dictum tincidunt. Nunc vel est vitae elit vulputate congue nec in mauris. In leo metus, varius in sollicitudin eu, egestas vel mi. Maecenas semper pellentesque felis vitae rhoncus. Sed ultricies ornare nunc ac ultrices. Sed tincidunt, nulla sit amet porta feugiat, enim libero euismod massa, quis lobortis diam sem sed ante. Donec ultricies lacus quis diam euismod mollis.
    |Curabitur at dolor nulla. Proin dui turpis, sodales ac facilisis vel, vestibulum ac dui. Etiam varius sagittis dolor, semper aliquam arcu dictum sed. Sed in nibh at urna posuere interdum in eget nulla. Maecenas vulputate magna eget eros euismod nec venenatis mi bibendum. Nullam non imperdiet quam. Vivamus aliquam tincidunt turpis, in euismod risus adipiscing in.
    |Mauris placerat consectetur dolor auctor euismod. Donec placerat pretium sem nec lobortis. Cras semper, justo in cursus viverra, felis enim gravida sapien, at pellentesque felis arcu eu augue. Duis tellus sapien, congue sit amet convallis eu, mollis condimentum eros. Donec lorem metus, lacinia quis sodales vitae, elementum at nibh. Morbi dapibus, mauris a pharetra scelerisque, orci ante adipiscing metus, non venenatis odio risus id felis. Nam id interdum tellus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Maecenas elementum venenatis nibh vel dictum.
    |Curabitur a purus justo, vitae dictum lacus. Quisque sed justo nec orci vestibulum tristique. Phasellus pharetra cursus aliquet. Nam vel volutpat erat. Donec mattis egestas felis eget tempor. Donec iaculis lectus sit amet augue dictum a euismod libero facilisis. Sed id nunc id tellus condimentum gravida.
    |Morbi iaculis iaculis est, id volutpat lorem viverra auctor. Donec urna tellus, dignissim vel pellentesque sed, eleifend nec diam. Vivamus condimentum justo tellus, eu dignissim nulla. Nam sed urna dapibus lorem vulputate tincidunt ac vitae lacus. Fusce eu risus nibh. Suspendisse fringilla accumsan nulla sed sagittis. Cras in tortor nec elit blandit semper non sed ante. Phasellus tellus risus, volutpat sed vehicula ac, congue id ligula. Nulla facilisi. Duis sed elit elit, a commodo dolor. Morbi accumsan felis sit amet arcu varius sed eleifend mauris fermentum.
    """.stripMargin

  def image(height: Int, width: Int)(implicit context: Context) = {
    val i = new ImageView(context)
    i.setImageDrawable(getInstrumentation.getContext.getResources.getDrawable(novoda.widget.tests.R.drawable.gradient_box));
    //i.setMinimumWidth(width)
    //i.setMinimumHeight(height)
    i
  }

}