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

    private static Uri sCurrentPhotoUri;
    private static String sCurrentPhotoPath;

    public interface Callback {
        void onImagePicked(Uri uri, boolean fromCamera);

        void onImagePickError(String errorMessage, boolean fromCamera);
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
                notifyError("Error: can't create temp file", true);
                Log.e("TAG", ex.toString());
            }
            if (photoFile != null) {
                sCurrentPhotoUri = FileProvider.getUriForFile(mActivity,
                        "com.vk.challenge.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, sCurrentPhotoUri);
                mActivity.startActivityForResult(takePictureIntent, CAMERA_REQ_CODE);
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
                notifyPicked(sCurrentPhotoUri, true);
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
        }
        return false;
    }

    private File createImageFile() throws IOException {
        String timeStamp = DATE_FORMAT.format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        sCurrentPhotoPath = image.getAbsolutePath();
        return image;
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

    private void galleryAddPic(String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        mActivity.sendBroadcast(mediaScanIntent);
    }


}
