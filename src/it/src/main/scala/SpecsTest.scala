import org.specs2.mutable._

class HelloWorldSpec extends Specification {

  "The 'Hello world' string" should {
    "contain 11 characters" in {
      "Hello world" must have size(11)
    }
    "start with 'Hello'" in {
      "Hello world" must startWith("Hello")
    }
    "end with 'world'" in {
      "Hello world" must endWith("world")
    }
  }
}

class HelloWorldSpec extends S {

  "The 'Hello world' string" should {
    "contain 11 characters" in {
      "Hello world" must have size(11)
    }
    "start with 'Hello'" in {
      "Hello world" must startWith("Hello")
    }
    "end with 'world'" in {
      "Hello world" must endWith("world")
    }
  }
}


import org.specs2._

import control._
import main.{ArgumentsShortcuts, ArgumentsArgs}
import time._
import execute._
import matcher._
import specification._


trait S extends SpecificationStructure    with matcher.MustMatchers with matcher.ShouldMatchers with mutable.FragmentsBuilder {
  implicit def contextToResult[T](t: MatchResult[T])(implicit context: Context = defaultContext): Result = context(asResult(t))
  implicit def outsideFunctionToResult[T, S](implicit o: Outside[T]) = (f: T => MatchResult[S]) => { o((t1:T) => f(t1).toResult) }
}