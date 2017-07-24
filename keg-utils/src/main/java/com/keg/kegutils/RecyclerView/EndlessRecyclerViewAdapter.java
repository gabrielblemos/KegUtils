package com.keg.kegutils.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keg.kegutils.RecyclerView.ViewHolder.BaseViewHolder;
import com.keg.kegutils.RecyclerView.ViewHolder.ErrorViewHolder;
import com.keg.kegutils.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Material de apoio:
 * https://gist.github.com/nesquena/a988aac278cff59a9a69
 */
public class EndlessRecyclerViewAdapter<T, VH extends BaseViewHolder<T>> extends RecyclerView.Adapter<VH> {

    protected final int VIEW_TYPE_EMPTY = 0;
    protected final int VIEW_TYPE_LOADING = 1;
    protected final int VIEW_TYPE_FINISHED = 2;
    protected final int VIEW_TYPE_ACTIVITY = 3;

    protected final int VIEW_TYPE_CONNECTION_ERROR = -1;
    protected final int VIEW_TYPE_UNEXPECTED_ERROR = -2;

    protected Context mContext;
    protected boolean displayEmpty;
    protected final LayoutInflater inflater;

    protected List<T> dataSet;
    protected DataLoader dataLoader;

    public EndlessRecyclerViewAdapter(Context context) {
        this(context, true);
    }

    public EndlessRecyclerViewAdapter(Context context, boolean displayEmpty) {
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

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        BaseViewHolder<T> viewHolder = null;
        if (viewType == VIEW_TYPE_CONNECTION_ERROR || viewType == VIEW_TYPE_UNEXPECTED_ERROR) {
            view = inflater.inflate(R.layout.information_card, parent, false);
            viewHolder = new ErrorViewHolder(view);
            ((ErrorViewHolder) viewHolder).setDataLoader(dataLoader);
        } else if (viewType == VIEW_TYPE_EMPTY || viewType == VIEW_TYPE_FINISHED) {
            view = inflater.inflate(R.layout.information_card, parent, false);
            viewHolder = new BaseViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            view = inflater.inflate(R.layout.loading_card, parent, false);
            viewHolder = new BaseViewHolder(view);
        } else if (viewType == VIEW_TYPE_ACTIVITY) {
            //view = inflater.inflate(R.layout.restaurante_card, parent, false);
            //viewHolder = new RestauranteViewHolder(view);
        }

        return (VH) viewHolder;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        int viewType = getItemViewType(position);

        if (viewType == VIEW_TYPE_ACTIVITY || viewType == VIEW_TYPE_LOADING) {
            return;
        }

        TextView textView = (TextView) holder.itemView.findViewById(R.id.card_message);
        if (viewType == VIEW_TYPE_UNEXPECTED_ERROR) {
            textView.setText(R.string.endless_recyclcer_unexpedted_error);
        } else if (viewType == VIEW_TYPE_CONNECTION_ERROR) {
            textView.setText(dataLoader.getErrorMessage());
        } else if (viewType == VIEW_TYPE_EMPTY) {
            textView.setText(R.string.endless_recyclcer_load_time_out);
        } else if (viewType == VIEW_TYPE_FINISHED) {
            textView.setText(R.string.endless_recyclcer_load_finished);
        }
    }

    @Override
    public int getItemViewType(int position) {
        boolean firstElement = dataSet.size() == 0;
        boolean lastElement = position == dataSet.size();

        if (dataLoader != null && (firstElement || lastElement)) {
            if (dataLoader.isLoading()) {
                return VIEW_TYPE_LOADING;
            } else if (dataLoader.hasConnectionError()) {
                return VIEW_TYPE_CONNECTION_ERROR;
            } else if (dataLoader.isLastPage()) {
                if (firstElement) {
                    return VIEW_TYPE_EMPTY;
                } else {
                    return VIEW_TYPE_FINISHED;
                }
            } else {
                return VIEW_TYPE_UNEXPECTED_ERROR;
            }
        } else {
            return VIEW_TYPE_ACTIVITY;
        }
    }

    @Override
    public int getItemCount() {
        int count;
        if (displayEmpty && (getDataSet().isEmpty() || (dataLoader != null && !dataLoader.isLoading()))) {
            return 1;
        } else {
            count = getDataSet().size();
            if (dataLoader != null) {
                count++;
            }
        }
        return count;
//        if (dataLoader != null) {
//            return getDataSet().size() + 1;
//        } else {
//            return getDataSet().size();
//        }
    }

    @Override
    public long getItemId(int position) {
        if (position == getItemCount()) {
            return -1l;
        }

        return super.getItemId(position);
    }

    public void reinicializaListagem() {
        dataSet = new ArrayList<>();
        if (dataLoader != null) {
            dataLoader.loadPage(0);
        }
        notifyDataSetChanged();
    }

    public void setDataLoader(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

}
