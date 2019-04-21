package es.iespuertodelacruz.dam.gtdnow.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import es.iespuertodelacruz.dam.gtdnow.model.entity.Project;

@Dao
public interface ProjectDao {

    @Insert
    public void insertProject(Project project);

    @Insert
    public void insertProject(Project... projects);

    @Update
    public void updateProject(Project project);

    @Update
    public void updateProject(Project...projects);

    @Delete
    public void deleteProject(Project project);

    @Delete
    public void deleteProject(Project... project);

}
