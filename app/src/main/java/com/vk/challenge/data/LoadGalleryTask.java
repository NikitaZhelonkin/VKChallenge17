package com.vk.challenge.data;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.vk.challenge.data.model.GalleryItem;
import com.vk.challenge.data.provider.GalleryPhotosProvider;
import com.vk.challenge.utils.ListUtils;

import java.util.List;

/**
 * Created by nikita on 09.09.17.
 */

public class LoadGalleryTask extends AsyncTask<Void, Void, List<GalleryItem>> {

    public interface Callback {
        void onGalleryLoaded(List<GalleryItem> items);
    }

    private LoadGalleryTask.Callback mCallback;

    private GalleryPhotosProvider mGalleryPhotosProvider;

    public LoadGalleryTask(Context context){
        mGalleryPhotosProvider = GalleryPhotosProvider.getInstance(context);
    }

    public void setCallback(LoadGalleryTask.Callback callback) {
        mCallback = callback;
    }

    @Override
    protected List<GalleryItem> doInBackground(Void... params) {
        List<String> photos = mGalleryPhotosProvider.getCameraPhotos();
        if (photos == null) {
            return null;
        }
        return ListUtils.map(photos, new ListUtils.Map<String, GalleryItem>() {
            @Override
            public GalleryItem map(String photo) {
                return new GalleryItem(Uri.parse("file://"+photo));
            }
        });
    }

    @Override
    protected void onPostExecute(List<GalleryItem> items) {
        if (mCallback != null) {
            mCallback.onGalleryLoaded(items);
        }
    }
}
