import android.test.InstrumentationTestCase
import android.view.View.MeasureSpec
import novoda.widget.FlowableTextView
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.WordSpec

class FlowableTextViewSpec extends StubActivityTest {

  "A flowable text view" should {
    "fit its container view to its maximum" in {
      val f = new FlowableTextView(getInstrumentation.getTargetContext)
      f.setText(longText)
      val width = MeasureSpec.makeMeasureSpec(200, MeasureSpec.AT_MOST)
      val height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
      f.measure(width, height)
      f.getMeasuredHeight should be(9503)
      val newHeight = MeasureSpec.makeMeasureSpec(500, MeasureSpec.AT_MOST)
      f.measure(width, newHeight)
      f.getMeasuredHeight should be(500)
    }
  }

  val longText = "HelloWorld," * 1000
}

class StubActivityTest
  extends InstrumentationTestCase
  with WordSpec with ShouldMatchers
