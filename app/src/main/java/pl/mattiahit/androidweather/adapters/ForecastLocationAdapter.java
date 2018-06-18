package pl.mattiahit.androidweather.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mattiahit.androidweather.MainActivity;
import pl.mattiahit.androidweather.R;

public class ForecastLocationAdapter extends RecyclerView.Adapter<ForecastLocationAdapter.ViewHolder> {


    private JsonArray forecastList;
    private MainActivity mainActivity;

    public ForecastLocationAdapter(MainActivity mainActivity, JsonArray forecasts){
        this.mainActivity = mainActivity;
        this.forecastList = forecasts;
    }

    @Override
    public ForecastLocationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_weather_detail_adapter, parent, false);
        ForecastLocationAdapter.ViewHolder viewHolder = new ForecastLocationAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final JsonObject forecast = this.forecastList.get(position).getAsJsonObject();

        String iconName = forecast.getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();
        Picasso.get().load("http://openweathermap.org/img/w/" + iconName + ".png").into(viewHolder.location_weather_icon);
        String forecastDate = new DateTime(forecast.get("dt").getAsLong()*1000).toString();
        viewHolder.location_weather_city_name.setText(forecastDate);
        int currentTemp = forecast.getAsJsonObject("main").get("temp").getAsInt() - 273;
        viewHolder.location_weather_temperature.setText(viewHolder.temperature + ": " +currentTemp + "°C");
        int windSpeed = forecast.getAsJsonObject("wind").get("speed").getAsInt();
        viewHolder.location_weather_wind.setText(viewHolder.wind + ": " + windSpeed + "km/h");
        int cloudLevel = forecast.getAsJsonObject("clouds").get("all").getAsInt();
        viewHolder.location_weather_clouds.setText(viewHolder.clouds + ": " + cloudLevel + "%");
        double pressure = forecast.getAsJsonObject("main").get("pressure").getAsDouble();
        viewHolder.location_weather_pressure.setText(viewHolder.pressure + ": " + pressure + "hPa");

        viewHolder.manage_favourites_btn.setVisibility(View.GONE);
        viewHolder.goToWeatherDetailsBtn.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return this.forecastList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

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


        public ViewHolder(View v){
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
