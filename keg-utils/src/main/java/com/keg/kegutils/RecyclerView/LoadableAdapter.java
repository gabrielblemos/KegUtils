package com.keg.kegutils.RecyclerView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.keg.kegutils.R;
import com.keg.kegutils.RecyclerView.LoadSystem.LoadSystem;
import com.keg.kegutils.RecyclerView.ViewHolder.BaseViewHolder;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by gamer on 15/07/2017.
 */

public abstract class LoadableAdapter<T, VH extends BaseViewHolder<T>> extends SimpleAdapter {

    protected final int VIEW_TYPE_LOADING = 3;
    protected final int VIEW_TYPE_ERROR = 4;

    protected int lastLoadedPage;
    protected boolean isLoading, isLastPage, hasConnectionError;
    protected LoadSystem loadSystem = new LoadSystem(mContext, new LoadSystem.OnLoad() {
        @Override
        public void onSuccess(final JSONObject response) {
            isLoading = false;
            hasConnectionError = false;
            insertData(loadParser(response));
        }

        @Override
        public void onFailure(VolleyError error) {
            isLoading = false;
            hasConnectionError = true;
        }
    });

    public LoadableAdapter(Context context) {
        this(context, true);
    }

    public LoadableAdapter(Context context, boolean displayEmpty) {
        super(context, displayEmpty);
        lastLoadedPage = 0;
        isLoading = false;
        isLastPage = false;
        hasConnectionError = false;
        loadNextPage();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        BaseViewHolder<T> viewHolder;
        if (viewType == VIEW_TYPE_LOADING) {
            view = inflater.inflate(R.layout.loading_card, parent, false);
            viewHolder = new BaseViewHolder(view);
        } else if (viewType == VIEW_TYPE_ERROR) {
            view = inflater.inflate(R.layout.information_card, parent, false);
            viewHolder = new BaseViewHolder(view);
        } else {
            viewHolder = super.onCreateViewHolder(parent, viewType);
        }
        return (VH) viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_EMPTY) {
            TextView textView = (TextView) holder.itemView.findViewById(R.id.card_message);
            textView.setText(R.string.endless_recyclcer_unexpedted_error);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadLastPage();
                }
            });
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        int itemCount;
        if (isLoading || hasConnectionError) {
            itemCount = getDataSet().size() + 1;
        } else {
            itemCount = super.getItemCount();
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        boolean isLastItem = position + 1 == getItemCount();
        if (isLastItem && isLoading) {
            viewType = VIEW_TYPE_LOADING;
        } else if (isLastItem && hasConnectionError) {
            viewType = VIEW_TYPE_ERROR;
        } else {
            viewType = super.getItemViewType(position);
        }
        return viewType;
    }

    public void loadNextPage() {
        if (loadSystem == null) {
            return;
        }

        lastLoadedPage++;
        loadLastPage();
    }

    protected void loadLastPage() {
        if (loadSystem == null) {
            return;
        }

        isLoading = true;
        isLastPage = false;
        hasConnectionError = true;
        loadSystem.startLoad(buildUrl(lastLoadedPage));
    }

    protected abstract String buildUrl(int loadPage);

    protected abstract List<T> loadParser(JSONObject objectJSON);

}
