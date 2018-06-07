package pl.mattiahit.androidweather.rest;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class BaseRestTask {

    protected final String SERVER_ADDRESS = "http://api.openweathermap.org/";
    protected final String API_KEY = "d85aecd601e4f0c45aacbc1362e6ea9f";

    protected APIService getAPIService(){
        return RetrofitClient.getClient(SERVER_ADDRESS).create(APIService.class);
    }

    public void doOnResult(int code, JsonObject object){

    }
}
