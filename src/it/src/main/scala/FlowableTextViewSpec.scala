import android.app.{Instrumentation, Activity}
import android.content.{Context, Intent}
import android.os.Bundle
import android.test.{InstrumentationTestCase, ActivityInstrumentationTestCase2}
import android.widget.TextView
import novoda.android.test.ViewMatchers
import novoda.widget.tests.ActivityStub
import novoda.widget.{TextLayoutUtil}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.WordSpec

class FlowableTextViewSpec extends ActivitySpec {

  class RichActivity(a: Activity) {
    def waitForIdle(instr: Instrumentation) {
      //instr.waitForIdleSync()
    }
  }

  object RichActivity {
    implicit def toRichActivity(a: Activity): RichActivity = new RichActivity(a)
  }

  implicit def toRunnable(a: => Any): Runnable = new Runnable() {
    def run() {
      a
    }
  }


  /**
   *
   * This fails and blocks
   * @param testCode
   */

  def activity(testCode: Activity => Any) {
    implicit val inst: Instrumentation = this.getInstrumentation
    val activity = launchActivity("novoda.widget.tests", classOf[ActivityStub], new Bundle);
    try {
      runTestOnUiThread {
        testCode(activity)
      }
      // getInstrumentation.waitForIdleSync()
    }
    finally {
      //      getInstrumentation.callActivityOnPause(activity)
      //      getInstrumentation.callActivityOnStop(activity)
      //      getInstrumentation.callActivityOnDestroy(activity)
    }
  }

  lazy val context: Context = getInstrumentation.getTargetContext

  "A flowable text view" should {
    "consider indexing of text" in {
      val activity = getActivity
      val tv = new TextView(context)
      tv.setText("Hello World")
      runTestOnUiThread {
        activity.setContentView(tv)
      }
      tv.getBottom should be(140)
      super.tearDown()
    }
  }


}


class ActivitySpec extends ActivityInstrumentationTestCase2[ActivityStub](classOf[ActivityStub]) with TextLayoutUtil with WordSpec with ShouldMatchers with ViewMatchers
