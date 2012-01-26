//package novoda.widget.tests
//
//
//import org.scalatest.matchers.ShouldMatchers
//import collection.immutable.Stack
//import android.os.{Looper, Bundle}
//import android.app.Instrumentation
//import org.scalatest.{Reporter, FlatSpec}
//import org.scalatest.events.Event
//
//class StackSpec extends FlatSpec with ShouldMatchers {
//
//  "A Stack" should "pop values in last-in-first-out order" in {
//    val stack = new Stack[Int]
//    stack.push(1)
//    stack.push(2)
//    2 should equal(2)
//    1 should equal(1)
//  }
//
//  it should "throw NoSuchElementException if an empty stack is popped" in {
//    val emptyStack = new Stack[String]
//    evaluating {
//      emptyStack.pop
//    } should produce[NoSuchElementException]
//  }
//}
//
//
//import android.test.InstrumentationTestRunner
//
//class Runner extends InstrumentationTestRunner {
//  override def onCreate(arguments: android.os.Bundle) {
//    android.util.Log.i("TEST", "==============> running specs");
//
//    var b: Bundle = new Bundle();
//    import org.scalatest._
//    //new StackSpec().run("test", new L, )
//
////    stopper: Stopper, filter: Filter,
////    configMap: Map[String, Any], distributor: Option[Distributor], tracker: Tracker) {
////
////    }
//    b.putString(Instrumentation.REPORT_KEY_STREAMRESULT, " hello world1")
//    b.putFloat("runtime", 2.3f);
//    b.putString("suite", "some specs");
//
//    sendStatus(0, b)
//  }
//
//  override def onStart() {
//    android.util.Log.i("TEST", "==============> start specs");
//    Looper.prepare();
//    var b: Bundle = new Bundle();
//    b.putString(Instrumentation.REPORT_KEY_STREAMRESULT, " hello world2")
//    b.putFloat("runtime", 2.3f);
//    b.putString("suite", "some specs");
//    sendStatus(0, b)
//
//    android.util.Log.i("TEST", "==============> end? specs");
//
//  }
//
//  override def getLoader: ClassLoader = {
//    class M extends ClassLoader {
//
//      override def loadClass(s: String) = {
//        println("TEST =>>>>>>>>>>>>" + s)
//        super.loadClass(s)
//      }
//
//      override def loadClass(n: String, r: Boolean) = {
//        loadClass(n);
//      }
//    }
//    new M
//  }
//
//  class L extends Reporter {
//    def apply(event: Event) {
//      var b: Bundle = new Bundle();
//      b.putString(Instrumentation.REPORT_KEY_STREAMRESULT, " Running: " + event.threadName);
//      b.putFloat("runtime", 2.3f);
//      b.putString("suite", "some specs");
//      sendStatus(0, b)
//
//    }
//  }
//
//}
//
