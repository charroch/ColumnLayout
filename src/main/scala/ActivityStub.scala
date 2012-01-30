package novoda.widget

import android.app.Activity
import android.os.Bundle

class ActivityStub extends Activity {

  var isStarted = false;

  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle);
  }

  //
  override def onResume() {
    android.util.Log.i("TEST", "on start");
    isStarted = true;
    super.onResume();
  }

}