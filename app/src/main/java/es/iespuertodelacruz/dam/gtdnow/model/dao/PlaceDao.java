package es.iespuertodelacruz.dam.gtdnow.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import es.iespuertodelacruz.dam.gtdnow.model.entity.Place;
import es.iespuertodelacruz.dam.gtdnow.model.utility.DatabaseHelper;

@Dao
public interface PlaceDao {

    @Insert
    public void insertPlace(Place place);

    @Insert
    public void insertPlace(Place... places);

    @Insert
    public void insertPlace(List<Place> places);

    @Update
    public void updatePlace(Place place);

    @Update
    public void updatePlace(Place...places);

    @Update
    public void updatePlace(List<Place> places);

    @Delete
    public void deletePlace(Place place);

    @Delete
    public void deletePlace(Place... place);

    @Delete
    public void deletePlace(List<Place> places);

    @Query("SELECT * FROM " + DatabaseHelper.TABLE_PLACE)
    public List<Place> getPlaces();
}
