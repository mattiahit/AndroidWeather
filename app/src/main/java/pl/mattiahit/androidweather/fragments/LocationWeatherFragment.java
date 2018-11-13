package pl.mattiahit.androidweather.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.mattiahit.androidweather.MainActivity;
import pl.mattiahit.androidweather.R;
import pl.mattiahit.androidweather.adapters.ForecastLocationAdapter;
import pl.mattiahit.androidweather.models.FavouriteLocation;
import pl.mattiahit.androidweather.rest.WeatherForCityRestTask;
import pl.mattiahit.androidweather.utils.Tools;

public class LocationWeatherFragment extends Fragment {

    private MainActivity mainActivity;
    private RecyclerView.LayoutManager mLayoutManager;
    private FavouriteLocation favouriteLocation;

    @BindView(R.id.fav_list)
    RecyclerView fav_list;
    @BindView(R.id.go_home_btn)
    Button go_home_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_favourities, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        this.mainActivity = (MainActivity) this.getActivity();
        this.initForecast();


        super.onResume();
    }

    private void initForecast(){
        this.go_home_btn.setText(R.string.favourities);
        this.favouriteLocation = (FavouriteLocation) this.getArguments().getSerializable(MainActivity.DETAIL_REQUEST_LOCATION);

        WeatherForCityRestTask weatherForCityRestTask = new WeatherForCityRestTask(this.favouriteLocation.getLocationName()){
            @Override
            public void doOnResult(int code, JsonObject object) {
                super.doOnResult(code, object);
                Tools.showLog("code = " + code);
                if(code == 200){
                    Tools.showLog("object = " + object.toString());
                    fav_list.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(mainActivity);
                    fav_list.setLayoutManager(mLayoutManager);
                    ForecastLocationAdapter forecastLocationAdapter = new ForecastLocationAdapter(mainActivity, parseJSON(object.getAsJsonArray("list")));
                    fav_list.setAdapter(forecastLocationAdapter);
                }
            }
        };
        weatherForCityRestTask.get5DayWeather();

    }

    private JsonArray parseJSON(JsonArray sourceArray){
        JsonArray resultDayArray = new JsonArray();
        String resultDate = "";
        JsonArray resultHourArray = new JsonArray();
        JsonObject resultHourObject = null;

        for(int i = 0; i < sourceArray.size(); i++){
            JsonObject sourceObject = sourceArray.get(i).getAsJsonObject();
            String[] dateTime = sourceObject.get("dt_txt").getAsString().split(" ");
            String sourceDate = dateTime[0];
            String sourceHour = dateTime[1];
            if(!resultDate.equals(sourceDate)){
                if(resultHourObject != null){
                    JsonObject resultDayObject = new JsonObject();
                    resultDayObject.add(resultDate,resultHourArray);
                    resultDayArray.add(resultDayObject);
                    resultHourArray = new JsonArray();
                }
                resultDate = sourceDate;
            }
            resultHourObject = new JsonObject();
            resultHourObject.addProperty("hour",sourceHour);
            resultHourObject.add("main",sourceObject.get("main").getAsJsonObject());
            resultHourObject.add("weather",sourceObject.get("weather").getAsJsonArray());
            resultHourObject.add("clouds",sourceObject.get("clouds").getAsJsonObject());
            resultHourObject.add("wind",sourceObject.get("wind").getAsJsonObject());
            resultHourObject.add("rain",sourceObject.get("rain").getAsJsonObject());
            resultHourArray.add(resultHourObject);

        }

        Tools.showLog("Parse result = " + resultDayArray.toString());

        return resultDayArray;
    }

    @OnClick(R.id.go_home_btn)
    public void goToFav(){
        this.mainActivity.getNavigator().goToFavouriteList();
    }
}
