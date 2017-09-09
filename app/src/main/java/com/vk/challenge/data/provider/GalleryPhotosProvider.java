package com.vk.challenge.data.provider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContentResolverCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikita on 09.09.17.
 */

public class GalleryPhotosProvider {

    @SuppressLint("StaticFieldLeak")
    private static GalleryPhotosProvider sInstance;

    private static  final String[] PROJECTION = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.DATA
    };

    private static final String SORT_ORDER =  MediaStore.Images.Media.DATE_MODIFIED+" DESC LIMIT 100";

    private static  final Uri CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;


    private Context mContext;


    public static GalleryPhotosProvider getInstance(Context context) {
        if (sInstance == null) {
            synchronized (GalleryPhotosProvider.class) {
                if (sInstance == null) {
                    sInstance = new GalleryPhotosProvider(context);
                }
            }
        }
        return sInstance;
    }

    private GalleryPhotosProvider(Context context) {
        mContext = context.getApplicationContext();
    }

    public List<String> getCameraPhotos() {
        Cursor cursor = ContentResolverCompat.query(mContext.getContentResolver(),
                CONTENT_URI, PROJECTION, null, null, SORT_ORDER, null);
        if (cursor == null) {
            return null;
        }
        List<String> images = new ArrayList<>();
        while (cursor.moveToNext()) {
            String bucket = cursor.getString(3);
            images.add(bucket);
        }
        return images;
    }
}
