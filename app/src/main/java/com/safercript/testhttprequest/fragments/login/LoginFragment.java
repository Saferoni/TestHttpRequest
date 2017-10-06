package com.safercript.testhttprequest.fragments.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.safercript.testhttprequest.LoginActivity;
import com.safercript.testhttprequest.activity.MainActivity;
import com.safercript.testhttprequest.MyHttpRequestApp;
import com.safercript.testhttprequest.R;
import com.safercript.testhttprequest.SharedInfo;
import com.safercript.testhttprequest.callbacks.DaoHandler;
import com.safercript.testhttprequest.callbacks.OnInputUserInfoChangedListener;
import com.safercript.testhttprequest.dao.ManagerDao;
import com.safercript.testhttprequest.entity.response.ResponseUserLogin;
import com.safercript.testhttprequest.entity.User;
import com.safercript.testhttprequest.util.AppConstants;
import com.safercript.testhttprequest.util.PreferenceManager;
import com.safercript.testhttprequest.util.UIUtils;

import java.lang.ref.WeakReference;
import java.util.regex.Pattern;

public class LoginFragment extends Fragment implements OnInputUserInfoChangedListener {
    private static final String LOG_TAG = LoginFragment.class.getSimpleName();

    private Activity mActivity;

    private Button mFinal;
    private ProgressBar mProgressBar;

    private DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;

    private ViewPager mViewPager;

    private User mSignUser;
    private ManagerDao managerDao;
    private int mCurrentItem;
    private Fragment fragmentSignIn;
    private SharedInfo sharedInfo;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        UIUtils.lockOrientation((LoginActivity) mActivity);

        mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getActivity().getSupportFragmentManager());

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.sign_login_tab_title));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.sign_signup_tab_title));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager = (ViewPager) view.findViewById(R.id.tutorial_pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        mFinal.setText(R.string.sign_sign_in_final);
                        break;
                    case 1:
                        mFinal.setText(R.string.sign_sign_up_final);
                        break;
                }
                mCurrentItem = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mFinal = (Button) view.findViewById(R.id.sign_final_button);
        mProgressBar = (ProgressBar) view.findViewById(R.id.sign_progressbar);
        mFinal.setText(R.string.sign_sign_in_final);

        mSignUser = new User();
        managerDao = ((MyHttpRequestApp) mActivity.getApplication()).getManagerDao();
        sharedInfo = SharedInfo.get();

        initNickName();
        initUserEmail();
        initPass();

        mFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (mCurrentItem) {
                    case 0:
                        if (mSignUser.getUserEmail() != null && !mSignUser.getUserEmail().equals("") || mSignUser.getUserPassword() != null && !mSignUser.getUserPassword().equals("")){
                            mProgressBar.setVisibility(View.VISIBLE);
                            mFinal.setVisibility(View.GONE);
                            UIUtils.showToast(mActivity, mSignUser.getUserEmail() + mSignUser.getUserPassword());
                            managerDao.signIn(mSignUser);
                        }else {
                            UIUtils.showToast(mActivity, mActivity.getString(R.string.error_nickname_or_password_not_empty));
                            managerDao.signUP(mSignUser);
                        }
                        break;
                    case 1:
                        if (!isErrorInfoSignUp()) {
                            mCurrentItem = 0;
                            mViewPager.setCurrentItem(mCurrentItem);
                            UIUtils.showToast(mActivity, mSignUser.getUserName() + mSignUser.getUserEmail() + mSignUser.getUserPassword());
                            ((SignInFragment) fragmentSignIn).initUserEmail();
                            ((SignInFragment) fragmentSignIn).initPass();
                        }
                        break;
                }

            }

            private boolean isErrorInfoSignUp() {
                if (TextUtils.isEmpty(mSignUser.getUserEmail()) || TextUtils.isEmpty(mSignUser.getUserName())
                        || TextUtils.isEmpty(mSignUser.getUserPassword())) {
                    UIUtils.showToast(mActivity, mActivity.getString(R.string.sign_error_empty));
                    return true;
                }

                if (isEmailErrorCheck()) return true;
                if (isUsernameErrorCheck()) return true;
                if (isPasswordErrorCheck()) return true;
                if (isNicknameMatchesPassword()) return true;

                return false;
            }

            private boolean isNicknameMatchesPassword() {
                if (mSignUser.getUserName().equals(mSignUser.getUserPassword())) {
                    UIUtils.showToast(mActivity, mActivity.getString(R.string.sign_error_nickname_matches_password));
                    return true;
                }
                return false;
            }

            private boolean isEmailErrorCheck() {
                String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                if (!Pattern.compile(EMAIL_PATTERN).matcher(mSignUser.getUserEmail()).find()) {
                    UIUtils.showToast(mActivity, mActivity.getString(R.string.sign_error_email));
                    return true;
                }
                return false;
            }

            private boolean isPasswordErrorCheck() {
                if (!mSignUser.isSetInfo()) {
                    UIUtils.showToast(mActivity, mActivity.getString(R.string.sign_error_confirm_password));
                    return true;
                }

                if (mSignUser.getUserPassword().length() < 8) {
                    UIUtils.showToast(mActivity, mActivity.getString(R.string.sign_error_password));
                    return true;
                }


                if (!Pattern.compile("^[A-Za-z0-9]+$").matcher(mSignUser.getUserPassword()).find()) {
                    UIUtils.showToast(mActivity, mActivity.getString(R.string.sign_error_password));
                    return true;
                }

                if (!Pattern.compile("[0-9]").matcher(mSignUser.getUserPassword()).find()) {
                    UIUtils.showToast(mActivity, mActivity.getString(R.string.sign_error_password));
                    return true;
                }

                boolean hasUppercase = mSignUser.getUserPassword().equals(mSignUser.getUserPassword().toLowerCase());
                boolean hasLowercase = mSignUser.getUserPassword().equals(mSignUser.getUserPassword().toUpperCase());
                if (hasUppercase) {
                    UIUtils.showToast(mActivity, mActivity.getString(R.string.sign_error_password));
                    return true;
                }

                if (hasLowercase) {
                    UIUtils.showToast(mActivity, mActivity.getString(R.string.sign_error_password));
                    return true;
                }
                return false;
            }

            private boolean isUsernameErrorCheck() {
                if (!mSignUser.getUserName().matches("^[\\w\\d_]+$")) {
                    UIUtils.showToast(mActivity, mActivity.getString(R.string.sign_error_username));
                    return true;
                }

                if (mSignUser.getUserName().length() < 5) {
                    UIUtils.showToast(mActivity, mActivity.getString(R.string.sign_error_username));
                    return true;
                }
                return false;
            }

        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        managerDao.init(new LoginHandler(mActivity, this));
    }

    @Override
    public void onUpdate(User user) {
        mSignUser.setUserName(user.getUserName());
        mSignUser.setUserEmail(user.getUserEmail());
        mSignUser.setUserPassword(user.getUserPassword());
        mSignUser.setSetInfo(user.isSetInfo());
    }

    private class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {

        private DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment;
            switch (i) {
                case 0:
                    fragment = new SignInFragment().setUpdateListener(LoginFragment.this);
                    fragmentSignIn = fragment;
                    break;
                case 1:
                    fragment = new SignUpFragment().setUpdateListener(LoginFragment.this);
                    break;
                default:
                    fragment = new SignInFragment().setUpdateListener(LoginFragment.this);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // For this contrived example, we have a 100-object collection.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }

    public void initUserEmail() {
        if (PreferenceManager.getUserEmail(getActivity()) != null) {
            mSignUser.setUserEmail(PreferenceManager.getUserEmail(getActivity()));
        }
    }

    public void initNickName() {
        if (PreferenceManager.getUserNickname(getActivity()) != null) {
            mSignUser.setUserName(PreferenceManager.getUserNickname(getActivity()));
        }
    }
    public void initPass(){
        if(PreferenceManager.getUserPassword(getActivity()) != null){
            mSignUser.setUserPassword(PreferenceManager.getUserPassword(getActivity()));
        }
    }

    private static class LoginHandler extends DaoHandler {
        private final WeakReference<LoginFragment> mFragment;

        LoginHandler(Activity activity, LoginFragment fragment) {
            super(activity);
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case AppConstants.HANDLER_RESULT_OK:
                    Log.e(LOG_TAG, " HANDLER_RESULT_OK");
                    if (mFragment.get().mCurrentItem == 0) {
                        if (msg.obj != null) {
                            ResponseUserLogin responseUserLogin = (ResponseUserLogin) msg.obj;
                            mFragment.get().sharedInfo.setCurrentUser(updateUser(responseUserLogin));
                            mFragment.get().sharedInfo.setUserToken(mFragment.get().mSignUser.getAuthorizationKey());
                            mFragment.get().mActivity.startActivity(new Intent(mFragment.get().mActivity, MainActivity.class));
                            mFragment.get().mActivity.finish();
                        }
                    }else {
                        UIUtils.showToast(mFragment.get().mActivity, mFragment.get().mActivity.getString(R.string.registration_successful));
                        mFragment.get().mCurrentItem = 0;
                        mFragment.get().mViewPager.setCurrentItem(mFragment.get().mCurrentItem);
                        ((SignInFragment) mFragment.get().fragmentSignIn).initUserEmail();
                        ((SignInFragment) mFragment.get().fragmentSignIn).initPass();
                    }
                    return;

                case AppConstants.HANDLER_RESULT_ERROR:
                    Log.e(LOG_TAG, " HANDLER_RESULT_ERROR");
                    mFragment.get().mFinal.setVisibility(View.VISIBLE);
                    mFragment.get().mProgressBar.setVisibility(View.GONE);
                    UIUtils.showToast(mFragment.get().mActivity,"Wrong Data");
                    break;
            }
            super.handleMessage(msg);
        }

        private User updateUser(ResponseUserLogin responseUserLogin) {
            mFragment.get().mSignUser.setAuthorizationKey(responseUserLogin.getToken());
            if (!responseUserLogin.getAvatar().equals("")){
                mFragment.get().mSignUser.setAvatar(responseUserLogin.getAvatar());
            }
            return mFragment.get().mSignUser;
        }
    }
}
