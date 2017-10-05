package com.example.android.camera2video;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.content.ContentValues.TAG;


public class BackgroundService extends IntentService {


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BackgroundService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int i;
       // for (i = 0; i < 100; i++) {
            System.out.println("yo");
            Log.i(TAG, "background work");

        //}
    }
}
