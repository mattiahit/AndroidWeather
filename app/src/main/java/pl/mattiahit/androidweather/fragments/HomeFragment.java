package pl.mattiahit.androidweather.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.mattiahit.androidweather.MainActivity;
import pl.mattiahit.androidweather.R;
import pl.mattiahit.androidweather.async.GetFavLocations;
import pl.mattiahit.androidweather.async.InsertFavLocation;
import pl.mattiahit.androidweather.dialogs.ConfirmDialog;
import pl.mattiahit.androidweather.models.FavouriteLocation;
import pl.mattiahit.androidweather.rest.WeatherForCityRestTask;
import pl.mattiahit.androidweather.rest.WeatherForLocationRestTask;
import pl.mattiahit.androidweather.utils.Tools;

public class HomeFragment extends Fragment {

    @BindView(R.id.city_name_edittext)
    EditText city_name_edittext;
    @BindView(R.id.location_weather_icon)
    ImageView location_weather_icon;
    @BindView(R.id.location_weather_city_name)
    TextView location_weather_city_name;
    @BindView(R.id.location_weather_wind)
    TextView location_weather_wind;
    @BindView(R.id.location_weather_pressure)
    TextView location_weather_pressure;
    @BindView(R.id.location_weather_clouds)
    TextView location_weather_clouds;
    @BindView(R.id.manage_favourites_btn)
    ImageButton manage_favourites_btn;
    @BindView(R.id.location_weather_temperature)
    TextView location_weather_temperature;
    @BindString(R.string.no_permission_for_localization)
    String no_permission_for_localization;
    @BindString(R.string.temperature)
    String temperature;
    @BindString(R.string.wind)
    String wind;
    @BindString(R.string.clouds)
    String clouds;
    @BindString(R.string.pressure)
    String pressure;
    @BindString(R.string.add_to_favourities_question)
    String add_to_favourities_question;
    @BindString(R.string.location_as_favourite)
    String location_as_favourite;

    private MainActivity mainActivity;
    private JsonObject currentObject;
    private List<FavouriteLocation> listOfFavourities;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        this.mainActivity = (MainActivity) this.getActivity();
        this.checkForFavourities();
        super.onResume();
    }

    @OnClick(R.id.search_by_city_name_btn)
    public void searchWeatherForCity(){
        if(this.city_name_edittext.getText().length() > 0){
            WeatherForCityRestTask weatherForCityRestTask = new WeatherForCityRestTask(this.city_name_edittext.getText().toString()){
                @Override
                public void doOnResult(int code, JsonObject object) {
                    cleanLocationInfo();
                    if(code == 200){
                        initLocationWeatherInfo(object);
                    }
                }
            };
            weatherForCityRestTask.getWeather();
        }
    }

    @OnClick(R.id.auto_detect_btn)
    public void autoDetectLocationForWeather(){
        if(this.mainActivity.isLocationPermissionGranted()) {
            WeatherForLocationRestTask weatherForLocationRestTask = new WeatherForLocationRestTask(this.mainActivity.getLocation()) {
                @Override
                public void doOnResult(int code, JsonObject object) {
                    cleanLocationInfo();
                    if (code == 200) {
                        initLocationWeatherInfo(object);
                    }
                }
            };
            weatherForLocationRestTask.getWeather();
        }else{
            this.mainActivity.showToast(no_permission_for_localization);
        }
    }

    @OnClick(R.id.manage_favourites_btn)
    public void addToFavourities(){
        if(currentObject != null) {
            final ConfirmDialog confirmDialog = ConfirmDialog.newInstance(add_to_favourities_question);
            confirmDialog.setConfirmListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmDialog.dismiss();
                    FavouriteLocation favouriteLocation = new FavouriteLocation();
                    favouriteLocation.setId(Tools.getRandomLongId());
                    favouriteLocation.setLocationName(currentObject.get("name").getAsString());
                    favouriteLocation.setLocationLat(currentObject.getAsJsonObject("coord").get("lat").getAsDouble());
                    favouriteLocation.setLocationLon(currentObject.getAsJsonObject("coord").get("lon").getAsDouble());

                    InsertFavLocation insertFavLocation = new InsertFavLocation(mainActivity.getAppDatabase(), favouriteLocation);
                    insertFavLocation.setListener(new InsertFavLocation.AsyncTaskListener() {
                        @Override
                        public void onAsyncTaskFinished(Integer value) {
                            mainActivity.showToast(location_as_favourite);
                        }
                    });
                    insertFavLocation.execute();
                }
            });
            confirmDialog.show(mainActivity.getSupportFragmentManager(),"question_dialog");
        }
    }

    private void cleanLocationInfo(){
        manage_favourites_btn.setVisibility(View.GONE);
        location_weather_icon.setImageDrawable(null);
        location_weather_city_name.setText("");
        location_weather_temperature.setText("");
        location_weather_wind.setText("");
        location_weather_clouds.setText("");
        currentObject = null;
    }

    private void initLocationWeatherInfo(JsonObject object){
        Tools.showLog(object.toString());
        this.currentObject = object;
        manage_favourites_btn.setVisibility(View.VISIBLE);
        String iconName = object.getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();
        Picasso.get().load("http://openweathermap.org/img/w/" + iconName + ".png").into(location_weather_icon);
        location_weather_city_name.setText(object.get("name").getAsString());
        int currentTemp = object.getAsJsonObject("main").get("temp").getAsInt() - 273;
        location_weather_temperature.setText(this.temperature + ": " +currentTemp + "°C");
        int windSpeed = object.getAsJsonObject("wind").get("speed").getAsInt();
        location_weather_wind.setText(this.wind + ": " + windSpeed + "km/h");
        int cloudLevel = object.getAsJsonObject("clouds").get("all").getAsInt();
        location_weather_clouds.setText(this.clouds + ": " + cloudLevel + "%");
        double pressure = object.getAsJsonObject("main").get("pressure").getAsDouble();
        location_weather_pressure.setText(this.pressure + ": " + pressure + "hPa");
    }

    private void checkForFavourities(){
        GetFavLocations getFavLocations = new GetFavLocations(this.mainActivity.getAppDatabase());
        getFavLocations.setListener(new GetFavLocations.AsyncTaskListener() {
            @Override
            public void onAsyncTaskFinished(List<FavouriteLocation> value) {
                listOfFavourities = value;
                Tools.showLog("listOfFavourities = " + listOfFavourities.size());
            }
        });
        getFavLocations.execute();
    }
}
