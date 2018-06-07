package pl.mattiahit.androidweather;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mattiahit.androidweather.models.FavouriteLocation;
import pl.mattiahit.androidweather.rest.WeatherForCityRestTask;

public class FavouriteLocationAdapter extends RecyclerView.Adapter<FavouriteLocationAdapter.ViewHolder>{


    private ArrayList<FavouriteLocation> locationArrayList;
    private MainActivity context;

    public FavouriteLocationAdapter(MainActivity context, List<FavouriteLocation> locationArrayList){
        this.context = context;
        this.locationArrayList = new ArrayList<FavouriteLocation>(locationArrayList);
    }

    @Override
    public FavouriteLocationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_weather_detail_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FavouriteLocationAdapter.ViewHolder viewHolder, final int position) {

        FavouriteLocation favouriteLocation = this.locationArrayList.get(position);

        WeatherForCityRestTask weatherForCityRestTask = new WeatherForCityRestTask(favouriteLocation.getLocationName()) {
            @Override
            public void doOnResult(int code, JsonObject object) {
                if (code == 200) {
                    viewHolder.manage_favourites_btn.setVisibility(View.VISIBLE);
                    viewHolder.manage_favourites_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            doOnClick(position);
                        }
                    });
                    String iconName = object.getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();
                    Picasso.get().load("http://openweathermap.org/img/w/" + iconName + ".png").into(viewHolder.location_weather_icon);
                    viewHolder.location_weather_city_name.setText(object.get("name").getAsString());
                    int currentTemp = object.getAsJsonObject("main").get("temp").getAsInt() - 273;
                    viewHolder.location_weather_temperature.setText(viewHolder.temperature + ": " +currentTemp + "°C");
                    int windSpeed = object.getAsJsonObject("wind").get("speed").getAsInt();
                    viewHolder.location_weather_wind.setText(viewHolder.wind + ": " + windSpeed + "km/h");
                    int cloudLevel = object.getAsJsonObject("clouds").get("all").getAsInt();
                    viewHolder.location_weather_clouds.setText(viewHolder.clouds + ": " + cloudLevel + "%");
                    double pressure = object.getAsJsonObject("main").get("pressure").getAsDouble();
                    viewHolder.location_weather_pressure.setText(viewHolder.pressure + ": " + pressure + "hPa");
                }
            }
        };
        weatherForCityRestTask.getWeather();
    }

    @Override
    public int getItemCount() {
        return this.locationArrayList.size();
    }

    public void doOnClick(int position){

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
