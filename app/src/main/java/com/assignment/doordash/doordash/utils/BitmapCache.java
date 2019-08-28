package com.assignment.doordash.doordash.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;

import com.assignment.doordash.doordash.interfaces.onBitmapDownloadedListener;
import com.assignment.doordash.doordash.network.RestaurantApi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class BitmapCache {

    private static final String TAG = BitmapCache.class.getName();

    private LruCache<String, Bitmap> cache;
    private static BitmapCache instance;

    private static final Object obj = new Object();

    public static BitmapCache getInstance() {
        synchronized (obj) {
            if (instance == null) {
                instance = new BitmapCache();
            }
            return instance;
        }
    }

    private BitmapCache() {
        cache = new LruCache<>(40);
    }

    public void get(String url, onBitmapDownloadedListener listener) {
        Bitmap bm = cache.get(url);;
        if (bm == null) {
            Log.i(TAG, "Url: " + url);
            new ImageDownloaderTask(url, listener).execute();
            return;
        }
        listener.onBitmapDownloaded(url, bm);
    }

    private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private Bitmap decodeImage(InputStream in, int reqWidth, int reqHeight) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageArray = null;
        try {
            int n = 0;
            byte[] buffer = new byte[1024];
            while ((n = in.read(buffer)) > 0) {
                baos.write(buffer, 0, n);
            }
            imageArray = baos.toByteArray();
        } catch (IOException ioe) {

        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length, options);
        int inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length, options);
    }

    class ImageDownloaderTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private onBitmapDownloadedListener listener;

        public ImageDownloaderTask(String url, onBitmapDownloadedListener listener) {
            this.url = url;
            this.listener = listener;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            RestaurantApi api = RestaurantApi.retrofit.create(RestaurantApi.class);
            Call<ResponseBody> call = api.getImage(url);
            try {
                ResponseBody response = call.execute().body();
                return decodeImage(response.byteStream(), 64, 64);
            } catch (IOException ioe) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                cache.put(url, bitmap);
            }
            listener.onBitmapDownloaded(url, bitmap);
        }
    }
}
