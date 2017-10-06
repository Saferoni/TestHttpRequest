package com.safercript.testhttprequest.fragments.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.safercript.testhttprequest.LoginActivity;
import com.safercript.testhttprequest.R;
import com.safercript.testhttprequest.callbacks.OnInputUserInfoChangedListener;
import com.safercript.testhttprequest.entity.User;
import com.safercript.testhttprequest.util.PreferenceManager;
import com.safercript.testhttprequest.util.UIUtils;

public class SignInFragment extends Fragment implements TextWatcher {

    private Activity mActivity;
    
    private EditText mUserEmail;
    private EditText mPass;
    private boolean needDeleteOldPassword = false;

    private User user;

    private OnInputUserInfoChangedListener listener;

    public SignInFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        user = new User();

        UIUtils.lockOrientation((LoginActivity) mActivity);

        mUserEmail = (EditText) view.findViewById(R.id.login_email);
        mPass = (EditText) view.findViewById(R.id.login_password);

        initUserEmail();
        initPass();

        mUserEmail.addTextChangedListener(this);
        mPass.addTextChangedListener(this);

        mUserEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mUserEmail.requestFocus();
                        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.
                                INPUT_METHOD_SERVICE);
                        imm.showSoftInput(mUserEmail, InputMethodManager.SHOW_IMPLICIT);
                        mUserEmail.setSelection(mUserEmail.getText().length());
                        needDeleteOldPassword = false;
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        mPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mPass.requestFocus();
                        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.
                                INPUT_METHOD_SERVICE);
                        imm.showSoftInput(mPass, InputMethodManager.SHOW_IMPLICIT);
                        mPass.setSelection(mPass.getText().length());
                        needDeleteOldPassword = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        mUserEmail.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER) {
                    needDeleteOldPassword = true;
                }
                return false;
            }
        });

        mPass.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(needDeleteOldPassword){
                    if(keyCode == KeyEvent.KEYCODE_DEL) {
                        mPass.setText("");
                        needDeleteOldPassword = false;
                    }
                }
                return false;
            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        return view;
    }

    public void initUserEmail() {
        if (PreferenceManager.getUserEmail(mActivity) != null) {
            mUserEmail.setText(PreferenceManager.getUserEmail(mActivity));
            mUserEmail.setSelection(mUserEmail.getText().length());
            user.setUserEmail(mUserEmail.getText().toString());
        }
    }
    public void initPass(){
        if(PreferenceManager.getUserPassword(mActivity) != null){
            mPass.setText(PreferenceManager.getUserPassword(mActivity));
            mPass.setSelection(mPass.getText().length());
            user.setUserPassword(mPass.getText().toString());
        }else {
            mPass.getText().clear();
        }
    }

    public SignInFragment setUpdateListener(OnInputUserInfoChangedListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(needDeleteOldPassword){
            needDeleteOldPassword = false;
            String s = null;
            if((mPass.getText().toString().length())!= 0) {
                s = (mPass.getText().toString()).substring(mPass.getText().toString().length() - 1);
            }
            if(s != null){
                mPass.setText(s);
                mPass.setSelection(1);
            }else {
                mPass.getText().clear();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        user.setUserEmail(mUserEmail.getText().toString().toLowerCase());
        user.setUserPassword(mPass.getText().toString());
        PreferenceManager.saveUserEmail(mActivity, mUserEmail.getText().toString());
        PreferenceManager.saveUserPassword(mActivity, mPass.getText().toString());
        if (listener != null){
            listener.onUpdate(user);
        }
    }
}
