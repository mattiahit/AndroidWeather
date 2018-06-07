package pl.mattiahit.androidweather.utils;

import android.util.Log;

import java.util.Random;

public class Tools {

    public static void showLog(String info){
        Log.i("CUSTOM_LOG >>> ", info);
    }

    public static long getRandomLongId(){
        Random random = new Random();
        return random.nextLong();
    }
}
