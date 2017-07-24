package com.keg.kegutils.RecyclerView.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class BaseViewHolder<T> extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener {

    protected T informacao;
    protected Context mContext;

    public BaseViewHolder(View itemView) {
        super(itemView);

        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setInformacao(T informacao) {
        this.informacao = informacao;
        onBindInformacao();
    }

    protected void onBindInformacao() {
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    public T getInformacao() {
        return informacao;
    }

}
