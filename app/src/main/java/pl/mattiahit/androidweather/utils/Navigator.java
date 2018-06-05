package pl.mattiahit.androidweather.utils;

import android.support.v4.app.Fragment;

import pl.mattiahit.androidweather.MainActivity;
import pl.mattiahit.androidweather.R;
import pl.mattiahit.androidweather.fragments.FavouriteListFragment;
import pl.mattiahit.androidweather.fragments.HomeFragment;
import pl.mattiahit.androidweather.fragments.LocationWeatherFragment;

public class Navigator {

    private MainActivity mainActivity;

    public Navigator(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void goToFavouriteList(){
        FavouriteListFragment favouriteListFragment = new FavouriteListFragment();
        this.forwardToFragment(favouriteListFragment);
    }
    public void goToHome(){
        HomeFragment homeFragment = new HomeFragment();
        this.forwardToFragment(homeFragment);
    }

    public void goToLocationWeather(){
        LocationWeatherFragment locationWeatherFragment = new LocationWeatherFragment();
        this.forwardToFragment(locationWeatherFragment);
    }

    private void forwardToFragment(Fragment target){
        this.mainActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, target)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }
}
