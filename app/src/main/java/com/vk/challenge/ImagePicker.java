package com.vk.challenge;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nikita on 10.09.17.
 */

public class ImagePicker {

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss");

    private static final int CAMERA_REQ_CODE = 1;
    private static final int GALLERY_REQ_CODE = 2;

    private static  String sCurrentPhotoPath;

    public interface Callback {
        void onImagePicked(Uri uri, boolean fromCamera);

        void onImagePickError(String errorMessage, boolean fromCamera);

        void onImagePickCancel();
    }

    private Activity mActivity;

    private Callback mCallback;

    public ImagePicker(Activity activity) {
        mActivity = activity;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                notifyError("Error: can't create file", true);
                Log.e("TAG", ex.toString());
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                mActivity.startActivityForResult(takePictureIntent, CAMERA_REQ_CODE);
            } else {
                notifyError("Error: can't create file", true);
            }
        } else {
            notifyError("Error: can't find camera", true);
        }
    }

    public void pickFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        mActivity.startActivityForResult(Intent.createChooser(intent, mActivity.getString(R.string.pick_image)),
                GALLERY_REQ_CODE);
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQ_CODE) {
                galleryAddPic(sCurrentPhotoPath);
                notifyPicked(Uri.fromFile(new File(sCurrentPhotoPath)), true);
                return true;
            } else if (requestCode == GALLERY_REQ_CODE) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    notifyPicked(selectedImageUri, false);
                } else {
                    notifyError("Error: Image is null", false);
                }
                return false;
            }
        }else{
            if (requestCode == CAMERA_REQ_CODE || requestCode == GALLERY_REQ_CODE) {
                notifyCancel();
            }
        }
        return false;
    }

    private File createImageFile() throws IOException {
        String timeStamp = DATE_FORMAT.format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getAlbumDir();
        if (storageDir == null) {
            return null;
        }
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        sCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private File getAlbumDir() {
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), mActivity.getString(R.string.app_name));
            if (!storageDir.mkdirs()) {
                if (!storageDir.exists()) {
                    return null;
                }
            }
        }
        return storageDir;
    }

    private void notifyPicked(Uri uri, boolean fromCamera) {
        if (mCallback != null) {
            mCallback.onImagePicked(uri, fromCamera);
        }
    }

    private void notifyError(String errorMessage, boolean fromCamera) {
        if (mCallback != null) {
            mCallback.onImagePickError(errorMessage, fromCamera);
        }
    }

    private void notifyCancel(){
        if (mCallback != null) {
            mCallback.onImagePickCancel();
        }
    }

    private void galleryAddPic(String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        mActivity.sendBroadcast(mediaScanIntent);
    }


}
