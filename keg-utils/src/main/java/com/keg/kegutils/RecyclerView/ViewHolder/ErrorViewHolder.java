package com.keg.kegutils.RecyclerView.ViewHolder;

import android.view.View;

import com.keg.kegutils.RecyclerView.DataLoader;

public class ErrorViewHolder<T> extends BaseViewHolder<T> {

    private DataLoader dataLoader;

    public ErrorViewHolder(View itemView) {
        super(itemView);

        this.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataLoader.loadLastPage();
            }
        });
    }

    public void setDataLoader(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

}
