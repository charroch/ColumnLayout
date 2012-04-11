package novoda.widget

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.WordSpec
import android.test.AndroidTestCase
import android.widget.TextView
import novoda.widget.ColumnLayout.LayoutParams
import android.view.ViewGroup

class ColumnSpec extends AndroidSpec with WordSpec with ShouldMatchers with TextLayoutUtil {
  "A column" should {

    def layoutParams = {
      val lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
      lp.columnIndex = 0
      lp
    }

    def textView(s: String)(p: TextView => LayoutParams): TextView = {
      val tv = new TextView(getContext)
      tv.setText(s)
      tv.setLayoutParams(p(tv))
      tv
    }

    "compute height correctly" in {
      val cl = new ColumnLayout(getContext)

      val t = new TextView(getContext)
      t.setText("hello world")
      t.setHeight(200)
      t.setLayoutParams(layoutParams)

      val t2 = new TextView(getContext)
      t2.setText("hello world")
      t2.setHeight(200)
      t2.setLayoutParams(layoutParams)


      val column = new cl.Column(0, 200, 500, 10, new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT))
      column.measure(t)
      column.measure(t2)

      column.measuredUsedHeight should be(400)
    }

    "compute height correctly with margin bottom" in {
      val cl = new ColumnLayout(getContext)

      val t = new TextView(getContext)
      t.setText("hello world")
      t.setHeight(200)
      val lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
      lp.columnIndex = 0
      lp.setMargins(0, 0, 0, 10)
      t.setLayoutParams(lp)

      val t2 = new TextView(getContext)
      t2.setText("hello world")
      t2.setHeight(200)
      val lp2 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
      lp2.columnIndex = 0
      t2.setLayoutParams(lp2)


      val column = new cl.Column(0, 200, 500, 10, new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT))
      column.measure(t)
      column.measure(t2)

      column.measuredUsedHeight should be(410)
    }

    "compute height correctly with margin top" in {
      val t = new TextView(getContext)

      t.setText("hello world")
      t.setHeight(200)
      val lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
      lp.columnIndex = 0
      lp.setMargins(0, 15, 0, 0)
      t.setLayoutParams(lp)
      val t2 = new TextView(getContext)

      t2.setText("hello world")
      t2.setHeight(200)
      val lp2 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
      lp2.columnIndex = 0
      t2.setLayoutParams(lp2)

      val cl = new ColumnLayout(getContext)
      val column = new cl.Column(0, 200, 500, 10, new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT))
      column.measure(t)
      column.measure(t2)

      column.measuredUsedHeight should be(415)
    }


    "consider parent margin top" in {
      val t = textView("hello world") {
        (v: TextView) =>
          v.setHeight(200)
          val lp2 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
          lp2
      }
      val lp = new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT)
      lp.setMargins(0, 20, 0, 0)
      val cl = new ColumnLayout(getContext)
      val column = new cl.Column(0, 200, 500, 10, lp)
      column.measure(t)
      column.measuredUsedHeight should be(220)
    }

    "compute height given padding" in {
      val t = textView("hello world") {
        (v: TextView) =>
          v.setHeight(200)
          v.setPadding(0, 10, 0, 0)
          val lp2 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
          lp2
      }

      val t2 = textView("hello world") {
        (v: TextView) =>
          v.setHeight(200)
          v.setPadding(0, 0, 0, 0)
          val lp2 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
          lp2
      }

      val cl = new ColumnLayout(getContext)
      val column = new cl.Column(0, 200, 500, 10, new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT))
      column.measure(t)
      column.measure(t2)
      column.measuredUsedHeight should be(400)

      val cl2 = new ColumnLayout(getContext)
      cl2.setPadding(0, 10, 0, 0)
      val column2 = new cl2.Column(0, 200, 500, 10, new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT))
      column2.measure(t)
      column2.measure(t2)
      column2.measuredUsedHeight should be(400)
      column2.getAvailableHeight should be(90)
    }


    "layout" should {
      "consider margin" in {
        val t = textView("hello world") {
          (v: TextView) =>
            v.setHeight(200)
            v.setPadding(0, 10, 0, 0)
            val lp2 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            lp2.setMargins(0, 0, 200, 20)
            lp2
        }

        val t2 = textView("hello world") {
          (v: TextView) =>
            v.setHeight(200)
            v.setPadding(0, 0, 0, 0)
            val lp2 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            lp2
        }



        val cl2 = new ColumnLayout(getContext)
        val column2 = new cl2.Column(0, 200, 500, 10, new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT))
        column2.measure(t)
        column2.measure(t2)

        column2.layout(t, 0, 0, 0, 500)
        column2.layout(t2, 0, 0, 0, 500)
      }
    }
  }
}

trait AndroidSpec extends AndroidTestCase

