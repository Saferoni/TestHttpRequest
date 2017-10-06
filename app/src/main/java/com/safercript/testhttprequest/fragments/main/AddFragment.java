package com.safercript.testhttprequest.fragments.main;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.safercript.testhttprequest.activity.MainActivity;
import com.safercript.testhttprequest.MyHttpRequestApp;
import com.safercript.testhttprequest.R;
import com.safercript.testhttprequest.SharedInfo;
import com.safercript.testhttprequest.callbacks.DaoHandler;
import com.safercript.testhttprequest.dao.ManagerDao;
import com.safercript.testhttprequest.entity.ImageToSend;
import com.safercript.testhttprequest.entity.response.ResponseError;
import com.safercript.testhttprequest.entity.response.ResponseImage;
import com.safercript.testhttprequest.util.AppConstants;
import com.safercript.testhttprequest.util.UIUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.ACTION_GET_CONTENT;


public class AddFragment extends Fragment {

    private static final String LOG_TAG = AddFragment.class.getSimpleName();

    private static final int REQUEST_CODE_IMAGE_FROM_GALLERY = 103;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 102;

    private Activity mActivity;
    private ManagerDao managerDao;
    private SharedInfo sharedInfo;
    private ImageToSend imageToSend;
    private String mCurrentPhotoPath;
    private ProgressDialog mProgress;

    private ImageView addImage;
    private EditText addDescription;
    private EditText addHashtag;
    private Button buttonSave;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add, container, false);

        managerDao = ((MyHttpRequestApp) mActivity.getApplication()).getManagerDao();
        sharedInfo = SharedInfo.get();

        imageToSend = new ImageToSend("53.122042", "23.175977");

        addImage = (ImageView) view.findViewById(R.id.add_image);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSourceImageDialog();
            }
        });
        addDescription = (EditText) view.findViewById(R.id.add_text_description);
        addHashtag = (EditText) view.findViewById(R.id.add_text_hashtag);
        buttonSave = (Button) view.findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageToSend.setDescription(addDescription.getText().toString());
                imageToSend.setHashTag(addHashtag.getText().toString());
                managerDao.addImage(imageToSend, sharedInfo.getUserToken());
                mProgress.show();
            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    closeFragment();
                    return true;
                }
                return false;
            }
        });

        mProgress = new ProgressDialog(mActivity);
        mProgress.setMessage("Save photo to Server " + AppConstants.API_SERVER);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        managerDao.init(new AddHandler(mActivity, this));
    }

    private void closeFragment(){
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    private static class AddHandler extends DaoHandler {
        private final WeakReference<AddFragment> mFragment;

        AddHandler(Activity activity, AddFragment fragment) {
            super(activity);
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case AppConstants.HANDLER_RESULT_UPLOAD_IMAGE:
                    Log.e(LOG_TAG, " HANDLER_RESULT_OK");
                    if (msg.obj != null) {
                        ResponseImage responseImage = (ResponseImage) msg.obj;
                        Log.e(LOG_TAG, responseImage.toString());
                        mFragment.get().mProgress.hide();
                        ((MainActivity)mFragment.get().mActivity).getImages();
                        mFragment.get().closeFragment();
                    }
                    return;

                case AppConstants.HANDLER_RESULT_ERROR:
                    Log.e(LOG_TAG, " HANDLER_RESULT_ERROR");
                    if (msg.obj != null) {
                        ResponseError responseError = (ResponseError) msg.obj;
                        mFragment.get().mProgress.hide();
                        UIUtils.showToast(mFragment.get().mActivity,"Wrong Data" + responseError.getAllErrors());

                    }else{
                        Log.e(LOG_TAG, "msg.obj == null");
                        mFragment.get().mProgress.hide();
                        UIUtils.showToast(mFragment.get().mActivity,"Wrong Data");
                    }

                    break;
                case AppConstants.HANDLER_RESULT_ERROR_TOKEN:
                    Log.e(LOG_TAG, " HANDLER_RESULT_ERROR_TOKEN");
                    mFragment.get().mProgress.hide();
                    UIUtils.showToast(mFragment.get().mActivity,"Wrong Token");
                    break;
            }
            super.handleMessage(msg);
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
            String imagePath = null;
            if (requestCode == REQUEST_CODE_IMAGE_FROM_GALLERY) {
                imagePath = getRealPathFromUri(mActivity,data.getData());
                imageToSend.setImagePath(imagePath);
                addImage.setImageURI(Uri.parse(imagePath));
                //Picasso.with(mActivity).load(imagePath).into(addImage);
            }

            if (requestCode == REQUEST_CODE_CAPTURE_IMAGE) {
                imagePath = getRealPathFromUri(mActivity,data.getData());
                imageToSend.setImagePath(imagePath);
                Picasso.with(mActivity).load(imagePath).into(addImage);
            }
        }
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
