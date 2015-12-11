package ru.onyanov.geni.network;

import android.content.Context;

public interface ImageLoadCallback {

    Context getContext();

    void onFileLoaded(String path);

    void onError();
}
