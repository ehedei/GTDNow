package es.iespuertodelacruz.dam.gtdnow.model.dao;

import java.util.Date;

import es.iespuertodelacruz.dam.gtdnow.model.entity.Group;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Note;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Place;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Project;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Task;
import io.realm.Realm;

public class TaskDao {
    private Realm realm;
    private Task task;

    public TaskDao(Task task) {
        this.realm = Realm.getDefaultInstance();
        this.task = task;
    }

    public void createOrUpdateTask() {
        realm.beginTransaction();
        realm.insertOrUpdate(task);
        realm.commitTransaction();
    }

    public void setName(String name) {
        realm.beginTransaction();
        task.setName(name);
        realm.commitTransaction();
    }

    public void setCompleted(boolean completed) {
        realm.beginTransaction();
        task.setCompleted(completed);
        realm.commitTransaction();
    }

    public void setEndTime(Date endTime) {
        realm.beginTransaction();
        task.setEndTime(endTime);
        realm.commitTransaction();
    }

    public void setPlace(Place place) {
        realm.beginTransaction();
        task.setPlace(place);
        realm.commitTransaction();
    }

    public void setProject(Project project) {
        realm.beginTransaction();
        task.setProject(project);
        realm.commitTransaction();
    }

    public void addGroup(Group group) {
        realm.beginTransaction();
        task.getGroups().add(group);
        realm.commitTransaction();
    }

    public void removeGroup(Group group) {
        realm.beginTransaction();
        task.getGroups().remove(group);
        realm.commitTransaction();
    }

    public void addNote(Note note) {
        realm.beginTransaction();
        task.getNotes().add(note);
        realm.commitTransaction();
    }


    public void deleteNote(Note note) {
        realm.beginTransaction();
        task.getNotes().first(note).deleteFromRealm();
        realm.commitTransaction();
    }

    public void addReminder(String reminder) {
        realm.beginTransaction();
        task.setReminder(reminder);
        realm.commitTransaction();
    }

    public void deleteReminder() {
        realm.beginTransaction();
        task.setReminder(null);
        //task.getReminder().deleteFromRealm();
        realm.commitTransaction();
    }

    public void closeRealm() {
        realm.close();
    }

}
