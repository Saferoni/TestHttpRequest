package com.safercript.testhttprequest;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.safercript.testhttprequest.adapters.ItemRecyclerAdapter;
import com.safercript.testhttprequest.callbacks.DaoHandler;
import com.safercript.testhttprequest.dao.ManagerDao;
import com.safercript.testhttprequest.entity.response.ResponseAllArray;
import com.safercript.testhttprequest.entity.response.ResponseImage;
import com.safercript.testhttprequest.fragments.main.AddFragment;
import com.safercript.testhttprequest.util.AppConstants;
import com.safercript.testhttprequest.util.UIUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements ItemRecyclerAdapter.OnClickListenerAdapter{
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private GridLayoutManager mLayoutManager;
    private ItemRecyclerAdapter mAdapter;
    private ArrayList<ResponseImage> imageItems;
    private MainHandler mainHandler;
    private SharedInfo sharedInfo;
    private ManagerDao managerDao;
    private ProgressDialog mProgress;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        RecyclerView itemRecyclerView = (RecyclerView) findViewById(R.id.recycleView);

        imageItems = new ArrayList<>();


        mAdapter = new ItemRecyclerAdapter(this, imageItems);
        mAdapter.setOnClickListenerAdapter(this);
        mLayoutManager = new GridLayoutManager(this,2);
        itemRecyclerView.setAdapter(mAdapter);
        itemRecyclerView.setLayoutManager(mLayoutManager);


        managerDao = ((MyHttpRequestApp) getApplication()).getManagerDao();
        sharedInfo = SharedInfo.get();
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling API" + AppConstants.API_SERVER);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_onRecyclerView);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getImages();
            }
        });

        getImages();
    }

    public void getImages(){
        managerDao.init(mainHandler);
        managerDao.getAllImages(sharedInfo.getUserToken());
        mProgress.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            UIUtils.switchFragment(this, new AddFragment());
            //UIUtils.showToast(this, "Click ADD");
            return true;
        }
        if (id == R.id.action_play) {
            managerDao.getGif(sharedInfo.getUserToken(), "Rain");

            UIUtils.showToast(this, "Click PLAY");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showItemDialog(String imagePath, String addressImage, String weatherImage) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View viewDialog = inflater.inflate(R.layout.item_card, null);
        ImageView imageView = (ImageView) viewDialog.findViewById(R.id.card_img);
        TextView address = (TextView) viewDialog.findViewById(R.id.card_text_address);
        TextView weather = (TextView) viewDialog.findViewById(R.id.card_text_weathe);
        Picasso.with(this).load(imagePath).into(imageView);
        address.setText(addressImage == null ? "" : addressImage);
        weather.setText(weatherImage == null ? "" : weatherImage);

        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });
        builder.setContentView(viewDialog);
        builder.show();
    }

    @Override
    public void onClick(ResponseImage imageItem) {
        showItemDialog(imageItem.getSmallImagePath(),
                imageItem.getParameters().getAddress(),
                imageItem.getParameters().getWeather());
    }

    @Override
    public void onLongClick(ResponseImage imageItem) {
        UIUtils.showToast(this, "Long Click " + imageItem.getId() + " " + imageItem.getParameters().getWeather());
    }

    public void updateImageItems(ArrayList<ResponseImage> imageItems){
        this.imageItems = imageItems;
        mAdapter.setNewData(imageItems);
        mProgress.hide();
    }

    private static class MainHandler extends DaoHandler {
        private MainActivity activity;

        public MainHandler(MainActivity activity) {
            super(activity);
            this.activity = activity;
        }
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case AppConstants.HANDLER_RESULT_OK:
                    Log.e(LOG_TAG, " HANDLER_RESULT_OK");
                    if (msg.obj != null) {
                        activity.mSwipeRefreshLayout.setRefreshing(false);
                        ResponseAllArray responseAllArray = (ResponseAllArray) msg.obj;
                        activity.updateImageItems(new ArrayList<ResponseImage>(Arrays.asList(responseAllArray.getImages())));
                    }
                    return;

                case AppConstants.HANDLER_RESULT_GIF:
                    Log.e(LOG_TAG, " HANDLER_RESULT_GIF");
                    activity.mProgress.hide();
                    if (msg.obj != null) {
                        activity.mSwipeRefreshLayout.setRefreshing(false);
                        ManagerDao.GifResponse gifResponse = (ManagerDao.GifResponse) msg.obj;
                        activity.showItemDialog(gifResponse.getGif(), null, null);
                    }
                    return;

                case AppConstants.HANDLER_RESULT_ERROR:
                    Log.e(LOG_TAG, " HANDLER_RESULT_ERROR");
                    activity.mSwipeRefreshLayout.setRefreshing(false);
                    activity.mProgress.hide();
                    break;
            }
            activity.mSwipeRefreshLayout.setRefreshing(false);
            super.handleMessage(msg);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainHandler = new MainHandler(this);
        managerDao.init(mainHandler);
    }
}
