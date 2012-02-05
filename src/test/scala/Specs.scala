import android.content.Context
import novoda.widget.ColumnLayout
import org.specs2.mock.Mockito

class HelloWorldSpec extends RoboSpecs with Mockito {

  "The 'Hello world' string" should {
    "contain 11 characters" in {
      "Hello world" must have size (11)
    }
    "start with 'Hello'" in {
      "Hello world" must startWith("Hello")
    }
    "end with 'world'" in {
      val cl = new ColumnLayout(mock[Context])
      "Hello world" must endWith("world")
    }
  }
}