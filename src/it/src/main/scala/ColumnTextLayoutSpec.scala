import android.test.AndroidTestCase
import android.text.TextPaint
import novoda.widget.layout.ColumnTextLayout
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.WordSpec

class ColumnTextLayoutSpec2 extends AndroidTestCase with ShouldMatchers with WordSpec {

  "A flowable text" should {
    "keep track of columns" in {
      val flowableText = new ColumnTextLayout(ipsum, new TextPaint)
      val col1 = flowableText.next(50, 20)
      val col2 = flowableText.next(50, 20)
      val col3 = flowableText.next(50, 20)
      val col4 = flowableText.next(50, 20)

      flowableText.setCurrentColumn(col2)
      flowableText.size() should be(2)

      flowableText.next(50, 20).getText should be(col3.getText)
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