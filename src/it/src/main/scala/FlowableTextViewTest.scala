package novoda.widget


import junit.framework.Assert._
import android.test.ActivityInstrumentationTestCase2
import java.util.concurrent.{TimeUnit, CountDownLatch}
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup


class FlowableTextViewTest extends ActivityInstrumentationTestCase2[ActivityStub](classOf[ActivityStub]) with TextLayoutUtil {


  def test_should_break_after_5_character {

    val a = getActivity
    assertNotNull(a);
    val next = new FlowableTextView(a);
    next.setId(123);
    val f = new FlowableTextView(a);
    f.setId(2);
    f.setText(■("Hello World"))
    f.setNextFlowableTextView(next);

    val (lineHeight, charWidth) = compute()

    val vg = new ViewGroup.LayoutParams(charWidth.toInt * 5, lineHeight.toInt);
    val vgNext = new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    val latch = new CountDownLatch(1)
    runTestOnUiThread(() => {
      getActivity.setContentView(f, vg)
      getActivity.addContentView(next, vgNext)
      latch.countDown()
    });
    latch.await(10, TimeUnit.SECONDS)
    Thread.sleep(1000)

    // 5 on device
    assertEquals(6, f.getLaidText.length());
    assertEquals("World", next.getText.toString);
  }


}

