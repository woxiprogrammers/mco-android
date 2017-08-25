package com.android.inventory;



import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * This class is used to Pick and Crop Image using Camera/Gallery application.
 * This class uses- "Android-Image-Cropper" by "ArthurHub" library to crop image.
 * Library github link - "https://github.com/ArthurHub/Android-Image-Cropper"
 * Created by RohitSS
 */
public class ImageUtilityHelper {
    public Bitmap bitmapProfile = null;
    private Context mContext;
    private File localImageFile;
    private boolean isDeleteLocalImage;

    public ImageUtilityHelper(Context context) {
        mContext = context;
    }

    /**
     * Used to create image chooser intent
     *
     * @return chooserIntent
     */
    public Intent getPickImageChooserIntent() {
        File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
        Uri outputFileUri = Uri.fromFile(f);
        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = mContext.getPackageManager();
        // collect all camera intents
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }
        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }
        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);
        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
        return chooserIntent;
    }

    /**
     * Used to set preferences for Android-Image-Cropper UI.
     *
     * @param fileUri Uri of image to crop
     */
    private void getSetCropImage(Uri fileUri) {
        CropImage.activity(fileUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropWindowSize(100, 100)
                .setAspectRatio(1, 1)
                .setFixAspectRatio(true)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start((Activity) mContext);
    }

    public void onSelectionResult(int requestCode, int resultCode, Intent data) {
        //For Android-Image-Cropper
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmapProfile = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                isDeleteLocalImage = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(mContext, "" + error, Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 2612) {
            boolean isCamera;
            if (data == null) {
                isCamera = true;
            } else {
                final String action = data.getAction();
                isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
            }
            if (isCamera) {
                //Result From Camera
                localImageFile = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : localImageFile.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        localImageFile = temp;
                        break;
                    }
                }
                Uri selectedFileUri = Uri.fromFile(localImageFile);
                //call image crop activity
                getSetCropImage(selectedFileUri);
            } else {
                //Result From Gallery
                Uri selectedImageUri = data.getData();
                //call image crop activity
                getSetCropImage(selectedImageUri);
            }
        }
    }

    /**
     * Delete local/temporary image file only after it is cropped.
     */
    public void deleteLocalImage() {
        try {
            if (isDeleteLocalImage && localImageFile != null) {
                localImageFile.delete();
                isDeleteLocalImage = false;
            } else {
                isDeleteLocalImage = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
