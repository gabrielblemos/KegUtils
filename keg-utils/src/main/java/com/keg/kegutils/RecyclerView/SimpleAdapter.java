package com.keg.kegutils.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keg.kegutils.RecyclerView.ViewHolder.BaseViewHolder;
import com.keg.kegutils.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilizável quando já se possui os elementos do recycler.
 * Created by gamer on 15/07/2017.
 */

public class SimpleAdapter<T, VH extends BaseViewHolder<T>> extends RecyclerView.Adapter<VH> {

    protected final int VIEW_TYPE_EMPTY = 0;
    protected final int VIEW_TYPE_ACTIVITY = 1;
    protected final int VIEW_TYPE_FINISHED = 2;

    protected List<T> dataSet;
    protected Context mContext;
    protected boolean displayEmpty;
    protected final LayoutInflater inflater;

    public SimpleAdapter(Context context) {
        this(context, true);
    }

    public SimpleAdapter(Context context, boolean displayFinished) {
        this.mContext = context;
        this.displayEmpty = displayEmpty;
        this.inflater = LayoutInflater.from(this.mContext);
    }

    public List<T> getDataSet() {
        if (dataSet == null) {
            dataSet = new ArrayList<>();
        }
        return dataSet;
    }

    public void insertData(T newData) {
        getDataSet().add(newData);
        notifyItemInserted(getDataSet().indexOf(newData));
    }

    public void insertData(List<T> newData) {
        getDataSet().addAll(newData);
        notifyDataSetChanged();
    }

    public void removeData(T data) {
        int indexOf = getDataSet().indexOf(data);
        getDataSet().remove(data);
        notifyItemRemoved(indexOf);
    }

    public void removeAllData() {
        getDataSet().clear();
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        BaseViewHolder<T> viewHolder = null;
        if (viewType == VIEW_TYPE_EMPTY || viewType == VIEW_TYPE_FINISHED) {
            view = inflater.inflate(R.layout.information_card, parent, false);
            viewHolder = new BaseViewHolder(view);
        } else if (viewType == VIEW_TYPE_ACTIVITY) {
            view = inflater.inflate(R.layout.information_card, parent, false);
            viewHolder = new BaseViewHolder(view);
        }
        return (VH) viewHolder;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        int viewType = getItemViewType(position);
        TextView textView = (TextView) holder.itemView.findViewById(R.id.card_message);
        if (viewType == VIEW_TYPE_EMPTY) {
            textView.setText(R.string.endless_recyclcer_empty);
        } else if (viewType == VIEW_TYPE_FINISHED) {
            textView.setText(R.string.endless_recyclcer_load_finished);
        } else if (viewType == VIEW_TYPE_ACTIVITY) {
            T element = getDataSet().get(position);
            textView.setText(element.toString());
        }
    }

    @Override
    public int getItemCount() {
        int itemCount;
        if (displayEmpty && getDataSet().isEmpty()) {
            itemCount = 1;
        } else {
            itemCount = getDataSet().size();
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if (displayEmpty && getDataSet().isEmpty()) {
            viewType = VIEW_TYPE_EMPTY;
        } else {
            viewType = VIEW_TYPE_ACTIVITY;
        }
        return viewType;
    }
}
