//resources used
//https://www.geeksforgeeks.org/how-to-generate-qr-code-in-android/
//https://stackoverflow.com/questions/5663671/creating-an-empty-bitmap-and-drawing-though-canvas-in-android
package com.example.qrun;

import static android.content.Context.WINDOW_SERVICE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRGenerator {
    /**
     * Generate the QR Bitmap by its String
     * @param Text the raw Text String
     * @param context the context in which this QR is located
     * @return Bitmap class for displaying
     */
    public static Bitmap generateQRBitmap(String Text, Context context){

        Bitmap QRBitmap;

        //adapted from geeks for geeks

        WindowManager manager = (WindowManager) context.getSystemService(WINDOW_SERVICE);

        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;

        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        QRBitmap = Bitmap.createBitmap(width, height, conf);

        // generating dimension from width and height.
        int dimensions = width < height ? width : height;
        dimensions = dimensions * 3 / 4;


        QRGEncoder qrgEncoder = new QRGEncoder(Text, null, QRGContents.Type.TEXT, dimensions);

        try{
            //get qr code as bitmap
            QRBitmap = qrgEncoder.encodeAsBitmap();

        }
        catch (WriterException e){
            // this method is called for
            // exception handling.
            Log.e("WriteExceptionTag", e.toString());
        }

        return QRBitmap;

    }
}
