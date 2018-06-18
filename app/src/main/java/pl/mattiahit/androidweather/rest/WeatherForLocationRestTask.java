package pl.mattiahit.androidweather.rest;

import android.location.Location;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherForLocationRestTask extends BaseRestTask {

    private Location location;

    public WeatherForLocationRestTask(Location location){
        this.location = location;
    }

    public void getWeather(){
        if(this.location != null) {
            getAPIService().getWeatherForLocation(this.location.getLatitude(), this.location.getLongitude(), API_KEY).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    doOnResult(response.code(), response.body());
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    doOnResult(-100, null);
                }
            });
        }
    }

    public void get5DayWeather(){
        if(this.location != null) {
            getAPIService().getForecastForLocation(this.location.getLatitude(), this.location.getLongitude(), API_KEY).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    doOnResult(response.code(), response.body());
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    doOnResult(-100, null);
                }
            });
        }
    }
}
