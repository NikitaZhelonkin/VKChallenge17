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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        GalleryItem that = (GalleryItem) object;

        return mUri != null ? mUri.equals(that.mUri) : that.mUri == null;

    }

    @Override
    public int hashCode() {
        return mUri != null ? mUri.hashCode() : 0;
    }
}
