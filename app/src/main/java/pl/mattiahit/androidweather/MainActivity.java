package pl.mattiahit.androidweather;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.Serializable;

import pl.mattiahit.androidweather.api.LocationWeatherApi;
import pl.mattiahit.androidweather.utils.AppDatabase;
import pl.mattiahit.androidweather.utils.Navigator;
import pl.mattiahit.androidweather.utils.Tools;

public class MainActivity extends AppCompatActivity implements LocationListener{

    private Navigator navigator;
    private AppDatabase appDatabase;
    private LocationManager locationManager;
    private Location location;
    private String provider;
    private boolean locationPermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initRequiredObjects();
    }

    @Override
    protected void onResume() {
        this.initLocationListener();
        this.getNavigator().goToHome();

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.locationManager.removeUpdates(this);
    }

    private void initRequiredObjects(){
        this.navigator = new Navigator(this);
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

    private void initLocationListener(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        }else{
            this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);
            this.location = locationManager.getLastKnownLocation(provider);
            this.locationManager.requestLocationUpdates(provider, 400, 1, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Tools.showLog("grantResults = " + grantResults[0]);

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

    public Location getLocation() {
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
