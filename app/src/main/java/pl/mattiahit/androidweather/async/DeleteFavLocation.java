package pl.mattiahit.androidweather.async;

import android.os.AsyncTask;

import pl.mattiahit.androidweather.models.FavouriteLocation;
import pl.mattiahit.androidweather.utils.AppDatabase;

public class DeleteFavLocation extends AsyncTask<String, String, Integer> {

    private DeleteFavLocation.AsyncTaskListener listener;
    private AppDatabase appDatabase;
    private FavouriteLocation favouriteLocation;

    public DeleteFavLocation(AppDatabase appDatabase, FavouriteLocation favouriteLocation){
        this.appDatabase = appDatabase;
        this.favouriteLocation = favouriteLocation;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        this.appDatabase.favouriteLocationDao().deleteFavouriteLocation(favouriteLocation);
        return null;
    }

    @Override
    protected void onPostExecute(Integer value) {
        super.onPostExecute(value);
        if (listener != null) {
            listener.onAsyncTaskFinished(value);
        }
    }

    public void setListener(DeleteFavLocation.AsyncTaskListener listener) {
        this.listener = listener;
    }

    public interface AsyncTaskListener {
        void onAsyncTaskFinished(Integer value);
    }
}