package pl.mattiahit.androidweather.async;

import android.os.AsyncTask;
import pl.mattiahit.androidweather.utils.AppDatabase;

public class CheckLocationExist extends AsyncTask<String, String, Boolean> {

    private CheckLocationExist.AsyncTaskListener listener;
    private AppDatabase appDatabase;
    private String name;

    public CheckLocationExist(AppDatabase appDatabase, String name){
        this.appDatabase = appDatabase;
        this.name = name;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        return this.appDatabase.favouriteLocationDao().isLocationExist(this.name) == 1;
    }

    @Override
    protected void onPostExecute(Boolean value) {
        super.onPostExecute(value);
        if (listener != null) {
            listener.onAsyncTaskFinished(value);
        }
    }

    public void setListener(CheckLocationExist.AsyncTaskListener listener) {
        this.listener = listener;
    }

    public interface AsyncTaskListener {
        void onAsyncTaskFinished(Boolean value);
    }
}
