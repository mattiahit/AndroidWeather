package pl.mattiahit.androidweather;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

public class WeatherApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}
