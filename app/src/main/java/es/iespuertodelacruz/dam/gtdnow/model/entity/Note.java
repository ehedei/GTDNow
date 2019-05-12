package es.iespuertodelacruz.dam.gtdnow.model.entity;


import java.util.UUID;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Note extends RealmObject implements NamedEntity{

    @PrimaryKey
    private String noteId;

    private String name;

    private boolean isCompleted;

    @LinkingObjects("notes")
    private final RealmResults<Task> task;

    public Note() {
        noteId = UUID.randomUUID().toString();
        task = null;
    }

    public Note(String name) {
        this();
        this.setName(name);
    }


    public String getNoteId() {
        return noteId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public RealmResults<Task> getTask() {
        return task;
    }
}
