package novoda.android.test

import android.content.Context
import android.widget.TextView
import android.view.ViewGroup.LayoutParams
import org.scalatest.matchers.{BeMatcher, MatchResult}
import android.view.{ViewGroup, View}

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
        left.getLeft >= right.getRight,
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

  def parent_aligned(where: Position) = new BeMatcher[View] {
    def apply(leftV: View) = where.i match {
      case 0 => MatchResult(leftV.getLeft == leftV.getParent.asInstanceOf[ViewGroup].getLeft, leftV.toS + " left ", "left")
      case 1 => MatchResult(leftV.getTop == leftV.getParent.asInstanceOf[ViewGroup].getTop, leftV.toS + "top", "top")
      case 2 => MatchResult(leftV.getRight == leftV.getParent.asInstanceOf[ViewGroup].getRight, leftV.toS + "rigth" + leftV.getParent.asInstanceOf[ViewGroup].toS, "rogjt")
      case 3 => MatchResult(leftV.getBottom == leftV.getParent.asInstanceOf[ViewGroup].getBottom, leftV.toS + "bottom", "bottom")
    }

  }

  sealed abstract class Position(val i: Int)

  final case object left extends Position(0)

  final case object top extends Position(1)

  final case object right extends Position(2)

  final case object bottom extends Position(3)

}

object ViewMatchers extends ViewMatchers

//
//trait ViewSpec extends BeforeAndAfter {
//  a: ActivityTestCase =>
//  def setUp() {
//    a.setUp()
//    implicit val context = getActivity.getApplicationContext
//  }
//
//  def against(layoutCreation: () => View)(v: (Activity, View) => MatchResult) {
//    v(getActivity, layoutCreation())
//  }
//}
//
//object ViewSpec extends ActivityInstrumentationTestCase2[ActivityStub](classOf[ActivityStub]) with ViewSpec
//
