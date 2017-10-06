package com.safercript.testhttprequest.fragments.login;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.safercript.testhttprequest.LoginActivity;
import com.safercript.testhttprequest.R;
import com.safercript.testhttprequest.callbacks.OnInputUserInfoChangedListener;
import com.safercript.testhttprequest.entity.User;
import com.safercript.testhttprequest.util.PreferenceManager;
import com.safercript.testhttprequest.util.UIUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.ACTION_GET_CONTENT;

public class SignUpFragment extends Fragment implements TextWatcher {

    private static final int REQUEST_CODE_IMAGE_FROM_GALLERY = 103;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 102;

    private Activity mActivity;
    private ImageView avatar;
    private EditText mNickname;
    private EditText mEmail;
    private EditText mPass;
    private EditText mConfirmPass;

    private User user;
    private String mCurrentPhotoPath;

    private OnInputUserInfoChangedListener listener;

    public SignUpFragment() {
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
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        user = new User();

        UIUtils.lockOrientation((LoginActivity) mActivity);

        avatar = (ImageView) view.findViewById(R.id.add_avatar);
        mNickname = (EditText) view.findViewById(R.id.signup_nickname);
        mEmail = (EditText) view.findViewById(R.id.signup_email);
        mPass = (EditText) view.findViewById(R.id.signup_password);
        mConfirmPass = (EditText) view.findViewById(R.id.signup_confirm_password);

        mNickname.addTextChangedListener(this);
        mEmail.addTextChangedListener(this);
        mPass.addTextChangedListener(this);
        mConfirmPass.addTextChangedListener(this);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSourceImageDialog();
            }
        });

        return view;
    }

    public SignUpFragment setUpdateListener(OnInputUserInfoChangedListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (mPass.getText().toString().equals(mConfirmPass.getText().toString())) {
            user.setSetInfo(true);
        } else {
            user.setSetInfo(false);
        }

        user.setUserPassword(mPass.getText().toString());
        PreferenceManager.saveUserPassword(mActivity, mPass.getText().toString());
        user.setUserEmail(mEmail.getText().toString());
        PreferenceManager.saveUserEmail(mActivity, mEmail.getText().toString().toLowerCase());
        user.setUserName(mNickname.getText().toString().toLowerCase());
        PreferenceManager.saveUserNickname(mActivity, mNickname.getText().toString().toLowerCase());
        if (listener != null){
            listener.onUpdate(user);
        }
    }

/* add new photo or get photo from gallery */
    private void showSourceImageDialog() {
        new MaterialDialog.Builder(getContext())
                .title("Выбери ресурс:")
                .items(new String[]{"Из галереи", "Из камеры"})
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                Intent localPhotoIntent = new Intent();
                                localPhotoIntent.setType("image/*");
                                localPhotoIntent.setAction(ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(localPhotoIntent, "Select Picture"), REQUEST_CODE_IMAGE_FROM_GALLERY);
                                break;
                            case 1:
                                if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                    if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA)
                                            == PackageManager.PERMISSION_GRANTED
                                            && (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                            == PackageManager.PERMISSION_GRANTED)) {
                                        getImageFromCamera();
                                    } else {
                                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                    }
                                } else {
                                    getImageFromCamera();
                                }
                                break;
                        }
                        return false;
                    }
                })
                .show();
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                UIUtils.showToast(mActivity, getString(R.string.camera_denied));
                return;
            }
            if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                UIUtils.showToast(mActivity,getString(R.string.write_disk_denied));
                return;
            }
            getImageFromCamera();
        }
    }
    private void getImageFromCamera() {
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            Log.e("createImageFile error:", e.getMessage());
        }
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(mActivity, mActivity.getPackageName(), photoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(cameraIntent, REQUEST_CODE_CAPTURE_IMAGE);
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri imagePath = null;
            if (requestCode == REQUEST_CODE_IMAGE_FROM_GALLERY) {
                imagePath = data.getData();
                user.setAvatar(imagePath.toString());
                Picasso.with(mActivity).load(imagePath).into(avatar);
            }

            if (requestCode == REQUEST_CODE_CAPTURE_IMAGE) {
                imagePath = Uri.parse(mCurrentPhotoPath);
                user.setAvatar(imagePath.toString());
                Picasso.with(mActivity).load(imagePath).into(avatar);
            }

            if (listener != null){
                listener.onUpdate(user);
            }
        }
    }
}
