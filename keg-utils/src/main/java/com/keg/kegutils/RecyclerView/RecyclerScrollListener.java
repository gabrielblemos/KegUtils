package com.keg.kegutils.RecyclerView;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Material de apoio:
 * https://gist.github.com/nesquena/8a976dd3d6f866518db2cfe7f9cb0db7
 */
public class RecyclerScrollListener extends RecyclerView.OnScrollListener {

    protected LoadableAdapter loadableAdapter;
    private LinearLayoutManager mLayoutManager;

    public RecyclerScrollListener(LoadableAdapter loadableAdapter, LinearLayoutManager layoutManager) {
        this.loadableAdapter = loadableAdapter;
        this.mLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int visibleItemCount = mLayoutManager.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();
        int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

        if (!loadableAdapter.isLoading && !loadableAdapter.isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0 && totalItemCount >= 10) {
                loadableAdapter.loadNextPage();
            }
        }
    }
}
