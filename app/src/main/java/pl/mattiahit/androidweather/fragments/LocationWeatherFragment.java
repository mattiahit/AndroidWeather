package pl.mattiahit.androidweather.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.mattiahit.androidweather.MainActivity;
import pl.mattiahit.androidweather.R;
import pl.mattiahit.androidweather.adapters.ExpandableForecastAdapter;
import pl.mattiahit.androidweather.models.FavouriteLocation;
import pl.mattiahit.androidweather.rest.WeatherForCityRestTask;
import pl.mattiahit.androidweather.utils.Tools;

public class LocationWeatherFragment extends Fragment {

    private MainActivity mainActivity;
    private List<String> listForecastGroup;
    private HashMap<String, JsonArray> listForecastChild;
    private FavouriteLocation favouriteLocation;
    private ExpandableForecastAdapter expandableForecastAdapter;

    @BindView(R.id.expandableListView)
    ExpandableListView expandableListView;
    @BindView(R.id.forecast_title)
    TextView forecast_title;

    @BindString(R.string.forecast_for_city)
    String forecast_for_city;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_weather, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        this.mainActivity = (MainActivity) this.getActivity();
        this.listForecastGroup = new ArrayList<>();
        this.listForecastChild = new HashMap<>();
        this.expandableForecastAdapter = new ExpandableForecastAdapter(this.mainActivity, this.listForecastGroup, this.listForecastChild);
        this.expandableListView.setAdapter(this.expandableForecastAdapter);
        this.initForecast();
        super.onResume();
    }

    @OnClick(R.id.favourities_btn)
    public void backToFav(){
        this.mainActivity.getNavigator().goToFavouriteList();
    }

    private void initForecast(){
        this.favouriteLocation = (FavouriteLocation) this.getArguments().getSerializable(MainActivity.DETAIL_REQUEST_LOCATION);
        this.forecast_title.setText(String.format(forecast_for_city, this.favouriteLocation.getLocationName()));

        WeatherForCityRestTask weatherForCityRestTask = new WeatherForCityRestTask(this.favouriteLocation.getLocationName()){
            @Override
            public void doOnResult(int code, JsonObject object) {
                super.doOnResult(code, object);
                Tools.showLog("code = " + code);
                if(code == 200){
                    Tools.showLog("object = " + object.toString());
                    initData(object.getAsJsonArray("list"));
                    expandableForecastAdapter.notifyDataSetChanged();
                }
            }
        };
        weatherForCityRestTask.get5DayWeather();
    }

    private void initData(JsonArray sourceArray) {
        if(sourceArray.size() > 0) {
            String tempDayTxt = "";
            JsonArray hourArrray = null;

            for (JsonElement sourceElement : sourceArray) {
                JsonObject sourceObject = sourceElement.getAsJsonObject();
                String sourceDay = sourceObject.get("dt_txt").getAsString().substring(0, 10);
                String sourceHour = sourceObject.get("dt_txt").getAsString().substring(11, 16);

                if (!tempDayTxt.equals(sourceDay)) {
                    if(hourArrray!=null) {
                        Tools.showLog(tempDayTxt + " => " + hourArrray.toString());
                        this.listForecastGroup.add(tempDayTxt);
                        this.listForecastChild.put(tempDayTxt, hourArrray);
                    }
                    tempDayTxt = sourceDay;
                    hourArrray = new JsonArray();
                }

                JsonObject hourObject = new JsonObject();
                hourObject.addProperty("hour", sourceHour);
                hourObject.add("main", sourceObject.get("main").getAsJsonObject());
                hourObject.add("weather", sourceObject.get("weather").getAsJsonArray());
                hourObject.add("clouds", sourceObject.get("clouds") != null ? sourceObject.get("clouds").getAsJsonObject() : new JsonObject());
                hourObject.add("wind", sourceObject.get("wind") != null ? sourceObject.get("wind").getAsJsonObject() : new JsonObject());
                hourObject.add("rain", sourceObject.get("rain") != null ? sourceObject.get("rain").getAsJsonObject() : new JsonObject());
                hourObject.add("sys", sourceObject.get("sys").getAsJsonObject());

                hourArrray.add(hourObject);
            }
        }
    }
}
