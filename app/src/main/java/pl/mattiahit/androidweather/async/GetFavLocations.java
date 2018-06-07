package pl.mattiahit.androidweather.async;

import android.os.AsyncTask;

import java.util.List;

import pl.mattiahit.androidweather.models.FavouriteLocation;
import pl.mattiahit.androidweather.utils.AppDatabase;

public class GetFavLocations extends AsyncTask<String, String, List<FavouriteLocation>> {

    private GetFavLocations.AsyncTaskListener listener;
    private AppDatabase appDatabase;

    public GetFavLocations(AppDatabase appDatabase){
        this.appDatabase = appDatabase;
    }

    @Override
    protected List<FavouriteLocation> doInBackground(String... strings) {
        return this.appDatabase.favouriteLocationDao().getAllFavouriteLocations();
    }

    @Override
    protected void onPostExecute(List<FavouriteLocation> value) {
        super.onPostExecute(value);
        if (listener != null) {
            listener.onAsyncTaskFinished(value);
        }
    }

    public void setListener(GetFavLocations.AsyncTaskListener listener) {
        this.listener = listener;
    }

    public interface AsyncTaskListener {
        void onAsyncTaskFinished(List<FavouriteLocation> value);
    }
}
