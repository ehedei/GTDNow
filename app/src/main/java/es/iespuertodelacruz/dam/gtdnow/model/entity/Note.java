package es.iespuertodelacruz.dam.gtdnow.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

import es.iespuertodelacruz.dam.gtdnow.model.utility.DatabaseHelper;

import static android.arch.persistence.room.ForeignKey.CASCADE;



@Entity (tableName = DatabaseHelper.TABLE_NOTE,
        foreignKeys = {
                @ForeignKey(
                        entity = Task.class,
                        parentColumns = DatabaseHelper.TASK_ID,
                        childColumns = DatabaseHelper.NOTE_TASK_ID,
                        onUpdate = CASCADE,
                        onDelete = CASCADE)
        },
        indices = {@Index(DatabaseHelper.NOTE_TASK_ID)})


public class Note {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DatabaseHelper.NOTE_ID)
    private String noteId;

    private String name;

    @ColumnInfo(name = DatabaseHelper.NOTE_TASK_ID)
    private String taskId;

    private boolean isCompleted;


    public Note() {
        noteId = UUID.randomUUID().toString();
    }

    @NonNull
    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(@NonNull String noteId) {
        this.noteId = noteId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
