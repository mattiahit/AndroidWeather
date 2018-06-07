package pl.mattiahit.androidweather.api;

import android.content.Context;

@Deprecated
public class LocationWeatherApi extends BaseApi {

    public LocationWeatherApi(Context context) {
        super(context);
    }

    public void getCurrentViaLatLon(double lat, double lon){
        this.connectToAPI("?lat="+lat+"&lon="+lon);
    }

    public void getCurrentViaCityName(String city){
        this.connectToAPI("?q="+city);
    }
}
