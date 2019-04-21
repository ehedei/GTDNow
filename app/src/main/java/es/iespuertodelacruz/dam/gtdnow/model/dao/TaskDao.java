package es.iespuertodelacruz.dam.gtdnow.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import es.iespuertodelacruz.dam.gtdnow.model.entity.Task;

@Dao
public interface TaskDao {

    @Insert
    public void insertTask(Task task);

    @Insert
    public void insertTask(Task... tasks);

    @Update
    public void updateTask(Task task);

    @Update
    public void updateTask(Task...tasks);

    @Delete
    public void deleteTask(Task task);

    @Delete
    public void deleteTask(Task... task);
}
