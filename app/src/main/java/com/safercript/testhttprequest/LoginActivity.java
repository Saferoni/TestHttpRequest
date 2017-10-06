package com.safercript.testhttprequest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.safercript.testhttprequest.fragments.login.LoginFragment;
import com.safercript.testhttprequest.util.UIUtils;

public class LoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UIUtils.changeStatusBarColor(this, R.color.colorPrimary);

        UIUtils.switchFragment(this, new LoginFragment());

//        if (PreferenceManager.getUserEmail(this) != null) {
//            // Clear charts old data before user login
//            SharedVitalsChartsInfo.get().clearChartData();
//            UIUtils.switchFragment(this, new LoginFragment());
//        } else {
//            UIUtils.switchFragment(this, new TutorialFragment());
//        }
    }
}
