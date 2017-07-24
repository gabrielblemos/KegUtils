package com.keg.kegutils.Utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by gamer on 12/07/2017.
 */

public class UtilsImagem {

    /**
     * Com auxílio dos links abaixo:
     *      http://stackoverflow.com/questions/8471226/how-to-resize-image-bitmap-to-a-given-size
     *      http://stackoverflow.com/questions/6908604/android-crop-center-of-bitmap
     */

    public static Bitmap customFitBitmap(Bitmap bitmap, int nWidth, int nHeight) {

        if (bitmap == null || nWidth == 0 || nHeight == 0 ) {
            return bitmap;
        }

        int width = bitmap.getWidth();
        int heigth = bitmap.getHeight();
        Bitmap output;

        if (width == nWidth && heigth == nHeight) {
            return bitmap;
        } else if ((width == nWidth && heigth > nHeight)
                || (heigth == nHeight && width > nWidth)) {
            output = cropBitmap(bitmap, nWidth, nHeight);
        } else if (width > nWidth && heigth > nHeight) {
            output = scaleBitmapDown(bitmap, nWidth, nHeight);
        } else if (width < nWidth || heigth < nHeight) {
            output = scaleBitmapUp(bitmap, nWidth, nHeight);
        } else {
            throw new RuntimeException("Algum caso não foi tratado");
        }
        return customFitBitmap(output, nWidth, nHeight);
    }

    private static Bitmap cropBitmap(Bitmap bitmap, int nWidth, int nHeight) {
        Bitmap output;
        int paddingHeight = (bitmap.getHeight() - nHeight) / 2;
        int paddingWidth = (bitmap.getWidth() - nWidth) / 2;

        output = Bitmap.createBitmap(bitmap, paddingWidth, paddingHeight, nWidth, nHeight);
        return output ;
    }

    private static Bitmap scaleBitmapDown(Bitmap bitmap, int nWidth, int nHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float escalaWidth = (float) nWidth / width;
        float escalaHeight = (float) nHeight / height;
        float escala = Math.min(escalaWidth, escalaHeight);

        int tempWidth = (int) (width * escala);
        int tempHeight = (int) (height * escala);

        Bitmap output = Bitmap.createBitmap(tempWidth, tempHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix matrix = new Matrix();

        matrix.setScale(escala, escala);
        canvas.drawBitmap(bitmap, matrix, new Paint());
        return output;
    }

    private static Bitmap scaleBitmapUp(Bitmap bitmap, int nWidth, int nHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float escalaWidth = (float) nWidth / width;
        float escalaHeight = (float) nHeight / height;
        float escala = Math.max(escalaHeight, escalaWidth);

        int tempWidth = (int) (width * escala);
        int tempHeight = (int) (height * escala);

        Bitmap output = Bitmap.createBitmap(tempWidth, tempHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix matrix = new Matrix();

        matrix.setScale(escala, escala);
        canvas.drawBitmap(bitmap, matrix, new Paint());
        return output;
    }

    public static Bitmap getMapPin(int idResource, Resources resources) {
        Bitmap returnBitmap = null;
        if (idResource <= 0) {
            return returnBitmap;
        }

        // Configurando bitmap.
        final int PADDING = 10;
        Canvas canvas = new Canvas();
        Drawable drawable = resources.getDrawable(idResource);
        returnBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth() + PADDING,
                drawable.getIntrinsicHeight() + PADDING, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(returnBitmap);

        // Desenhando circulo
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        float centerPoint = returnBitmap.getWidth()/2;
        canvas.drawCircle(centerPoint, centerPoint, centerPoint, paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(centerPoint, centerPoint, centerPoint-2, paint);

        // Desenhando drawable centralizado
        drawable.setBounds(PADDING, PADDING, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return returnBitmap;
    }

    public static Bitmap roundedBitmap(Bitmap bitmap) {
        Bitmap returnBitmap = null;
        if (bitmap == null) {
            return returnBitmap;
        }

        int diameter = bitmap.getWidth();
        if (bitmap.getHeight() == bitmap.getWidth()) {
            returnBitmap = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(returnBitmap);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(0xff424242);

            canvas.drawARGB(0, 0, 0, 0);
            int centerPoint = diameter / 2;
            canvas.drawCircle(centerPoint, centerPoint, diameter / 2, paint);

            Rect rect = new Rect(0, 0, diameter, diameter);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
        }

        return returnBitmap;
    }
}