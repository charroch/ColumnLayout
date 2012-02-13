package novoda.android.scala

import org.scalatest._
import org.scalatest.events._
import org.scalatest.tools.PrintReporter._
import org.scalatest.prop.PropertyCheckFailedException
import Suite.indentation

/**
 * A <code>Reporter</code> that prints test status information to
 * a <code>Writer</code>, <code>OutputStream</code>, or file.
 *
 * @author Bill Venners
 */
abstract class StringReporter(presentAllDurations: Boolean,
                              presentInColor: Boolean, presentShortStackTraces: Boolean, presentFullStackTraces: Boolean) extends ResourcefulReporter {

  private def withPossibleLineNumber(stringToPrint: String, throwable: Option[Throwable]): String = {
    throwable match {
      case Some(stackDepth: StackDepth) =>
        stackDepth.failedCodeFileNameAndLineNumberString match {
          case Some(lineNumberString) =>
            Resources("printedReportPlusLineNumber", stringToPrint, lineNumberString)
          case None => stringToPrint
        }
      case _ => stringToPrint
    }
  }

  protected def printPossiblyInColor(text: String, ansiColor: String)

  // Called for TestFailed, InfoProvided (because it can have a throwable in it), SuiteAborted, and RunAborted
  private def stringsToPrintOnError(noteResourceName: String, errorResourceName: String, message: String, throwable: Option[Throwable],
                                    formatter: Option[Formatter], suiteName: Option[String], testName: Option[String], duration: Option[Long]): List[String] = {

    val stringToPrint =
      formatter match {
        case Some(IndentedText(formattedText, _, _)) =>
          Resources("specTextAndNote", formattedText, Resources(noteResourceName))
        case _ =>
          // Deny MotionToSuppress directives in error events, because error info needs to be seen by users
          suiteName match {
            case Some(sn) =>
              testName match {
                case Some(tn) => Resources(errorResourceName, sn + ": " + tn)
                case None => Resources(errorResourceName, sn)
              }
            // Should not get here with built-in ScalaTest stuff, but custom stuff could get here.
            case None => Resources(errorResourceName, Resources("noNameSpecified"))
          }
      }

    val stringToPrintWithPossibleDuration =
      duration match {
        case Some(milliseconds) =>
          if (presentAllDurations)
            Resources("withDuration", stringToPrint, makeDurationString(milliseconds))
          else
            stringToPrint
        case None => stringToPrint
      }

    // If there's a message, put it on the next line, indented two spaces
    val possiblyEmptyMessage = Reporter.messageOrThrowablesDetailMessage(message, throwable)

    val possiblyEmptyMessageWithPossibleLineNumber =
      throwable match {
        case Some(e: PropertyCheckFailedException) => possiblyEmptyMessage // PCFEs already include the line number
        case Some(e: StackDepth) => withPossibleLineNumber(possiblyEmptyMessage, throwable) // Show it in the stack depth case
        case _ => "" // Don't show it in the non-stack depth case. It will be shown after the exception class name and colon.
      }

    // The whiteSpace is just used for printing out stack traces, etc., things that go after a test name. The formatted
    // text for test names actually goes over to the left once in a sense, to make room for the icon. So if the indentation
    // level is 3 for example, the "- " for that test's formatted text is really indented 2 times (or four spaces: "    ")
    // So that's why normally the indentation level times two spaces should be the white space. But at the top level (indentation
    // level of 0), the icon also has to go at 0 (because subtracting one would put it at -1), so in that case the white space
    // should be two spaces (or 1 level of indentation).
    val whiteSpace =
      formatter match {
        case Some(IndentedText(_, _, indentationLevel)) if (indentationLevel != 0) => indentation(indentationLevel)
        case _ => indentation(1)
      }

    def getStackTrace(throwable: Option[Throwable]): List[String] =
      throwable match {
        case Some(throwable) =>

          def stackTrace(throwable: Throwable, isCause: Boolean): List[String] = {

            val className = throwable.getClass.getName
            val labeledClassName = if (isCause) Resources("DetailsCause") + ": " + className else className
            // Only show the : message if a cause, because first one will have its message printed out 
            // Or if it is a non-StackDepth exception, because if they throw Exception with no message, the
            // message was coming out as "java.lang.Exception" then on the next line it repeated it. In the
            // case of no exception message, I think it looks best to just say the class name followed by a colon
            // and nothing else.
            val colonMessageOrJustColon =
              if ((throwable.getMessage != null && !throwable.getMessage.trim.isEmpty) && (isCause || !(throwable.isInstanceOf[StackDepth])))
                ": " + throwable.getMessage.trim
              else
                ":"

            val labeledClassNameWithMessage =
              whiteSpace + labeledClassName + colonMessageOrJustColon

            if (presentShortStackTraces || presentFullStackTraces || !(throwable.isInstanceOf[StackDepth])) {

              // Indent each stack trace item two spaces, and prepend that with an "at "
              val stackTraceElements = throwable.getStackTrace.toList map {
                whiteSpace + "at " + _.toString
              }
              val cause = throwable.getCause

              val stackTraceThisThrowable = labeledClassNameWithMessage :: stackTraceElements

              if (presentFullStackTraces) {
                if (cause == null)
                  stackTraceThisThrowable
                else
                  stackTraceThisThrowable ::: stackTrace(cause, true) // Not tail recursive, but shouldn't be too deep
              }
              else {

                // The drop(1) or drop(stackDepth + 1) that extra one is the labeledClassNameWithMessage
                val stackTraceThisThrowableTruncated =
                  throwable match {
                    case e: Throwable with StackDepth =>
                      val stackDepth = e.failedCodeStackDepth
                      stackTraceThisThrowable.head :: (whiteSpace + "...") :: stackTraceThisThrowable.drop(stackDepth + 1).take(7) ::: List(whiteSpace + "...")
                    case _ => // In case of IAE or what not, show top 10 stack frames
                      stackTraceThisThrowable.head :: stackTraceThisThrowable.drop(1).take(10) ::: List(whiteSpace + "...")
                  }

                if (cause == null)
                  stackTraceThisThrowableTruncated
                else
                  stackTraceThisThrowableTruncated ::: stackTrace(cause, true) // Not tail recursive, but shouldn't be too deep
              }
            }
            else
              Nil
          }
          stackTrace(throwable, false)
        case None => List()
      }

    if (possiblyEmptyMessageWithPossibleLineNumber.isEmpty)
      stringToPrintWithPossibleDuration :: getStackTrace(throwable)
    else
      stringToPrintWithPossibleDuration :: (whiteSpace + possiblyEmptyMessageWithPossibleLineNumber) :: getStackTrace(throwable)
  }

  private def stringToPrintWhenNoError(resourceName: String, formatter: Option[Formatter], suiteName: String, testName: Option[String]): Option[String] =
    stringToPrintWhenNoError(resourceName, formatter, suiteName, testName, None)

  private def stringToPrintWhenNoError(resourceName: String, formatter: Option[Formatter], suiteName: String, testName: Option[String], duration: Option[Long]): Option[String] = {

    formatter match {
      case Some(IndentedText(formattedText, _, _)) =>
        duration match {
          case Some(milliseconds) =>
            if (presentAllDurations)
              Some(Resources("withDuration", formattedText, makeDurationString(milliseconds)))
            else
              Some(formattedText)
          case None => Some(formattedText)
        }
      case Some(MotionToSuppress) => None
      case _ =>
        val arg =
          testName match {
            case Some(tn) => suiteName + ": " + tn
            case None => suiteName
          }
        val unformattedText = Resources(resourceName, arg)
        duration match {
          case Some(milliseconds) =>
            if (presentAllDurations)
              Some(Resources("withDuration", unformattedText, makeDurationString(milliseconds)))
            else
              Some(unformattedText)
          case None => Some(unformattedText)
        }

    }
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

      case TestCanceled(ordinal, message, suiteName, suiteID, suiteClassName, decodedSuiteName, testName, testText, decodedTestName, throwable, duration, formatter, location, payload, threadName, timeStamp) =>

        val lines = stringsToPrintOnError("canceledNote", "testCanceled", message, throwable, formatter, Some(suiteName), Some(testName), duration)
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

      case ScopeOpened(ordinal, message, nameInfo, aboutAPendingTest, aboutACanceledTest, formatter, location, payload, threadName, timeStamp) =>

        val testNameInfo = nameInfo.testName
        val stringToPrint = stringToPrintWhenNoError("scopeOpened", formatter, nameInfo.suiteName,
          testNameInfo match {
            case Some(tnInfo) => Some(tnInfo.testName)
            case None => None
          })
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
        stringToPrint match {
          case Some(string) => printPossiblyInColor(string, if (shouldBeYellow) ansiYellow else ansiGreen)
          case None =>
        }

      // TODO: Reduce duplication among InfoProvided, ScopeOpened, and ScopeClosed
      case ScopeClosed(ordinal, message, nameInfo, aboutAPendingTest, aboutACanceledTest, formatter, location, payload, threadName, timeStamp) =>

        val testNameInfo = nameInfo.testName
        val stringToPrint = stringToPrintWhenNoError("scopeClosed", formatter, nameInfo.suiteName,
          testNameInfo match {
            case Some(tnInfo) => Some(tnInfo.testName)
            case None => None
          }) // TODO: I htink I want ot say Scope Closed - + message
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
        stringToPrint match {
          case Some(string) => printPossiblyInColor(string, if (shouldBeYellow) ansiYellow else ansiGreen)
          case None =>
        }

      case MarkupProvided(ordinal, message, nameInfo, aboutAPendingTest, aboutACanceledTest, formatter, location, payload, threadName, timeStamp) =>
      //println("Got markup: " + message)
      // Won't do anything here, because not reporting these events in the StringReporter.

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

  protected def makeFinalReport(resourceName: String, duration: Option[Long], summaryOption: Option[Summary]) {

    summaryOption match {
      case Some(summary) =>

        import summary._

        duration match {
          case Some(msSinceEpoch) =>
            printPossiblyInColor(Resources(resourceName + "In", makeDurationString(msSinceEpoch)), ansiCyan)
          case None =>
            printPossiblyInColor(Resources(resourceName), ansiCyan)
        }

        // totalNumberOfTestsRun=Total number of tests run was: {0}
        printPossiblyInColor(Resources("totalNumberOfTestsRun", testsCompletedCount.toString), ansiCyan)

        // Suite Summary: completed {0}, aborted {1}
        printPossiblyInColor(Resources("suiteSummary", suitesCompletedCount.toString, suitesAbortedCount.toString), ansiCyan)

        // Test Summary: succeeded {0}, failed {1}, ignored, {2}, pending {3}, canceled {4}
        printPossiblyInColor(Resources("testSummary", testsSucceededCount.toString, testsFailedCount.toString, testsIgnoredCount.toString, testsPendingCount.toString, testsCanceledCount.toString), ansiCyan)

        // *** 1 SUITE ABORTED ***
        if (suitesAbortedCount == 1)
          printPossiblyInColor(Resources("oneSuiteAborted"), ansiRed)

        // *** {0} SUITES ABORTED ***
        else if (suitesAbortedCount > 1)
          printPossiblyInColor(Resources("multipleSuitesAborted", suitesAbortedCount.toString), ansiRed)

        // *** 1 TEST FAILED ***
        if (testsFailedCount == 1)
          printPossiblyInColor(Resources("oneTestFailed"), ansiRed)

        // *** {0} TESTS FAILED ***
        else if (testsFailedCount > 1)
          printPossiblyInColor(Resources("multipleTestsFailed", testsFailedCount.toString), ansiRed)

        else if (suitesAbortedCount == 0) // Maybe don't want to say this if the run aborted or stopped because "all"
          printPossiblyInColor(Resources("allTestsPassed"), ansiGreen)

      case None =>
    }
  }

  // We subtract one from test reports because we add "- " in front, so if one is actually zero, it will come here as -1
  // private def indent(s: String, times: Int) = if (times <= 0) s else ("  " * times) + s

  // Stupid properties file won't let me put spaces at the beginning of a property
  // "  {0}" comes out as "{0}", so I can't do indenting in a localizable way. For now
  // just indent two space to the left.  //  if (times <= 0) s 
  //  else Resources("indentOnce", indent(s, times - 1))
}

object StringReporter {
  def countTrailingEOLs(s: String): Int = s.length - s.lastIndexWhere(_ != '\n') - 1

  def countLeadingEOLs(s: String): Int = {
    val idx = s.findIndexOf(_ != '\n')
    if (idx != -1) idx else 0
  }

  def colorizeLinesIndividually(text: String, ansiColor: String): String =
    if (text.trim.isEmpty) text
    else {
      ("\n" * countLeadingEOLs(text)) +
        text.split("\n").dropWhile(_.isEmpty).map(ansiColor + _ + ansiReset).mkString("\n") +
        ("\n" * countTrailingEOLs(text))
    }
}
