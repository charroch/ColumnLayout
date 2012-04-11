package org.scalatest.tools

import android.app.Instrumentation
import org.scalatest._
import dalvik.system.DexFile
import java.io.File
import android.os.{Looper, Bundle}
import android.test.{AndroidTestCase, InstrumentationTestCase}
import android.content.Context


class SpecRunner extends SpecRunnerComponent with DefaultInstrumentationReporter

abstract class SpecRunnerComponent extends Instrumentation with InstrumentationReporter {

  override def onCreate(arguments: Bundle) {
    super.onCreate(arguments);
    start()
  }

  override def onStart() {
    Looper.prepare()
    val dexFile = new DexFile(new File(getContext.getApplicationInfo.publicSourceDir));
    val apkClassNames = dexFile.entries();

    import scala.collection.JavaConversions._

    val specs = apkClassNames.filter(
      _.endsWith("Spec")
    ).filter(c =>
      classOf[Suite].isAssignableFrom(getContext.getClassLoader.loadClass(c))
    ).map(getContext.getClassLoader.loadClass(_).newInstance().asInstanceOf[Suite]).map(
      a => {
        if (classOf[InstrumentationTestCase].isAssignableFrom(a.getClass)) {
          a.asInstanceOf[InstrumentationTestCase].injectInsrumentation(this)
        }

        if (classOf[AndroidTestCase].isAssignableFrom(a.getClass)) {

          a.asInstanceOf[ {
            def setContext(c: Context): Unit
          }].setContext(this.getTargetContext)
          a.asInstanceOf[ {
            def setTestContext(c: Context): Unit
          }].setTestContext(this.getTargetContext)

        }


        a
      }
    )
    specs.foreach(_.run(None, this.reporter, new Stopper {}, Filter(), Map(), None, new Tracker))
    finish(1, new Bundle())
  }

  //
  //  trait Context[T <: Suite] {
  //    if (classOf[AndroidTestCase].isAssignableFrom(this.getClass())) {
  //
  //    }
  //  }
  //
  //  def injectContext(klass: Class) {
  //    if (classOf[AndroidTestCase].isAssignableFrom(klass.getClass())) {
  //      //        ((AndroidTestCase) test).setContext (context);
  //      //        ((AndroidTestCase) test).setTestContext (testContext);
  //    }
  //  }

  def injectInstrumentation() {
    //  private void setInstrumentationIfInstrumentationTestCase (
    //    Test test, Instrumentation instrumentation) {
    //    if (InstrumentationTestCase.class.isAssignableFrom (test.getClass () ) ) {
    //      ((InstrumentationTestCase) test).injectInstrumentation (instrumentation);
    //
    //    }
  }


}


trait InstrumentationReporter {
  i: Instrumentation =>
  def reporter: Reporter
}

trait DefaultInstrumentationReporter extends InstrumentationReporter {
  i: Instrumentation =>
  def reporter = new SimpleInstrumentationReporter(i)
}

class SimpleInstrumentationReporter(inst: Instrumentation) extends StringReporter(false, false, false, false) {
  final val ansiReset = "\033[0m"

  protected def printPossiblyInColor(text: String, ansiColor: String) = {
    val mTestResult = new Bundle();
    mTestResult.putString(Instrumentation.REPORT_KEY_STREAMRESULT, ansiColor + text + ansiReset + '\n')
    inst.sendStatus(0, mTestResult)
  }

  def dispose() {
  }
}