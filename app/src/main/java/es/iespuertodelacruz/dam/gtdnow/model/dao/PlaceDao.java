package es.iespuertodelacruz.dam.gtdnow.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import es.iespuertodelacruz.dam.gtdnow.model.entity.Place;
@Dao
public interface PlaceDao {

    @Insert
    public void insertPlace(Place place);

    @Insert
    public void insertPlace(Place... places);

    @Update
    public void updatePlace(Place place);

    @Update
    public void updatePlace(Place...places);

    @Delete
    public void deletePlace(Place place);

    @Delete
    public void deletePlace(Place... place);
}
