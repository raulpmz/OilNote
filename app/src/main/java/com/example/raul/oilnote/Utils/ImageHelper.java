package com.example.raul.oilnote.Utils;

/**
 * Created by ptmarketing02 on 19/05/2017.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;

import static com.example.raul.oilnote.Utils.Codification.decodeBase64;

public class ImageHelper {
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels){
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, (float) pixels, (float) pixels, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap cropBitmapToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width) ? height - (height - width)
                : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0) ? 0 : cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0) ? 0 : cropH;

        return Bitmap.createBitmap(
                bitmap, cropW, cropH, newWidth, newHeight);
    }

    public static void rounderImage(Bitmap bitmap, ImageView imageView){

        Bitmap myBitmap = bitmap;
        // Llama al método encargado de cortar en forma cuadrada a la imagen:
        Bitmap croppedImage = ImageHelper.cropBitmapToSquare(myBitmap);

        // Llama al método encargado de redondear las esquinas de la imagen previamente cortada.
        // Recibe como parámetros el mapa de bits y el tamaño de sus lados en pixeles:
        Bitmap roundedCornersImage = ImageHelper.getRoundedCornerBitmap(
                croppedImage, 120);

        // Asigna el mapa de bits resultante a la vista ImageView que lo mostrará:
        imageView.setImageBitmap(roundedCornersImage);
    }
}
