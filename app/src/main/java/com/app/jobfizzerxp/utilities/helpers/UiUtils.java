package com.app.jobfizzerxp.utilities.helpers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.jobfizzerxp.utilities.image_compressor.Compressor;
import com.app.jobfizzerxp.utilities.uiUtils.LoaderDialog;
import com.app.jobfizzerxp.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.app.jobfizzerxp.utilities.helpers.Constants.PERMISSIONS.CAMERA_STORAGE_PERMISSIONS;
import static com.app.jobfizzerxp.utilities.helpers.Constants.PERMISSIONS.READ_STORAGE_PERMISSIONS;
import static com.app.jobfizzerxp.utilities.helpers.Constants.REQUEST_CODE.CAMERA_INTENT;
import static com.app.jobfizzerxp.utilities.helpers.Constants.REQUEST_CODE.GALLERY_INTENT;

public class UiUtils {
    private static String TAG = UiUtils.class.getSimpleName();
    private static Boolean isShowing = false;
    private static LoaderDialog loaderDialog;


    public static void toast(View view, CharSequence toastMsg) {
        Toast.makeText(view.getContext(), toastMsg, Toast.LENGTH_SHORT).show();
    }

    public static void snackBar(View view, CharSequence toastMsg) {
        Snackbar snackbar = Snackbar.make(view, toastMsg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public static void closeKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static boolean onTouch(float startX, float endX, float startY, float endY) {
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);
        int CLICK_ACTION_THRESHOLD = 200;
        return !(differenceX > CLICK_ACTION_THRESHOLD/* =5 */ || differenceY > CLICK_ACTION_THRESHOLD);
    }

    public static void setProfilePicture(Context context, ImageView profilePic) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.profile_pic);
        drawable.setColorFilter(new PorterDuffColorFilter(getPrimaryColor(context),
                PorterDuff.Mode.SRC_IN));
        profilePic.setImageDrawable(drawable);
    }

    public static Drawable getProfilePicture(Context context) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.profile_pic);
        drawable.setColorFilter(new PorterDuffColorFilter(getPrimaryColor(context),
                PorterDuff.Mode.SRC_IN));
        return drawable;
    }

    public static Drawable getRLayout(Context context) {
        Drawable drawable1 = context.getResources().getDrawable(R.drawable.timeslotblue);
        drawable1.setColorFilter(new PorterDuffColorFilter(getPrimaryColor(context), PorterDuff.Mode.SRC_IN));
        return drawable1;
    }

    public static Drawable getMapRequest(Context context) {
        return context.getResources().getDrawable(R.drawable.map_placeholder);
    }

    public static File getCompressedFile(Context context, File file) {
        File compressedImage = null;
        try {
            compressedImage = new Compressor(context)
                    .setQuality(80)
                    .setCompressFormat(Bitmap.CompressFormat.PNG)
                    .compressToFile(file);
            return compressedImage;
        } catch (IOException e) {
            e.printStackTrace();
            return file;
        }
    }


    public static List<Integer> getAllMaterialColors(Context activity) {
        XmlResourceParser xrp = activity.getResources().getXml(R.xml.theme_color);
        List<Integer> allColors = new ArrayList<>();
        int nextEvent;
        try {
            while ((nextEvent = xrp.next()) != XmlResourceParser.END_DOCUMENT) {
                String s = xrp.getName();
                if ("color".equals(s)) {
                    String color = xrp.nextText();
                    allColors.add(Color.parseColor(color));
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allColors;
    }

    public static int getPrimaryColor(Context context) {
        final TypedValue value = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, value, true);
        return value.data;
    }

    public static void setTheme(Context activity, String themeValue) {
        switch (themeValue) {
            case "1":
                activity.setTheme(R.style.AppThemeMain);
                break;
            case "2":
                activity.setTheme(R.style.AppThemePink);
                break;
            case "3":
                activity.setTheme(R.style.AppThemeBlue);
                break;
            case "4":
                activity.setTheme(R.style.AppThemeYellow);
                break;
            case "5":
                activity.setTheme(R.style.AppThemeIndigo);
                break;
            case "6":
                activity.setTheme(R.style.AppThemeRed);
                break;
            default:
                activity.setTheme(R.style.AppThemeMain);
                break;
        }
    }

    public static void show(Context context) {
        if (!isShowing) {
            isShowing = true;
            if (loaderDialog == null || !loaderDialog.isShowing()) {
                try {
                    loaderDialog = new LoaderDialog(context);
                    loaderDialog.setCancelable(false);
                    loaderDialog.setCanceledOnTouchOutside(false);
                    loaderDialog.setContentView(R.layout.dialog_loading);
                    Window window = loaderDialog.getWindow();
                    if (window != null) {
                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                                WindowManager.LayoutParams.MATCH_PARENT);
                    }
                    loaderDialog.show();
                } catch (Exception e) {
                    BaseUtils.log(TAG, e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public static void dismiss() {
        if (isShowing) {
            isShowing = false;
            if (loaderDialog != null && loaderDialog.isShowing()) {
                try {
                    loaderDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Dialog getCustomDialog(Context activity, int inflatingLayout) {
        Dialog dialog = new Dialog(activity);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(inflatingLayout);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);
            window.getAttributes().windowAnimations = R.style.CustomDialogAnimation;
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        return dialog;
    }

    public static Dialog getPopupDialog(Context activity, int inflatingLayout) {
        Dialog dialog = new Dialog(activity);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(inflatingLayout);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            window.getAttributes().windowAnimations = R.style.DialogAnimation;
        }
        return dialog;
    }

    public static void showPictureDialog(Activity activity, String fileName) {

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder.setTitle(activity.getString(R.string.choose_your_option));
        String[] items = {activity.getString(R.string.gallery), activity.getString(R.string.camera)};

        alertBuilder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                switch (which) {
                    case 0:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            activity.requestPermissions(Constants.PERMISSIONS.readStoragePermList, READ_STORAGE_PERMISSIONS);

                        } else {
                            openGallery(activity);
                        }
                        break;
                    case 1:

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            activity.requestPermissions(Constants.PERMISSIONS.cameraPermList, CAMERA_STORAGE_PERMISSIONS);

                        } else {
                            openCamera(activity, fileName);
                        }
                        break;
                }
            }
        });

        AlertDialog alert = alertBuilder.create();
        Window window = alert.getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.AlertDialogAnimation;
        }
        alert.show();
    }

    public static void openGallery(Activity activity) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(i, GALLERY_INTENT);
    }

    public static void openCamera(Activity activity, String fileName) {
        AppSettings appSettings = new AppSettings(activity);
        File filepath = Environment.getExternalStorageDirectory();
        final File zoeFolder = new File(filepath.getAbsolutePath(),
                activity.getString(R.string.app_name)).getAbsoluteFile();
        if (!zoeFolder.exists()) {
            zoeFolder.mkdir();
        }
        File newFolder = new File(zoeFolder,
                activity.getString(R.string.app_name) + "_Image").getAbsoluteFile();
        if (!newFolder.exists()) {
            newFolder.mkdir();
        }

        Random r = new Random();
        int Low = 1000;
        int High = 10000000;
        int randomImageNo = r.nextInt(High - Low) + Low;
        String camera_captureFile = String.valueOf(fileName + randomImageNo);
        final File file = new File(newFolder, camera_captureFile + ".jpg");
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", file);
            BaseUtils.log(TAG, "onClick: " + uri.getPath());
            appSettings.setImageUploadPath(file.getAbsolutePath());
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivityForResult(takePictureIntent, CAMERA_INTENT);

        } else {
            uri = Uri.fromFile(file);
            appSettings.setImageUploadPath(file.getAbsolutePath());
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivityForResult(takePictureIntent, CAMERA_INTENT);

        }
        BaseUtils.log(TAG, "onActivityResult: " + appSettings.getImageUploadPath());

    }
}