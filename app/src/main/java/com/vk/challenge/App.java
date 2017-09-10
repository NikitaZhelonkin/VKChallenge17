package com.vk.challenge;

import android.app.Application;
import com.vk.sdk.VKSdk;

/**
 * Created by nikita on 06.09.17.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        VKSdk.initialize(this);
    }

}
