package com.neshan.task1.neshan.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.neshan.task1.R;
import com.neshan.task1.domain.model.searchModel.ItemsItem;
import com.neshan.task1.domain.model.searchModel.Location;

import org.jetbrains.annotations.NotNull;
import org.neshan.common.model.LatLng;

import java.util.ArrayList;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private static final String TAG = "SearchAdapter";

    private ArrayList<ItemsItem> items = new ArrayList<>();

    public interface OnSearchItemListener {
        void onSearchItemClick(LatLng LatLng);
    }

    private final OnSearchItemListener onSearchItemListener;

    public SearchAdapter(ArrayList<ItemsItem> items, OnSearchItemListener onSearchItemListener) {
        this.items = items;
        this.onSearchItemListener = onSearchItemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textView;
        private final TextView tvAddress;

        public ViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.textView);
            tvAddress = v.findViewById(R.id.textView_address);
            textView.setOnClickListener(this);
        }

        public TextView getTextView() {
            return textView;
        }

        public TextView getTvAddress() {
            return tvAddress;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case (R.id.textView): {
                    Location location = items.get(getAdapterPosition()).getLocation();
                    LatLng LatLng = new LatLng(location.getY(), location.getX());
                    onSearchItemListener.onSearchItemClick(LatLng);
                }
            }

        }
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_item, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextView().setText(items.get(position).getTitle());
        viewHolder.getTvAddress().setText(items.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void getList(ArrayList<ItemsItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}