package novoda.android.scala

import org.scalatest.Payloads
import android.test.ActivityTestCase
import android.os.Environment
import android.graphics.Bitmap
import java.io.{FileOutputStream, File}

trait Screenshots extends Payloads {
//  v: ActivityTestCase =>
//
//  def take: File = {
//    val mPath = Environment.getExternalStorageDirectory().toString() + "/" + "test.png";
//    val v1 = v.getActivity.getWindow.getDecorView.getRootView();
//    v1.setDrawingCacheEnabled(true);
//    val bitmap = Bitmap.createBitmap(v1.getDrawingCache());
//    v1.setDrawingCacheEnabled(false);
//    val imageFile = new File(mPath);
//    val fout = new FileOutputStream(imageFile);
//    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
//    fout.flush();
//    fout.close();
//    imageFile
//  }
//
//  def withScreenshot(fun: => Unit) {
//    withPayload(take)(fun)
//  }
}
