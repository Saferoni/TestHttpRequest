package com.safercript.testhttprequest.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.safercript.testhttprequest.R;
import com.safercript.testhttprequest.entity.response.ResponseImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.MyViewHolder> {

    private ItemRecyclerAdapter.OnClickListenerAdapter onClickListenerAdapter;

    private ArrayList<ResponseImage> imageItems;
    private Context mContext;
    private int position;

    public ItemRecyclerAdapter(Context context, ArrayList<ResponseImage> imageItems) {
        this.imageItems = imageItems;
        mContext = context;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout cardView;
        ImageView imageView;
        TextView address;
        TextView weather;

        MyViewHolder(View view){
            super(view);
            cardView = (LinearLayout) view.findViewById(R.id.card);
            imageView = (ImageView) view.findViewById(R.id.card_img);
            address = (TextView) view.findViewById(R.id.card_text_address);
            weather = (TextView) view.findViewById(R.id.card_text_weathe);
        }
    }

    @Override
    public int getItemCount() {
        return imageItems == null ? 0 : imageItems.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        initCardView(holder, imageItems.get(position));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListenerAdapter.onClick(imageItems.get(position));
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onClickListenerAdapter.onLongClick(imageItems.get(position));
                return true;
            }
        });
        this.position = position;

    }

    private void initCardView(MyViewHolder holder, ResponseImage imageItem){
        int imgRes = imageItem.getId();
        holder.imageView.setImageResource(R.drawable.ic_panorama_black_24dp);
        if(imgRes != 0){
            Picasso.with(mContext).load(imageItem.getSmallImagePath()).into(holder.imageView);
        }else {
            holder.imageView.setImageResource(R.drawable.ic_panorama_black_24dp);
        }

        if(imageItem.getParameters().getAddress() != null){
            holder.address.setVisibility(View.VISIBLE);
            holder.address.setText(imageItem.getParameters().getAddress());
        }else {
            holder.address.setVisibility(View.GONE);
        }

        if(imageItem.getParameters().getWeather() != null){
            holder.weather.setVisibility(View.VISIBLE);
            holder.weather.setText(imageItem.getParameters().getWeather());
        }else {
            holder.weather.setVisibility(View.GONE);
        }
    }
    public void setNewData(ArrayList<ResponseImage> data){
        imageItems.clear();
        imageItems = data;
        notifyDataSetChanged();
    }

    // add onClickListener
    public void setOnClickListenerAdapter(ItemRecyclerAdapter.OnClickListenerAdapter onClickListenerAdapter) {
        this.onClickListenerAdapter = onClickListenerAdapter;
    }
    // interface for connection with activity
    public interface OnClickListenerAdapter {
        void onClick(ResponseImage imageItem);
        void onLongClick(ResponseImage imageItem);
    }
}
