package test.com.mx.testfernandopiedra.commons;

import android.app.Application;

/**
 * Created by fernandostone on 17/07/17.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RequestManager.initInstance(this);
    }
}
