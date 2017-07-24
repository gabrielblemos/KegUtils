package com.keg.kegutils.RecyclerView;

import java.util.List;

public class DataLoader<T> {

    public static final int PAGE_SIZE = 5;

    private String errorMessage;
    protected int lastPageLoaded = 1;
    public boolean isLoading = false;
    protected boolean isLastPage = false;
    protected boolean hasConnectionError = false;

    public DataLoader() {
        loadLastPage();
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public boolean hasConnectionError() {
        return hasConnectionError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    protected void setErrorMessage(String errorMessage) {
        this.hasConnectionError = true;
        this.errorMessage = errorMessage;
    }

    public void loadLastPage() {
        loadPage(lastPageLoaded);
    }

    public void loadPage(int page) {
        hasConnectionError = false;
        isLoading = true;
    }

    public void refreshStatus(List<T> object) {
        if (PAGE_SIZE - object.size() != 0) {
            isLastPage = true;
        }
        isLastPage = false;
    }
}
