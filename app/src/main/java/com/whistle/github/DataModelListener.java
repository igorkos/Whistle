package com.whistle.github;

public interface DataModelListener {
    public void onUpdated();
    public void onError(String error);
}
