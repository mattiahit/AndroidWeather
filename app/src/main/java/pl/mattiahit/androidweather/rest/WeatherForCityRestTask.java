package pl.mattiahit.androidweather.rest;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherForCityRestTask extends BaseRestTask {

    private String cityName;

    public WeatherForCityRestTask(String city){
        this.cityName = city;
    }

    public void getWeather(){
        this.getAPIService().getWeatherForCity(this.cityName, API_KEY).enqueue(new Callback<JsonObject>() {
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

    public void get5DayWeather(){
        this.getAPIService().get5DayWeatherForCity(this.cityName, API_KEY).enqueue(new Callback<JsonObject>() {
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
