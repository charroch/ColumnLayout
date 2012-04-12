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
    import scala.collection.JavaConversions._
    dexFile.entries()
      .withFilter(isSpec)
      .withFilter(isSuite)
      .map(asSuite)
      .map(injectContext)
      .map(injectInstrumentation)
      .foreach(run)
    finish(1, new Bundle())
  }

  def run(s: Suite) {
    s.run(None, this.reporter, new Stopper {}, Filter(), Map(), None, new Tracker)
  }

  def isSuite(klass: String) = {
    classOf[Suite].isAssignableFrom(getContext.getClassLoader.loadClass(klass))
  }

  def asSuite(klass: String): Suite = {
    getContext.getClassLoader.loadClass(klass).newInstance().asInstanceOf[Suite]
  }

  def isSpec(klass: String): Boolean = {
    klass.endsWith("Spec") || klass.endsWith("Specs")
  }

  def injectInstrumentation(s: Suite) = {
    if (classOf[InstrumentationTestCase].isAssignableFrom(s.getClass)) {
      s.asInstanceOf[InstrumentationTestCase].injectInsrumentation(this)
    }
    s
  }

  def injectContext(s: Suite) = {
    if (classOf[AndroidTestCase].isAssignableFrom(s.getClass)) {
      s.asInstanceOf[ {
        def setContext(c: Context): Unit
      }].setContext(this.getTargetContext)
      s.asInstanceOf[ {
        def setTestContext(c: Context): Unit
      }].setTestContext(this.getTargetContext)
    }
    s
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