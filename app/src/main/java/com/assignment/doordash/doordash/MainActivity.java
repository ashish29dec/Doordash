package com.assignment.doordash.doordash;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.assignment.doordash.doordash.model.Restaurant;

public class MainActivity extends AppCompatActivity {

    private RestaurantListViewModel mViewModel;
    private RestaurantAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.id_toolbar);
        toolbar.setTitle(R.string.title);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.id_restaurant_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        mAdapter = new RestaurantAdapter();

        recyclerView.setAdapter(mAdapter);

        mViewModel = ViewModelProviders.of(this).get(RestaurantListViewModel.class);

        mViewModel.getRestaurants().observe(this, new Observer<Restaurant[]>() {
            @Override
            public void onChanged(@Nullable Restaurant[] restaurants) {
                onRestaurantsFetched(restaurants);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Download the data
        mViewModel.fetchRestaurants(0);
    }

    public void onRestaurantsFetched(Restaurant[] restaurants) {
        mAdapter.setData(restaurants);
        mAdapter.notifyDataSetChanged();
    }
}
