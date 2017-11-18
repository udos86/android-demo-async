package de.udos.android_demo_async;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class InputStreamUtils {

    static String readJSON(InputStream inputStream) {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        try {

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {

            try {
                bufferedReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stringBuilder.toString();
    }

    static Bitmap readBitmap(InputStream inputStream) {

        return BitmapFactory.decodeStream(inputStream);
    }

    static Drawable readSVG(InputStream inputStream) {

        Drawable drawable = null;

        try {

            SVG svg = SVG.getFromInputStream(inputStream);
            drawable = new PictureDrawable(svg.renderToPicture());

        } catch (SVGParseException e) {
            e.printStackTrace();

        } finally {

            try {
                inputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return drawable;
    }
}
