package com.assignment.doordash.doordash;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.assignment.doordash.doordash.interfaces.onBitmapDownloadedListener;
import com.assignment.doordash.doordash.model.Restaurant;
import com.assignment.doordash.doordash.utils.BitmapCache;

import java.lang.ref.WeakReference;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {

    private Restaurant[] data = null;

    public void setData(Restaurant[] data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.restaurant_item, viewGroup, false);
        RestaurantViewHolder holder = new RestaurantViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder restaurantViewHolder, int position) {
        Restaurant restaurant = this.data[position];
        restaurantViewHolder.getName().setText(restaurant.name);
        restaurantViewHolder.getDescription().setText(restaurant.description);
        restaurantViewHolder.getStatus().setText(restaurant.status);
        ImageView imgView = restaurantViewHolder.getLogo();
        imgView.setTag(restaurant.cover_img_url);
        imgView.setImageDrawable(ResourcesCompat.getDrawable(imgView.getResources(), R.mipmap.ic_launcher, null));
        if (restaurant.cover_img_url != null) {
            final WeakReference<ImageView> logoRef = new WeakReference<>(imgView);
            BitmapCache.getInstance().get(restaurant.cover_img_url, new onBitmapDownloadedListener() {
                @Override
                public void onBitmapDownloaded(String url, Bitmap bitmap) {
                    ImageView imgView = logoRef.get();
                    if (imgView != null && bitmap != null && imgView.getTag().equals(url)) {
                        imgView.setImageBitmap(bitmap);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (this.data == null) {
            return 0;
        }
        return this.data.length;
    }
}
