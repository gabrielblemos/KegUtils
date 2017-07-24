package com.keg.kegutilsexample.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.keg.kegutils.RecyclerView.EndlessRecyclerViewAdapter;
import com.keg.kegutils.RecyclerView.ViewHolder.BaseViewHolder;
import com.keg.kegutilsexample.R;

/**
 * Created by gamer on 15/07/2017.
 */

public class CustomAdapter extends EndlessRecyclerViewAdapter<String, BaseViewHolder<String>>{

    public CustomAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        BaseViewHolder viewHolder;
        if (viewType == VIEW_TYPE_ACTIVITY) {
            view = inflater.inflate(R.layout.information_card, parent, false);
            viewHolder = new BaseViewHolder(view);
        } else {
            viewHolder = super.onCreateViewHolder(parent, viewType);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_ACTIVITY) {
            String current = getDataSet().get(position);
            holder.setInformacao(current);
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

}
