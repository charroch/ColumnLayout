package novoda.android.test

import android.view.View
import android.content.Context
import android.widget.TextView
import android.view.ViewGroup.LayoutParams
import org.scalatest.matchers.{BeMatcher, MatchResult, Matcher}

class RichView(v: View) {

  def toS: String = {
    val name = v.getClass.getSimpleName;
    val id = (if (v.getId == View.NO_ID) "no ID" else v.getContext.getResources.getResourceName(v.getId))
    "%s with id %s [l:%s, t:%s, r:%s, b:%s]".format(name, id, v.getLeft, v.getTop, v.getRight, v.getBottom)
  }
}

object RichView {
  implicit def toRichView(v: View) = new RichView(v);

  implicit def stringToView(s: CharSequence)(implicit c: Context) = {
    val t = new TextView(c)
    t.setText(s)
    t.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
    t
  }
}

trait ViewMatchers {

  import RichView._

  abstract class ViewBeMatcher(right: View) extends BeMatcher[View]

  def left_of(right: View) = new ViewBeMatcher(right) {
    def apply(left: View) =
      MatchResult(
        left.getRight <= right.getLeft,
        left.toS + " was right of " + right.toS,
        left.toS + " was left of " + right.toS
      )
  }

  def right_of(right: View) = new ViewBeMatcher(right) {
    def apply(left: View) =
      MatchResult(
        left.getRight >= right.getLeft,
        left.toS + " was right of " + right.toS,
        left.toS + " was left of " + right.toS
      )

  }

  def below(right: View) = new ViewBeMatcher(right) {
    def apply(left: View) = MatchResult(
      left.getBottom >= right.getTop,
      left.toS + " was above " + right.toS,
      left.toS + " was below " + right.toS
    )
  }

  def above(right: View) = new ViewBeMatcher(right) {
    def apply(left: View) = MatchResult(
      left.getBottom <= right.getTop,
      left.toS + " was below " + right.toS,
      left.toS + " was above " + right.toS
    )
  }
}

object ViewMatchers extends ViewMatchers

