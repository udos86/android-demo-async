package de.udos.android_demo_async;


import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpGetImageTask extends AsyncTask<String, Void, Drawable> {

    private static final String CONTENT_TYPE_BMP = "image/bmp";
    private static final String CONTENT_TYPE_JPG = "image/jpeg";
    private static final String CONTENT_TYPE_PNG = "image/png";
    private static final String CONTENT_TYPE_GIF = "image/gif";
    private static final String CONTENT_TYPE_SVG = "image/svg+xml";

    private OnTaskListener mListener = null;
    private WeakReference<ImageView> mImageViewWeakReference = null;
    private String mUrlString = null;

    HttpGetImageTask(OnTaskListener listener, ImageView imageView) {

        mListener = listener;
        mImageViewWeakReference = new WeakReference<>(imageView);
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected Drawable doInBackground(String... params) {

        mUrlString = params[0];
        HttpURLConnection connection = null;
        Drawable drawable = null;

        try {

            URL url = new URL(mUrlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2500);
            connection.setReadTimeout(10000);
            connection.setUseCaches(true);

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                String contentType = connection.getContentType();

                switch (contentType) {

                    case CONTENT_TYPE_BMP:
                    case CONTENT_TYPE_JPG:
                    case CONTENT_TYPE_GIF:
                    case CONTENT_TYPE_PNG:

                        drawable = new BitmapDrawable(null,
                                InputStreamUtils.readBitmap(connection.getInputStream()));
                        break;

                    case CONTENT_TYPE_SVG:

                        drawable = InputStreamUtils.readSVG(connection.getInputStream());
                        break;

                    default:

                        throw new IOException(HttpGetImageTask.class.toString()
                                + " unexpected content type " + connection.getContentType() + " for url: "
                                + url.toString());
                }

            } else {

                throw new IOException(HttpGetImageTask.class.toString()
                        + " could not establish successful http connection for url: "
                        + url.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }

        return drawable;
    }

    @Override
    protected void onPostExecute(Drawable drawable) {

        super.onPostExecute(drawable);

        ImageView imageView = null;

        if (mImageViewWeakReference != null) {

            imageView = mImageViewWeakReference.get();
        }

        if (mListener != null) {

            mListener.onImageLoaded(drawable, imageView, mUrlString);
        }
    }

    public interface OnTaskListener {

        void onImageLoaded(Drawable drawable, ImageView imageView, String url);
    }
}
