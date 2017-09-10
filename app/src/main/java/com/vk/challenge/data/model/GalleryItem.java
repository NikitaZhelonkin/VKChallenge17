package com.vk.challenge.data.model;

import android.net.Uri;

/**
 * Created by nikita on 09.09.17.
 */

public class GalleryItem {

    private Uri mUri;

    public GalleryItem(Uri uri){
        mUri = uri;
    }

    public Uri getUri() {
        return mUri;
    }
}
