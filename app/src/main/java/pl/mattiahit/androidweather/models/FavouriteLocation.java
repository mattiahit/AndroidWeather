package pl.mattiahit.androidweather.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class FavouriteLocation {

    @PrimaryKey
    private int id;

    @ColumnInfo(name="location_name")
    private String locationName;

    @ColumnInfo(name="location_lat")
    private double locationLat;

    @ColumnInfo(name="location_lon")
    private double locationLon;
}
