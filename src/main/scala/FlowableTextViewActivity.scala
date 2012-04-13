package novoda.widget

;

import android.app.Activity
import android.os.Bundle
import android.graphics.Color

/**
 * Created with IntelliJ IDEA.
 * User: acsia
 * Date: 4/13/12
 * Time: 1:51 PM
 * To change this template use File | Settings | File Templates.
 */

class FlowableTextViewActivity extends Activity {

  override def onCreate(b: Bundle) {
    super.onCreate(b)
    val f1 = new FlowableTextView(this)
    f1.setBackgroundColor(Color.RED)
    f1.setText("Hellow world")

    setContentView(f1)
  }

}
