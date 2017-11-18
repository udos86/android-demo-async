package de.udos.android_demo_async;


import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpGetJsonTask extends AsyncTask<String, Void, String> {

    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_JAVASCRIPT = "text/javascript; charset=utf-8";

    private OnTaskListener mListener = null;

    HttpGetJsonTask(OnTaskListener listener) {

        mListener = listener;
    }

    @Override
    protected String doInBackground(String... params) {

        HttpURLConnection connection = null;
        String json = null;

        try {

            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2500);
            connection.setReadTimeout(10000);
            connection.setUseCaches(false);

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                String contentType = connection.getContentType();

                if (contentType.contains(CONTENT_TYPE_JSON) ||contentType.contains(CONTENT_TYPE_JAVASCRIPT)) {

                    json = InputStreamUtils.readJSON(connection.getInputStream());

                } else {

                    throw new IOException(HttpGetJsonTask.class.toString()
                            + " unexpected content type " + connection.getContentType() + " "
                            + url.toString());
                }

            } else {

                throw new IOException(HttpGetJsonTask.class.toString()
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

        return json;
    }

    @Override
    protected void onPostExecute(String json) {

        super.onPostExecute(json);
        mListener.onJsonLoaded(json);
    }

    public interface OnTaskListener {

        void onJsonLoaded(String json);
    }
}
