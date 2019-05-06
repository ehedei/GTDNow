package es.iespuertodelacruz.dam.gtdnow.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import es.iespuertodelacruz.dam.gtdnow.model.entity.Note;
import es.iespuertodelacruz.dam.gtdnow.model.utility.DatabaseHelper;

@Dao
public interface NoteDao {

    @Insert
    public void insertNote(Note note);

    @Insert
    public void insertNote(Note... notes);

    @Insert
    public void insertNote(List<Note> notes);

    @Update
    public void updateNote(Note note);

    @Update
    public void updateNote(Note...notes);

    @Delete
    public void deleteNote(Note note);

    @Delete
    public void deleteNote(Note... note);

    @Query("SELECT * FROM " + DatabaseHelper.TABLE_NOTE + " WHERE " + DatabaseHelper.NOTE_TASK_ID + " == :taskId")
    public List<Note> getNotes(String taskId);

    @Query("SELECT * FROM " + DatabaseHelper.TABLE_NOTE + " WHERE " + DatabaseHelper.NOTE_TASK_ID + " == :taskId AND " + DatabaseHelper.NOTE_IS_COMPLETED + " == :isCompleted")
    public List<Note> getNotes(String taskId, boolean isCompleted);

    @Query("SELECT * FROM " + DatabaseHelper.TABLE_NOTE + " WHERE " + DatabaseHelper.NOTE_IS_COMPLETED + " == :isCompleted")
    public List<Note> getNotes(boolean isCompleted);

    @Query("SELECT * FROM " + DatabaseHelper.TABLE_NOTE)
    public List<Note> getNotes();
}
