package pl.mattiahit.androidweather;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import pl.mattiahit.androidweather.async.GetFavLocations;
import pl.mattiahit.androidweather.models.FavouriteLocation;
import pl.mattiahit.androidweather.utils.AppDatabase;
import pl.mattiahit.androidweather.utils.Navigator;
import pl.mattiahit.androidweather.utils.Tools;

public class MainActivity extends AppCompatActivity implements LocationListener{

    public final static String DETAIL_REQUEST_LOCATION = "detail_request_location";

    private final int PERMISSION_REQUEST_LOCATION = 123;
    private Navigator navigator;
    private AppDatabase appDatabase;
    private LocationManager locationManager;
    private Location location;
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

        GetFavLocations getFavLocations = new GetFavLocations(this.getAppDatabase());
        getFavLocations.setListener(new GetFavLocations.AsyncTaskListener() {
            @Override
            public void onAsyncTaskFinished(List<FavouriteLocation> value) {
                if(value.size() > 0)
                    getNavigator().goToFavouriteList();
                else
                    getNavigator().goToHome();
            }
        });
        getFavLocations.execute();

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
        this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    private void initLocationListener(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            Tools.showLog("Location Permission Denied!");

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_LOCATION);

            this.locationPermissionGranted = false;

        }else{
            Tools.showLog("Location Permission Granted!");
            this.locationPermissionGranted = true;
            this.location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, this);
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode){
            case PERMISSION_REQUEST_LOCATION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                    initLocationListener();
                }else
                    locationPermissionGranted = false;
                break;
        }
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

    public void showToast(String info){
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
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
