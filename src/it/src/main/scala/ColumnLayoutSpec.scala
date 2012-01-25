package novoda.widget.tests

import org.specs2.mutable._

class HelloWorldSpec extends Specification {

  "The 'Hello world' string" should {
    "contain 11 characters" in {
      "Hello world" must have size (11)
    }
    "start with 'Hello'" in {
      "Hello world" must startWith("Hello")
    }
    "end with 'world'" in {
      "Hello world" must endWith("world")
    }
  }
}


import android.test.InstrumentationTestRunner

class Runner extends InstrumentationTestRunner {
  override def onCreate(arguments: android.os.Bundle) {
    val spec = new HelloWorldSpec
    android.util.Log.i("TEST", "==============> running specs" + spec);
    specs2.run(spec)
  }
}

