package com.assignment.doordash.doordash;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    private ImageView logo;
    private TextView name;
    private TextView description;
    private TextView status;

    RestaurantViewHolder(View v) {
        super(v);

        logo = v.findViewById(R.id.id_logo);
        name = v.findViewById(R.id.id_name);
        description = v.findViewById(R.id.id_description);
        status = v.findViewById(R.id.id_status);
    }

    public ImageView getLogo() {
        return logo;
    }

    public TextView getName() {
        return name;
    }

    public TextView getDescription() {
        return description;
    }

    public TextView getStatus() {
        return status;
    }
}
