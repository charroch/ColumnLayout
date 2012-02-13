import android.test.ActivityInstrumentationTestCase2
import novoda.widget.{TextLayoutUtil, ActivityStub}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.WordSpec

class TestSomeSpec extends ViewTestCase with WordSpec with ShouldMatchers {

  "A Columnist article" should {
    "have a title" in {
      //runTestOnUiThread(() => {
        getActivity should not(be(null))
      //});
      Thread.sleep(1000)

      "title" should be("title")
    }

    "have a header" in {
      "title" should be("title")
    }

    "have images" in {
      "title" should be("title")
    }
  }
}

abstract class ViewTestCase extends ActivityInstrumentationTestCase2[ActivityStub](classOf[ActivityStub]) with TextLayoutUtil