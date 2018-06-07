package pl.mattiahit.androidweather.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import pl.mattiahit.androidweather.models.FavouriteLocation;

@Dao
public interface FavouriteLocationDao {

    @Query("SELECT * FROM FavouriteLocation")
    List<FavouriteLocation> getAllFavouriteLocations();

    @Query("SELECT EXISTS(SELECT 1 FROM FavouriteLocation WHERE location_name LIKE :name)")
    int isLocationExist(String name);

    @Insert
    void instertFavouriteLocation(FavouriteLocation... favouriteLocations);

    @Delete
    void deleteFavouriteLocation(FavouriteLocation favouriteLocation);
}
