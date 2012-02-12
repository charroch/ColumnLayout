package novoda.android.scala

import android.app.Instrumentation
import android.os.Bundle

import android.util.Log;

class SpecRunner extends Instrumentation {


  override def onCreate(arguments: Bundle) {
    Log.i("TEST", "this is on create " + arguments);
    start();
    finish(1, arguments)
  }

  override def onStart() {
    Log.i("TEST", "this on start -============= ");
  }
}