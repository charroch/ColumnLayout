package novoda.widget

;

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import layout.ColumnTextLayout
import novoda.widget.ColumnLayout.LayoutParams
import android.widget.{TextView, RelativeLayout}
import android.view.View.OnClickListener
import android.view.{View, ViewGroup}

class FlowableTextViewActivity extends Activity {

  override def onCreate(b: Bundle) {
    super.onCreate(b)

    val ctl = new ColumnTextLayout(ipsum, new TextView(this).getPaint)

    val rl = new RelativeLayout(this)

    val f1 = new FlowableTextView(this)
    f1.setFlowableText(ipsum)
    f1.setBackgroundColor(Color.RED)
   // f1.setLayout(ctl)
    f1.setHeight(200)
    //f1.setRoot(true)

    val f2 = new FlowableTextView(this)
    //f2.setRoot(false)
    f2.setBackgroundColor(Color.GREEN)
    f1.setNextFlowableTextView(f2)

    val f3 = new FlowableTextView(this)
    f3.setBackgroundColor(Color.CYAN)
    f2.setNextFlowableTextView(f3)

    val lp3 = new RelativeLayout.LayoutParams(500, 250)
    lp3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
    f3.setLayoutParams(lp3)

    val lp1 = new RelativeLayout.LayoutParams(200, 250)
    lp1.addRule(RelativeLayout.ALIGN_PARENT_TOP)
    f1.setLayoutParams(lp1)


    val lp = new RelativeLayout.LayoutParams(200, 100)
    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
    f2.setLayoutParams(lp)

    rl.addView(f3)
    rl.addView(f1)
    rl.addView(f2)

    f3.setOnClickListener(oc)
    f2.setOnClickListener(oc)
    f1.setOnClickListener(oc)

    setContentView(rl)

  }

  val oc = new OnClickListener {
    def onClick(p1: View) {
      val lp = p1.getLayoutParams
      lp.height *= 2
      p1.setLayoutParams(lp)
      p1.invalidate()
    }
  }


  def rl = {
    val rl = new RelativeLayout(this)
  }

  lazy val ipsum =
    """Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in adipiscing sapien. Nunc id diam nec purus dictum pretium. Proin pharetra tempor nisi eget fringilla. Phasellus sollicitudin lorem eu odio dictum tincidunt. Nunc vel est vitae elit vulputate congue nec in mauris. In leo metus, varius in sollicitudin eu, egestas vel mi. Maecenas semper pellentesque felis vitae rhoncus. Sed ultricies ornare nunc ac ultrices. Sed tincidunt, nulla sit amet porta feugiat, enim libero euismod massa, quis lobortis diam sem sed ante. Donec ultricies lacus quis diam euismod mollis.
    |Curabitur at dolor nulla. Proin dui turpis, sodales ac facilisis vel, vestibulum ac dui. Etiam varius sagittis dolor, semper aliquam arcu dictum sed. Sed in nibh at urna posuere interdum in eget nulla. Maecenas vulputate magna eget eros euismod nec venenatis mi bibendum. Nullam non imperdiet quam. Vivamus aliquam tincidunt turpis, in euismod risus adipiscing in.
    |Mauris placerat consectetur dolor auctor euismod. Donec placerat pretium sem nec lobortis. Cras semper, justo in cursus viverra, felis enim gravida sapien, at pellentesque felis arcu eu augue. Duis tellus sapien, congue sit amet convallis eu, mollis condimentum eros. Donec lorem metus, lacinia quis sodales vitae, elementum at nibh. Morbi dapibus, mauris a pharetra scelerisque, orci ante adipiscing metus, non venenatis odio risus id felis. Nam id interdum tellus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Maecenas elementum venenatis nibh vel dictum.
    |Curabitur a purus justo, vitae dictum lacus. Quisque sed justo nec orci vestibulum tristique. Phasellus pharetra cursus aliquet. Nam vel volutpat erat. Donec mattis egestas felis eget tempor. Donec iaculis lectus sit amet augue dictum a euismod libero facilisis. Sed id nunc id tellus condimentum gravida.
    |Morbi iaculis iaculis est, id volutpat lorem viverra auctor. Donec urna tellus, dignissim vel pellentesque sed, eleifend nec diam. Vivamus condimentum justo tellus, eu dignissim nulla. Nam sed urna dapibus lorem vulputate tincidunt ac vitae lacus. Fusce eu risus nibh. Suspendisse fringilla accumsan nulla sed sagittis. Cras in tortor nec elit blandit semper non sed ante. Phasellus tellus risus, volutpat sed vehicula ac, congue id ligula. Nulla facilisi. Duis sed elit elit, a commodo dolor. Morbi accumsan felis sit amet arcu varius sed eleifend mauris fermentum.
    """.stripMargin
}
