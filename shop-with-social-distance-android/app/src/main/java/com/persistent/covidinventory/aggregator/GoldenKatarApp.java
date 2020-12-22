package com.persistent.covidinventory.aggregator;

import android.app.Application;

public class GoldenKatarApp extends Application {
    private static GoldenKatarApp mInstance;

    public static GoldenKatarApp getInstance() {
        if (mInstance == null) {
            mInstance = new GoldenKatarApp();
        }
        return mInstance;
    }
}
