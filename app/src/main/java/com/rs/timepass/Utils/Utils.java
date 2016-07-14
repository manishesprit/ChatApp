package com.rs.timepass.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.Toast;

import com.urbanairship.UAirship;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by admin on 4/5/16.
 */
public class Utils {


    public static boolean isOnline(Context context) {
        try {
            if (context == null)
                return false;

            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (cm != null) {
                if (cm.getActiveNetworkInfo() != null) {
                    return cm.getActiveNetworkInfo().isConnected();
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.error("Exception", e);
            return false;
        }
    }

    public static String getDeviceID(Context context) {
        String udid = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Pref.setValue(context, Config.PREF_UDID, udid);
        return udid;
    }

    public static void setDefaultRoundImage(Context context, ImageView imageView, int imageResource) {
        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), BitmapFactory.decodeResource(context.getResources(), imageResource));
        circularBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(null);
        imageView.setImageDrawable(circularBitmapDrawable);
    }

    public static void addActivities(String actName, Activity _activity) {
        if (Config.screenStack == null)
            Config.screenStack = new HashMap<String, Activity>();
        if (_activity != null)
            Config.screenStack.put(actName, _activity);
    }

    public static void removeActivity(String key) {
        if (Config.screenStack != null && Config.screenStack.size() > 0) {
            Activity _activity = Config.screenStack.get(key);
            if (_activity != null) {
                freeMemory();
                _activity.finish();
            }
        }
    }

    public static void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getDeviceWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public static void createDir(String file) {
        File dir = new File(file);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        dir = null;
    }

    public static void deleteFile(String file) {
        File dir = new File(file);
        if (dir.exists()) {
            Log.print("========= DELETE FILE ========" + file.toString());
            dir.delete();
        }
        dir = null;
    }

    @SuppressLint("SimpleDateFormat")
    public static Date convertStringToDate(String strDate, String parseFormat) {
        try {
            return new SimpleDateFormat(parseFormat).parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String convertDateToString(Date strDate, String parseFormat) {
        try {
            return new SimpleDateFormat(parseFormat).format(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertDateStringToString(String strDate,
                                                   String currentFormat, String parseFormat) {
        try {
            return convertDateToString(
                    convertStringToDate(strDate, currentFormat), parseFormat);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String compressImage(String imageUri, Context context, int filetype) {
        String filePath = getRealPathFromURI(imageUri, context);
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        // max Height and width values of the compressed image is taken as
        // 816x612

        Log.print("actualHeight :: " + actualHeight);
        Log.print(" actualWidth :: " + actualWidth);

        float maxWidth;
        float maxHeight;
        // 1= avatar 2=feed
        if (filetype == 1) {
            maxWidth = 800;
            maxHeight = 800;
        } else {
            maxWidth = 1000;
            maxHeight = 1000;
        }

        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;

        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
                    Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                middleY - bmp.getHeight() / 2, new Paint(
                        Paint.FILTER_BITMAP_FLAG));

        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = new File(imageUri).getPath();
        try {
            Storage.verifyCategoryPath(Config.DIR_USERDATA);
            Storage.verifyCategoryPath(Config.DIR_FEEDDATA);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            out = new FileOutputStream(filename);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filename;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    private static String getRealPathFromURI(String contentURI, Context context) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = context.getContentResolver().query(contentUri, null,
                null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public static Bitmap rotateBitmap(Bitmap b, int degrees) {

        Log.print("rotateBitmap=====>>" + b.getWidth() + ":::" + b.getHeight() + ":::" + degrees);
        Matrix m = new Matrix();
        if (degrees != 0) {
            // clockwise
            m.postRotate(degrees, (float) b.getWidth() / 2,
                    (float) b.getHeight() / 2);
        }
        try {
            Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
                    b.getHeight(), m, true);
            if (b != b2) {
                b.recycle();
                b = b2;
            }
        } catch (OutOfMemoryError ex) {
            // We have no memory to rotate. Return the original bitmap.
        }
        return b;
    }


    public static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.print("RotateImage Exif orientation: " + orientation);
            Log.print("RotateImage Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static void DownloadImage(Context context, Bitmap bitmap, String name, String path) {
        Log.print("width==" + bitmap.getWidth() + "===height===" + bitmap.getHeight());
        try {

            Storage.verifyDataPath();

            File imageFile = new File(path, name);

            OutputStream os;
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
            Toast.makeText(context, "Download complete", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.print("Error writing bitmap" + e);
        }
    }

    public static void setPushId(Application application) {
        try {
            Log.print("=========== EXIST PUSH ID ======" + Pref.getValue(application.getApplicationContext(), Config.PREF_URBUN_PUSH_ID, null));
            if (Pref.getValue(application.getApplicationContext(), Config.PREF_URBUN_PUSH_ID, null) == null ||
                    Pref.getValue(application.getApplicationContext(), Config.PREF_URBUN_PUSH_ID, "").equals("")) {
                UAirship.takeOff(application, new UAirship.OnReadyCallback() {
                    @Override
                    public void onAirshipReady(UAirship uAirship) {
                        uAirship.shared().getPushManager().setUserNotificationsEnabled(true);
                    }
                });

                if (UAirship.shared().getPushManager().getChannelId() != null) {
                    Pref.setValue(application.getApplicationContext(), Config.PREF_URBUN_PUSH_ID, UAirship.shared().getPushManager().getChannelId().toString());
                }

                String channelId = UAirship.shared().getPushManager().getChannelId();
                Log.print("============My Application Channel ID: ============" + channelId);

                MyCustomeNotificationFactory myCustomeNotificationFactory = new MyCustomeNotificationFactory(application.getApplicationContext());
                UAirship.shared().getPushManager().setNotificationFactory(myCustomeNotificationFactory);
            }
        } catch (Exception e) {
            Log.error(application.getApplicationContext().getClass().getName() + "GetGCM()", e);
        }
    }

    public static void ClearPref(Context context) {
        Pref.setValue(context, Config.PREF_USER_ID, 0);
        Pref.setValue(context, Config.PREF_USER_FB_ID, "");
        Pref.setValue(context, Config.PREF_USERNAME, "");
        Pref.setValue(context, Config.PREF_NAME, "");
        Pref.setValue(context, Config.PREF_CITY, "");
        Pref.setValue(context, Config.PREF_STATUS, "");
        Pref.setValue(context, Config.PREF_EMAIL, "");
        Pref.setValue(context, Config.PREF_MOBILE, "");
        Pref.setValue(context, Config.PREF_AVATAR, "");
        Pref.setValue(context, Config.PREF_UDID, "");
        Pref.setValue(context, Config.PREF_NOOFPOST, 0);
        Pref.setValue(context, Config.PREF_NOOFFOLLOWER, 0);
        Pref.setValue(context, Config.PREF_NOOFFOLLING, 0);
        Pref.setValue(context, Config.PREF_ISFIRSTTIME, 0);
    }

    public final static Pattern EMAIL_ADDRESS_PATTERN = Pattern
            .compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$");
}
