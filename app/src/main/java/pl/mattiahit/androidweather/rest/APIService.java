package pl.mattiahit.androidweather.rest;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    @GET("weather")
    Call<JsonObject> getWeatherForCity(@Query("q") String cityName, @Query("appid") String appid);

    @GET("forecast")
    Call<JsonObject> getForecastForCity(@Query("q") String cityName, @Query("appid") String appid);

    @GET("weather")
    Call<JsonObject> getWeatherForLocation(@Query("lat") double lat, @Query("lon") double lon, @Query("appid") String appid);

    @GET("forecast")
    Call<JsonObject> getForecastForLocation(@Query("lat") double lat, @Query("lon") double lon, @Query("appid") String appid);
}
