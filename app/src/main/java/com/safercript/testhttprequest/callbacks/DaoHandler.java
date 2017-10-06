package com.safercript.testhttprequest.callbacks;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.safercript.testhttprequest.util.AppConstants;

public class DaoHandler extends Handler {
    private static final String LOG_TAG = DaoHandler.class.getSimpleName();

    private Activity activity;

    public DaoHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        switch (msg.what) {
            case AppConstants.HANDLER_RESULT_ERROR:
                Log.e(LOG_TAG, " HANDLER_RESULT_ERROR");
                break;
            case AppConstants.HANDLER_RESULT_OK:
                Log.e(LOG_TAG, " HANDLER_RESULT_OK");
                break;

            case AppConstants.HANDLER_RESULT_SIGN_UP:
                Log.e(LOG_TAG, " HANDLER_RESULT_SIGN_UP");
                break;
            case AppConstants.HANDLER_RESULT_SIGN_IN:
                Log.e(LOG_TAG, " HANDLER_RESULT_SIGN_IN");
                break;
            case AppConstants.HANDLER_RESULT_GIF:
                Log.e(LOG_TAG, " HANDLER_RESULT_GIF");
                break;
            case AppConstants.HANDLER_RESULT_UPLOAD_IMAGE:
                Log.e(LOG_TAG, " HANDLER_RESULT_UPLOAD_IMAGE");
                break;
            case AppConstants.HANDLER_RESULT_ERROR_TOKEN:
                Log.e(LOG_TAG, " HANDLER_RESULT_ERROR_TOKEN");
                break;
        }
    }
}
