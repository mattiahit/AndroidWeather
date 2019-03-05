package pl.mattiahit.androidweather.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mattiahit.androidweather.R;

public class ExpandableForecastAdapter extends BaseExpandableListAdapter {

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

    private Context context;
    private List<String> listForecastGroup;
    private HashMap<String, JsonArray> listForecastChild;

    public ExpandableForecastAdapter(Context context, List<String> listDataGroup,
                                     HashMap<String, JsonArray> listChildData) {
        this.context = context;
        this.listForecastGroup = listDataGroup;
        this.listForecastChild = listChildData;
    }

    @Override
    public int getGroupCount() {
        return this.listForecastGroup.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this.listForecastChild.get(this.listForecastGroup.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return this.listForecastGroup.get(i);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listForecastChild.get(this.listForecastGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_forecast_group, null);
        }

        TextView textViewGroup = convertView
                .findViewById(R.id.textViewGroup);
        textViewGroup.setTypeface(null, Typeface.BOLD);
        textViewGroup.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int group, int child, boolean b, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.location_weather_detail_adapter, null);
        ButterKnife.bind(this, view);

        JsonObject weatherObject = (JsonObject)this.getChild(group, child);

        String iconName = weatherObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();
        Picasso.get().load("http://openweathermap.org/img/w/" + iconName + ".png").into(this.location_weather_icon);
        this.location_weather_city_name.setText(weatherObject.get("hour").getAsString());
        int currentTemp = weatherObject.getAsJsonObject("main").get("temp").getAsInt() - 273;
        this.location_weather_temperature.setText(temperature + ": " +currentTemp + "°C");
        int windSpeed = weatherObject.getAsJsonObject("wind").get("speed").getAsInt();
        this.location_weather_wind.setText(wind + ": " + windSpeed + "km/h");
        int cloudLevel = weatherObject.getAsJsonObject("clouds").get("all").getAsInt();
        this.location_weather_clouds.setText(clouds + ": " + cloudLevel + "%");
        double pressure = weatherObject.getAsJsonObject("main").get("pressure").getAsDouble();
        this.location_weather_pressure.setText(pressure + ": " + pressure + "hPa");

        this.manage_favourites_btn.setVisibility(View.GONE);
        this.goToWeatherDetailsBtn.setVisibility(View.GONE);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
