package novoda.widget.tests.activities

import android.app.Activity
import android.os.Bundle
import novoda.widget.tests.{ActivityStub, R}
import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import android.test.InstrumentationTestCase
import android.content.Intent
import novoda.widget.ColumnLayout

class TwoColumnHeaderSpan extends Activity {
  override def onCreate(b: Bundle) {
    super.onCreate(b)
    setContentView(R.layout.two_columns_header_span)
    findViewById(R.id.column_layout).asInstanceOf[ColumnLayout].setText(ipsum)
  }

  lazy val ipsum =
    """Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in adipiscing sapien. Nunc id diam nec purus dictum pretium. Proin pharetra tempor nisi eget fringilla. Phasellus sollicitudin lorem eu odio dictum tincidunt. Nunc vel est vitae elit vulputate congue nec in mauris. In leo metus, varius in sollicitudin eu, egestas vel mi. Maecenas semper pellentesque felis vitae rhoncus. Sed ultricies ornare nunc ac ultrices. Sed tincidunt, nulla sit amet porta feugiat, enim libero euismod massa, quis lobortis diam sem sed ante. Donec ultricies lacus quis diam euismod mollis.
    |Curabitur at dolor nulla. Proin dui turpis, sodales ac facilisis vel, vestibulum ac dui. Etiam varius sagittis dolor, semper aliquam arcu dictum sed. Sed in nibh at urna posuere interdum in eget nulla. Maecenas vulputate magna eget eros euismod nec venenatis mi bibendum. Nullam non imperdiet quam. Vivamus aliquam tincidunt turpis, in euismod risus adipiscing in.
    |Mauris placerat consectetur dolor auctor euismod. Donec placerat pretium sem nec lobortis. Cras semper, justo in cursus viverra, felis enim gravida sapien, at pellentesque felis arcu eu augue. Duis tellus sapien, congue sit amet convallis eu, mollis condimentum eros. Donec lorem metus, lacinia quis sodales vitae, elementum at nibh. Morbi dapibus, mauris a pharetra scelerisque, orci ante adipiscing metus, non venenatis odio risus id felis. Nam id interdum tellus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Maecenas elementum venenatis nibh vel dictum.
    |Curabitur a purus justo, vitae dictum lacus. Quisque sed justo nec orci vestibulum tristique. Phasellus pharetra cursus aliquet. Nam vel volutpat erat. Donec mattis egestas felis eget tempor. Donec iaculis lectus sit amet augue dictum a euismod libero facilisis. Sed id nunc id tellus condimentum gravida.
    |Morbi iaculis iaculis est, id volutpat lorem viverra auctor. Donec urna tellus, dignissim vel pellentesque sed, eleifend nec diam. Vivamus condimentum justo tellus, eu dignissim nulla. Nam sed urna dapibus lorem vulputate tincidunt ac vitae lacus. Fusce eu risus nibh. Suspendisse fringilla accumsan nulla sed sagittis. Cras in tortor nec elit blandit semper non sed ante. Phasellus tellus risus, volutpat sed vehicula ac, congue id ligula. Nulla facilisi. Duis sed elit elit, a commodo dolor. Morbi accumsan felis sit amet arcu varius sed eleifend mauris fermentum.
    """.stripMargin
}


class TwoColumnHeaderSpanSpec extends StubActivityTest {

  "it should start the activity" should {
    "test it " in {
      val in = new Intent(getInstrumentation.getContext, classOf[ActivityStub])
      in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      val activity = getInstrumentation.startActivitySync(in)
      runTestOnUiThread(new Runnable() {
        def run() {

          //setUp()
          activity.setContentView(R.layout.two_columns_header_span)
          activity.findViewById(R.id.column_layout).asInstanceOf[ColumnLayout].setText(ipsum)
          activity.findViewById(R.id.column_layout).invalidate()
        }
      })
      getInstrumentation.waitForIdleSync()

      Thread.sleep(20000)
      //tearDown();
      val columnLayout = activity.findViewById(R.id.column_layout).asInstanceOf[ColumnLayout]
      columnLayout.getChildCount should be(5)
      columnLayout.getChildAt(2).getHeight should be(150)
    }
  }

  lazy val ipsum =
    """Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in adipiscing sapien. Nunc id diam nec purus dictum pretium. Proin pharetra tempor nisi eget fringilla. Phasellus sollicitudin lorem eu odio dictum tincidunt. Nunc vel est vitae elit vulputate congue nec in mauris. In leo metus, varius in sollicitudin eu, egestas vel mi. Maecenas semper pellentesque felis vitae rhoncus. Sed ultricies ornare nunc ac ultrices. Sed tincidunt, nulla sit amet porta feugiat, enim libero euismod massa, quis lobortis diam sem sed ante. Donec ultricies lacus quis diam euismod mollis.
    |Curabitur at dolor nulla. Proin dui turpis, sodales ac facilisis vel, vestibulum ac dui. Etiam varius sagittis dolor, semper aliquam arcu dictum sed. Sed in nibh at urna posuere interdum in eget nulla. Maecenas vulputate magna eget eros euismod nec venenatis mi bibendum. Nullam non imperdiet quam. Vivamus aliquam tincidunt turpis, in euismod risus adipiscing in.
    |Mauris placerat consectetur dolor auctor euismod. Donec placerat pretium sem nec lobortis. Cras semper, justo in cursus viverra, felis enim gravida sapien, at pellentesque felis arcu eu augue. Duis tellus sapien, congue sit amet convallis eu, mollis condimentum eros. Donec lorem metus, lacinia quis sodales vitae, elementum at nibh. Morbi dapibus, mauris a pharetra scelerisque, orci ante adipiscing metus, non venenatis odio risus id felis. Nam id interdum tellus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Maecenas elementum venenatis nibh vel dictum.
    |Curabitur a purus justo, vitae dictum lacus. Quisque sed justo nec orci vestibulum tristique. Phasellus pharetra cursus aliquet. Nam vel volutpat erat. Donec mattis egestas felis eget tempor. Donec iaculis lectus sit amet augue dictum a euismod libero facilisis. Sed id nunc id tellus condimentum gravida.
    |Morbi iaculis iaculis est, id volutpat lorem viverra auctor. Donec urna tellus, dignissim vel pellentesque sed, eleifend nec diam. Vivamus condimentum justo tellus, eu dignissim nulla. Nam sed urna dapibus lorem vulputate tincidunt ac vitae lacus. Fusce eu risus nibh. Suspendisse fringilla accumsan nulla sed sagittis. Cras in tortor nec elit blandit semper non sed ante. Phasellus tellus risus, volutpat sed vehicula ac, congue id ligula. Nulla facilisi. Duis sed elit elit, a commodo dolor. Morbi accumsan felis sit amet arcu varius sed eleifend mauris fermentum.
    """.stripMargin
}

class StubActivityTest
  extends InstrumentationTestCase
  with WordSpec with ShouldMatchers