package pl.mattiahit.androidweather.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.mattiahit.androidweather.MainActivity;
import pl.mattiahit.androidweather.R;
import pl.mattiahit.androidweather.api.LocationWeatherApi;
import pl.mattiahit.androidweather.utils.Tools;

public class HomeFragment extends Fragment {

    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        this.mainActivity = (MainActivity) this.getActivity();
        super.onResume();
    }

    @OnClick(R.id.call_api)
    public void callApi(){
        Tools.showLog("Calling API");
        LocationWeatherApi locationWeatherApi = new LocationWeatherApi(this.mainActivity){
            public void parseResponse(JsonObject jsonObject){
                Tools.showLog(jsonObject.toString());
            }
        };
        locationWeatherApi.getCurrentViaLatLon(this.mainActivity.getLocation().getLatitude(), this.mainActivity.getLocation().getLongitude());
    }
}
