package pl.mattiahit.androidweather.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Iterator;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mattiahit.androidweather.MainActivity;
import pl.mattiahit.androidweather.R;
import pl.mattiahit.androidweather.utils.WeatherView;

public class ForecastLocationAdapter extends RecyclerView.Adapter<ForecastLocationAdapter.ViewHolder> {


    private JsonArray forecastList;
    private MainActivity mainActivity;

    public ForecastLocationAdapter(MainActivity mainActivity, JsonArray forecasts){
        this.mainActivity = mainActivity;
        this.forecastList = forecasts;
    }

    @Override
    public ForecastLocationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_location_adapter, parent, false);
        ForecastLocationAdapter.ViewHolder viewHolder = new ForecastLocationAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final JsonObject forecast = this.forecastList.get(position).getAsJsonObject();

        String forecastDate = forecast.keySet().iterator().next();
        viewHolder.location_weather_date.setText(forecastDate);

        JsonArray forecastArray = forecast.getAsJsonArray(forecastDate);
        Iterator iterator = forecastArray.iterator();
        while(iterator.hasNext()){
            JsonElement jsonElement = (JsonElement) iterator.next();
            WeatherView weatherView = new WeatherView(this.mainActivity);
            weatherView.initWeatherView(jsonElement.getAsJsonObject());
            viewHolder.location_weather_hours.addView(weatherView);
        }
    }

    @Override
    public int getItemCount() {
        return this.forecastList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.location_weather_date)
        TextView location_weather_date;
        @BindView(R.id.location_weather_hours)
        LinearLayout location_weather_hours;


        public ViewHolder(View v){
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
