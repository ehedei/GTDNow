package es.iespuertodelacruz.dam.gtdnow.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

import es.iespuertodelacruz.dam.gtdnow.model.utility.DatabaseHelper;


@Entity(tableName = DatabaseHelper.TABLE_PLACE)
public class Place {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DatabaseHelper.PLACE_ID)
    private String placeId;

    private String name;

    private double longitude;

    private double latitude;


    public Place() {
        placeId = UUID.randomUUID().toString();
    }

    @NonNull
    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(@NonNull String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
