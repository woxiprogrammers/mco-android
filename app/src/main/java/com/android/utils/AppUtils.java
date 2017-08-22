package com.android.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

import io.realm.Realm;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class AppUtils {
    private static SharedPreferences preference;
    private static SharedPreferences.Editor editor;
    public static AppUtils instance;
    public static Context mContext;
    private Realm realm;
    private final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9+._%-+]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
                    "(" +
                    "." +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
                    ")+"
    );

    /**
     * initialize utils plus library
     *
     * @param context
     * @param prefs_name
     */
    public static void initialize(Context context, String prefs_name) {
        if (prefs_name == null) {
            throw new IllegalArgumentException("Preference name can not be null");
        }
        if (prefs_name.isEmpty()) {
            throw new IllegalArgumentException("Preference name can not be empty");
        }
        if (context == null) {
            throw new IllegalArgumentException("Preference name can not be null");
        }
        mContext = context;
        preference = context.getSharedPreferences(prefs_name, context.MODE_PRIVATE);
        editor = preference.edit();
    }

    /**
     * initialize Utils Plus Library
     *
     * @param context
     */
    public static void initialize(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Preference name can not be null");
        }
        String DEFAULT_SHARED_PREFS_NAME = "app_preferences";
        preference = context.getSharedPreferences(DEFAULT_SHARED_PREFS_NAME, context.MODE_PRIVATE);
        editor = preference.edit();
        mContext = context;
    }

    public static synchronized AppUtils getInstance() {
        if (instance == null) {
            return instance = new AppUtils();
        }
        return instance;
    }

    public AppUtils put(String key, Object obj) {
        // if(obj instanceof Float)
        if (obj instanceof String) {
            editor.putString(key, (String) obj);
            editor.commit();
            return this;
        } else if (obj instanceof Boolean) {
            editor.putBoolean(key, (Boolean) obj);
            editor.commit();
            return this;
        } else if (obj instanceof Integer) {
            editor.putInt(key, (Integer) obj);
            editor.commit();
            return this;
        } else if (obj instanceof Long) {
            editor.putLong(key, (Long) obj);
            editor.commit();
            return this;
        } else if (obj instanceof Float) {
            editor.putFloat(key, (Float) obj);
            editor.commit();
            return this;
        } else {
            throw new ClassCastException(obj.getClass().getName() + " is not allowed type of object.");
        }
    }

    public AppUtils insertStringSet(String key, Set<String> values) {
        editor.putStringSet(key, values);
        return this;
    }

    public Set<String> retrievetStringSet(String key, Set<String> defaultValue) {
        return preference.getStringSet(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return preference.getBoolean(key, defaultValue);
    }

    public String getString(String key, String defaultValue) {
        return preference.getString(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return preference.getInt(key, defaultValue);
    }

    public float getFloat(String key, float defaultValue) {
        return preference.getFloat(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return preference.getLong(key, defaultValue);
    }

    /**
     * removes specified preference using its name
     *
     * @param key name of the key
     * @return object
     */
    public AppUtils removeKey(String key) {
        editor.remove(key);
        editor.commit();
        return this;
    }

    /**
     * this utlity method clears all preferences
     *
     * @return object
     */
    public AppUtils clear() {
        editor.clear();
        editor.commit();
        return this;
    }

    /**
     * this utility method helps to save images in internal memory. Since the image is saved in the internal memory, the image is private
     *
     * @param bitmap    image in bitmap form
     * @param name      name of the file
     * @param type      format of the image. only png and jpg formats are allowed. parameter should be of following format. "png" or "jpg"
     * @param path      path of the folder in which image to be saved. sample path format: /folder1/folder2/folder3/
     * @param quality   quality of the image
     * @param isPrivate
     * @return path in which image is saved
     */
    public String SaveImage(Bitmap bitmap, String name, String type, String path, int quality, boolean isPrivate) {
        File file_path = null;
        File dir2 = null;
        String a = File.separator;
        if (path == null || path.isEmpty() || path.length() <= 3 || !path.contains("/")) {
            Log.e("Utils Plus", "Folder path seems to be incorrect. Please correct the path");
        }
        if (type == null || type.isEmpty()) {
            Log.e("Utils Plus", "Image type can not be null or empty.");
        }
        if (name == null || name.isEmpty()) {
            Log.e("Utils Plus", "Image name can not be null or empty.");
        }
        if (quality < 0 || quality > 100) {
            Log.e("Utils Plus", "Quality must be greater than 0 and less than 100");
        }
        if (type != null) {
            if (!type.equalsIgnoreCase("PNG") && !type.equalsIgnoreCase("JPG")) {
                Log.e("Utils Plus", "format not supported except png and jpg");
            }
        }
        if (type.equalsIgnoreCase("PNG")) {
            name = name + ".png";
        } else if (type.equalsIgnoreCase("JPG")) {
            name = name + ".jpg";
        }
        if (!isPrivate) {
            File rootDir = null;
            path = path.replaceAll("//*", "/");
            String[] folders = path.split("/");
            for (int i = 0; i < folders.length; i++) {
                if (i == 0) {
                    rootDir = mContext.getDir(folders[i], Context.MODE_APPEND); //Creating an internal dir;
                    if (!rootDir.exists()) {
                        rootDir.mkdirs();
                    }
                } else {
                    rootDir = new File(rootDir, folders[i]);
                    rootDir.mkdir();
                }
            }
            return rootDir.getAbsolutePath();
        }
        ContextWrapper cw = new ContextWrapper(mContext);
        File dir = cw.getFilesDir();
        dir2 = new File(dir, path);
        dir2.mkdirs();
        file_path = new File(dir2, name);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file_path);
            // Use the compress method on the BitMap object to write image to the OutputStream
            if (type.equalsIgnoreCase("PNG")) {
                bitmap.compress(Bitmap.CompressFormat.PNG, quality, fos);
            } else if (type.equalsIgnoreCase("JPG")) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dir2.getAbsolutePath();
    }

    /**
     * Checks whether there is an active network connection or not
     *
     * @return true or false
     */
    public boolean checkNetworkState() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    /**
     * converts image of the form "drawable" to "bitmap"
     *
     * @param drawable drawable resource
     * @return bitmap obtained from drawable
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * validates email
     *
     * @param email email address
     * @return true or false.
     */
    public boolean checkEmailIsValid(String email) {
        if (email != null && !email.isEmpty()) {
            return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
        } else {
            Log.e("Utils Plus", "email can not be null or empty.");
            return false;
        }
    }

    /**
     * sendEmail method is used to send email from available email clients installed in the app
     *
     * @param chooserTitle title of the client. Can not be null
     * @param subject      email subject. Can not be null.
     * @param body         body of the email. Can not be null
     * @param recipients   list of recipients. For example, "abc@xyz.com, xyz@dfc.com". this parameter can not be null
     */
    public void sendEmail(String chooserTitle, String subject, String body, String... recipients) {
        checkNull("chooserTitle not allowed to be null", chooserTitle);
        checkNull("subject not allowed to be null", subject);
        checkNull("body not allowed to be null", body);
        checkNull("recipients not allowed to be null", recipients);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        try {
            Intent chooserIntent = Intent.createChooser(intent, chooserTitle);
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(chooserIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Log.d("Utils Plus", "Activity Not Found");
        }
    }

    private static <T> T checkNull(String message, T object) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    public Realm getRealmInstance() {
        if (realm == null || realm.isClosed()) {
            realm = Realm.getDefaultInstance();
        }
        return realm;
    }
}
