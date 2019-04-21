package es.iespuertodelacruz.dam.gtdnow.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import es.iespuertodelacruz.dam.gtdnow.model.entity.Group;

@Dao
public interface GroupDao {

    @Insert
    public void insertGroup(Group group);

    @Insert
    public void insertGroup(Group... groups);

    @Update
    public void updateGroup(Group group);

    @Update
    public void updateGroup(Group...groups);

    @Delete
    public void deleteGroup(Group group);

    @Delete
    public void deleteGroup(Group... group);
}
