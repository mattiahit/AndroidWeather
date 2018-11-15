package pl.mattiahit.androidweather.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mattiahit.androidweather.R;

public class WeatherView extends RelativeLayout {

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
    @BindView(R.id.goToWeatherDetailsBtn)
    Button goToWeatherDetailsBtn;
    @BindString(R.string.temperature)
    String temperature;
    @BindString(R.string.wind)
    String wind;
    @BindString(R.string.clouds)
    String clouds;
    @BindString(R.string.pressure)
    String pressure;

    public WeatherView(Context context){
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.location_weather_detail_adapter, this, true);
        ButterKnife.bind(view);
    }

    public void initWeatherView(JsonObject weatherObject){
        String iconName = weatherObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();
        Picasso.get().load("http://openweathermap.org/img/w/" + iconName + ".png").into(location_weather_icon);
        location_weather_city_name.setText(weatherObject.get("hour").getAsString());
        int currentTemp = weatherObject.getAsJsonObject("main").get("temp").getAsInt() - 273;
        location_weather_temperature.setText(temperature + ": " +currentTemp + "°C");
        int windSpeed = weatherObject.getAsJsonObject("wind").get("speed").getAsInt();
        location_weather_wind.setText(wind + ": " + windSpeed + "km/h");
        int cloudLevel = weatherObject.getAsJsonObject("clouds").get("all").getAsInt();
        location_weather_clouds.setText(clouds + ": " + cloudLevel + "%");
        double pressure = weatherObject.getAsJsonObject("main").get("pressure").getAsDouble();
        location_weather_pressure.setText(pressure + ": " + pressure + "hPa");

        manage_favourites_btn.setVisibility(View.GONE);
        goToWeatherDetailsBtn.setVisibility(View.GONE);
    }

    public WeatherView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.location_weather_detail_adapter, this, true);
        ButterKnife.bind(view);
    }
}
