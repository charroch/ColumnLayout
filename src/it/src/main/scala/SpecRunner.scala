package novoda.android.scala

import android.app.Instrumentation
import android.os.Bundle

import android.util.Log
import org.scalatest.{Reporter, Tracker, Stopper, Filter}
import org.scalatest.events.Event
import org.scalatest.events._

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

class InstrumentationReporter extends Reporter {
  final def execute() {
    //run(None, new StandardOutReporter, new Stopper {}, Filter(), Map(), None, new Tracker)
  }

  def apply(event: Event) {

    event match {

      case RunStarting(ordinal, testCount, configMap, formatter, location, payload, threadName, timeStamp) =>

        if (testCount < 0)
          throw new IllegalArgumentException

        val string = Resources("runStarting", testCount.toString)
        printPossiblyInColor(string, ansiCyan)

      case RunCompleted(ordinal, duration, summary, formatter, location, payload, threadName, timeStamp) =>

        makeFinalReport("runCompleted", duration, summary)

      case RunStopped(ordinal, duration, summary, formatter, location, payload, threadName, timeStamp) =>

        makeFinalReport("runStopped", duration, summary)

      case RunAborted(ordinal, message, throwable, duration, summary, formatter, location, payload, threadName, timeStamp) =>

        val lines = stringsToPrintOnError("abortedNote", "runAborted", message, throwable, formatter, None, None, duration)
        for (line <- lines) printPossiblyInColor(line, ansiRed)

      case SuiteStarting(ordinal, suiteName, suiteID, suiteClassName, decodedSuiteName, formatter, location, rerunnable, payload, threadName, timeStamp) =>

        val stringToPrint = stringToPrintWhenNoError("suiteStarting", formatter, suiteName, None)

        stringToPrint match {
          case Some(string) => printPossiblyInColor(string, ansiGreen)
          case None =>
        }

      case SuiteCompleted(ordinal, suiteName, suiteID, suiteClassName, decodedSuiteName, duration, formatter, location, rerunnable, payload, threadName, timeStamp) =>

        val stringToPrint = stringToPrintWhenNoError("suiteCompleted", formatter, suiteName, None, duration)

        stringToPrint match {
          case Some(string) => printPossiblyInColor(string, ansiGreen)
          case None =>
        }

      case SuiteAborted(ordinal, message, suiteName, suiteID, suiteClassName, decodedSuiteName, throwable, duration, formatter, location, rerunnable, payload, threadName, timeStamp) =>

        val lines = stringsToPrintOnError("abortedNote", "suiteAborted", message, throwable, formatter, Some(suiteName), None, duration)
        for (line <- lines) printPossiblyInColor(line, ansiRed)

      case TestStarting(ordinal, suiteName, suiteID, suiteClassName, decodedSuiteName, testName, testText, decodedTestName, formatter, location, rerunnable, payload, threadName, timeStamp) =>

        val stringToPrint = stringToPrintWhenNoError("testStarting", formatter, suiteName, Some(testName))

        stringToPrint match {
          case Some(string) => printPossiblyInColor(string, ansiGreen)
          case None =>
        }

      case TestSucceeded(ordinal, suiteName, suiteID, suiteClassName, decodedSuiteName, testName, testText, decodedTestName, duration, formatter, location, rerunnable, payload, threadName, timeStamp) =>

        val stringToPrint = stringToPrintWhenNoError("testSucceeded", formatter, suiteName, Some(testName), duration)

        stringToPrint match {
          case Some(string) => printPossiblyInColor(string, ansiGreen)
          case None =>
        }

      case TestIgnored(ordinal, suiteName, suiteID, suiteClassName, decodedSuiteName, testName, testText, decodedTestName, formatter, location, payload, threadName, timeStamp) =>

        val stringToPrint =
          formatter match {
            case Some(IndentedText(formattedText, _, _)) => Some(Resources("specTextAndNote", formattedText, Resources("ignoredNote")))
            case Some(MotionToSuppress) => None
            case _ => Some(Resources("testIgnored", suiteName + ": " + testName))
          }

        stringToPrint match {
          case Some(string) => printPossiblyInColor(string, ansiYellow)
          case None =>
        }

      case TestFailed(ordinal, message, suiteName, suiteID, suiteClassName, decodedSuiteName, testName, testText, decodedTestName, throwable, duration, formatter, location, rerunnable, payload, threadName, timeStamp) =>

        val lines = stringsToPrintOnError("failedNote", "testFailed", message, throwable, formatter, Some(suiteName), Some(testName), duration)
        for (line <- lines) printPossiblyInColor(line, ansiRed)

      case InfoProvided(ordinal, message, nameInfo, aboutAPendingTest, aboutACanceledTest, throwable, formatter, location, payload, threadName, timeStamp) =>

        val (suiteName, testName) =
          nameInfo match {
            case Some(NameInfo(suiteName, _, _, _, testNameInfo)) => (Some(suiteName),
              testNameInfo match {
                case Some(tnInfo) => Some(tnInfo.testName)
                case None => None
              })
            case None => (None, None)
          }
        val lines = stringsToPrintOnError("infoProvidedNote", "infoProvided", message, throwable, formatter, suiteName, testName, None)
        val isPending = aboutAPendingTest.getOrElse(false)
        /*
                  aboutAPendingTest match {
                    case Some(isPending) => isPending
                    case None => false
                  }
        */
        val wasCanceled = aboutACanceledTest.getOrElse(false)
        /*
                  aboutACanceledTest match {
                    case Some(wasCanceled) => wasCanceled
                    case None => false
                  }
        */
        val shouldBeYellow = isPending || wasCanceled
        for (line <- lines) printPossiblyInColor(line, if (shouldBeYellow) ansiYellow else ansiGreen)

      case TestPending(ordinal, suiteName, suiteID, suiteClassName, decodedSuiteName, testName, testText, decodedTestName, duration, formatter, location, payload, threadName, timeStamp) =>

        val stringToPrint =
          formatter match {
            case Some(IndentedText(formattedText, _, _)) => Some(Resources("specTextAndNote", formattedText, Resources("pendingNote")))
            case Some(MotionToSuppress) => None
            case _ => Some(Resources("testPending", suiteName + ": " + testName))
          }

        stringToPrint match {
          case Some(string) => printPossiblyInColor(string, ansiYellow)
          case None =>
        }

      // case _ => throw new RuntimeException("Unhandled event")
    }
  }
}