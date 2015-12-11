package ru.onyanov.geni.network;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class PhotoLoader {
    private static final String TAG = "PhotoLoader";
    private static final String DIR_NAME = "geni";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private final String imageUri;
    private final ImageView imageView;
    private final ImageLoadCallback mCallback;
    private Context mContext;

    public PhotoLoader(String imageUri, ImageView imageView, Context context, ImageLoadCallback callback) {
        this.imageUri = imageUri;
        this.imageView = imageView;
        this.mCallback = callback;
        this.mContext = context;
    }

    public void load() {
        ImageLoader imageLoader = ImageLoader.getInstance();

        // Load image, decode it to Bitmap and display Bitmap in ImageView (or any other view
        //  which implements ImageAware interface)
        imageLoader.displayImage(imageUri, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                mCallback.onError();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                new SaveImageAsync(loadedImage, getFileName()).execute();
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {}
        });
    }

    class SaveImageAsync extends AsyncTask<String, String, String> {

        private String mFileName;
        private Bitmap mBitmap;

        public SaveImageAsync(Bitmap bitmap, String fileName) {
            mFileName = fileName;
            mBitmap = bitmap;
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteOutputStream);
                byte[] mbitmapdata = byteOutputStream.toByteArray();
                ByteArrayInputStream inputStream = new ByteArrayInputStream(mbitmapdata);

                String baseDir = getAlbumStorageDir(DIR_NAME).getAbsolutePath();

                String filePath = baseDir + File.separator + mFileName;



                OutputStream outputStream = new FileOutputStream(filePath);
                byteOutputStream.writeTo(outputStream);

                byte[] buffer = new byte[128]; //Use 1024 for better performance
                int lenghtOfFile = mbitmapdata.length;
                int totalWritten = 0;
                int bufferedBytes = 0;

                while ((bufferedBytes = inputStream.read(buffer)) > 0) {
                    totalWritten += bufferedBytes;
                    publishProgress(Integer.toString((int) ((totalWritten * 100) / lenghtOfFile)));
                    outputStream.write(buffer, 0, bufferedBytes);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();

                return filePath;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String filePath) {
            Log.d(TAG, "saved " + filePath);
            mCallback.onFileLoaded(filePath);
        }
    }

    private String getFileName() {
        String[] segments = imageUri.split("/");
        return segments[segments.length - 1];
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public File getAlbumStorageDir(String albumName) {
        Log.d(TAG, "State " + Environment.getExternalStorageState());
        // Get the directory for the user's public pictures directory.
        File pictureFolder = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
        );
        File file = new File(pictureFolder, albumName);

        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created " + file.getAbsolutePath());
        }
        return file;
    }


}
