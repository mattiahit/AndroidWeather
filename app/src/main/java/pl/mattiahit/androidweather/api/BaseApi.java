package pl.mattiahit.androidweather.api;

import android.content.Context;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

@Deprecated
public class BaseApi {

    private final String SERVER_ADDRESS = "http://api.openweathermap.org/data/2.5/weather";
    private final String API_KEY = "d85aecd601e4f0c45aacbc1362e6ea9f";
    private Context context;

    public BaseApi(Context context){
        this.context = context;
    }

    protected void connectToAPI(String question){
        Ion.with(this.context)
                .load(this.SERVER_ADDRESS + question + "&appid="+this.API_KEY)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        parseResponse(result);
                    }
                });
    }

    public void parseResponse(JsonObject jsonObject){

    }

}
