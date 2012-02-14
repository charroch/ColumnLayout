package novoda.android.scala

import android.os.Environment
import android.view.View
import android.graphics.Bitmap
import java.io.{PrintWriter, FileNotFoundException, FileOutputStream, File}
import org.scalatest.matchers.MatchResult

class Screenshotable {

  def take(v: View) {
    val mPath = Environment.getExternalStorageDirectory().toString() + "/" + "test.png";
    val v1 = v.getRootView();
    v1.setDrawingCacheEnabled(true);
    val bitmap = Bitmap.createBitmap(v1.getDrawingCache());
    v1.setDrawingCacheEnabled(false);
    val imageFile = new File(mPath);
    val fout = new FileOutputStream(imageFile);
    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
    fout.flush();
    fout.close();
  }
//
//  def withScreenshot(file: File)(op: () => MatchResult) {
//    val writer = new PrintWriter(file)
//    try {
//      op(writer)
//    } finally {
//      writer.close()
//    }
//  }

}
