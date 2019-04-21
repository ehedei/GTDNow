package es.iespuertodelacruz.dam.gtdnow.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import es.iespuertodelacruz.dam.gtdnow.model.entity.Note;

@Dao
public interface NoteDao {

    @Insert
    public void insertNote(Note note);

    @Insert
    public void insertNote(Note... notes);

    @Update
    public void updateNote(Note note);

    @Update
    public void updateNote(Note...notes);

    @Delete
    public void deleteNote(Note note);

    @Delete
    public void deleteNote(Note... note);
    
}
