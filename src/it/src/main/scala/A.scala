package novoda.test

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{FlatSpec, FunSuite}
import android.util.Log
import org.scalatest.junit.ShouldMatchersForJUnit
import java.util.concurrent.CountDownLatch


class StackSpec(l:CountDownLatch) extends FlatSpec with ShouldMatchersForJUnit {

  "A Stack" should "pop values in last-in-first-out order" in {
    Log.i("TEST", "in here");
    1 should equal (1)
    l.countDown();
  }
}