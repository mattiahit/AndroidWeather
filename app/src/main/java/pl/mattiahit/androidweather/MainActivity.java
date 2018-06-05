package pl.mattiahit.androidweather;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.Serializable;

import pl.mattiahit.androidweather.utils.AppDatabase;
import pl.mattiahit.androidweather.utils.Navigator;

public class MainActivity extends AppCompatActivity {

    private Navigator navigator;
    private AppDatabase appDatabase;
    private boolean locationPermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        this.initUtils();
        this.checkPermissions();
        super.onResume();
    }

    private void initUtils(){
        if(this.navigator == null)
            this.navigator = new Navigator(this);
        if(this.appDatabase == null)
            this.appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "weatherAppDatabase").build();
    }

    public void storeData(String key, Serializable data){
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, data);
        this.getIntent().putExtras(bundle);
    }

    public Serializable getStoredData(String key){
        return this.getIntent().getExtras().getSerializable(key);
    }

    private void checkPermissions(){
        this.locationPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean isLocationPermissionGranted(){
        return this.locationPermissionGranted;
    }

    public String getAppVersion(){
        try {
            return this.getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        }catch (Exception e){
            return "";
        }
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public Navigator getNavigator() {
        return navigator;
    }
}
