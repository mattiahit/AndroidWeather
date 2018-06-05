package pl.mattiahit.androidweather.utils;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import pl.mattiahit.androidweather.daos.FavouriteLocationDao;
import pl.mattiahit.androidweather.models.FavouriteLocation;

@Database(entities = {FavouriteLocation.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FavouriteLocationDao favouriteLocationDao();
}
