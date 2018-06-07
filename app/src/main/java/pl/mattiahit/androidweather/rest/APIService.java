package pl.mattiahit.androidweather.rest;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    @GET("data/2.5/weather")
    Call<JsonObject> getWeatherForCity(@Query("q") String cityName, @Query("appid") String appid);

    @GET("data/2.5/weather")
    Call<JsonObject> getWeatherForLocation(@Query("lat") double lat, @Query("lon") double lon, @Query("appid") String appid);
}
