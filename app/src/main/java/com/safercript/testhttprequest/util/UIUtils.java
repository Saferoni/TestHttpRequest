package com.safercript.testhttprequest.util;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.safercript.testhttprequest.R;

public class UIUtils {


    public static void changeStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(activity, color));
        }
    }

    public static void showToastUIThread(final Activity activity, final String text) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(activity, text);
            }
        });
    }

    public static void showToast(Activity context, CharSequence text) {
        try {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void switchFragmentUIThread(final Activity activity, final Fragment fragment) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switchFragment((AppCompatActivity) activity, fragment, false, false, false);
            }
        });
    }

    public static Fragment switchFragment(Activity activity, Fragment fragment) {
        return switchFragment((AppCompatActivity) activity, fragment, false, false, false);
    }

    public static Fragment switchFragment(AppCompatActivity activity, Fragment fragment) {
        return switchFragment(activity, fragment, false, false, false);
    }

    public static Fragment switchFragment(AppCompatActivity activity, Fragment fragment,
                                          boolean isFullScreen, boolean isLocked, boolean isLandScape) {

        if (isLocked) {
            activity.setRequestedOrientation(isLandScape ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }

        if (isFullScreen) {
            setFullScreen(activity);
        } else {
            unsetFullScreen(activity);
        }

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        try {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment, fragment.getClass().getSimpleName())
                    .commit();

        } catch (IllegalStateException ignored) {
            ignored.printStackTrace();
        }
        return fragment;
    }

    public static void lockOrientation(AppCompatActivity activity) {
        int orientation = activity.getApplicationContext().getResources().getConfiguration().orientation;
        activity.setRequestedOrientation(orientation);
    }

    public static void setFullScreen(AppCompatActivity activity) {
        if (activity.getSupportActionBar() != null)
            activity.getSupportActionBar().hide();
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private static void unsetFullScreen(AppCompatActivity activity) {
        if (activity.getSupportActionBar() != null)
            activity.getSupportActionBar().show();
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }
}
