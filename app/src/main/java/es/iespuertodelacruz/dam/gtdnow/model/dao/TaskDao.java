package es.iespuertodelacruz.dam.gtdnow.model.dao;

import java.util.Date;

import es.iespuertodelacruz.dam.gtdnow.model.entity.Group;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Note;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Place;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Project;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Task;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class TaskDao {
    private Realm realm;

    public TaskDao() {
        this.realm = Realm.getDefaultInstance();
    }

    public void createOrUpdateTask(Task task) {
        realm.beginTransaction();
        realm.insertOrUpdate(task);
        realm.commitTransaction();
    }

    public void deleteTask(Task task) {
        realm.beginTransaction();
        task.deleteFromRealm();
        realm.commitTransaction();
    }

    public void setName(Task task, String name) {
        realm.beginTransaction();
        task.setName(name);
        realm.commitTransaction();
    }

    public void setCompleted(Task task, boolean completed) {
        realm.beginTransaction();
        task.setCompleted(completed);
        realm.commitTransaction();
    }

    public void setEndTime(Task task, Date endTime) {
        realm.beginTransaction();
        task.setEndTime(endTime);
        realm.commitTransaction();
    }

    public void setPlace(Task task, Place place) {
        realm.beginTransaction();
        task.setPlace(place);
        realm.commitTransaction();
    }

    public void setProject(Task task, Project project) {
        realm.beginTransaction();
        task.setProject(project);
        realm.commitTransaction();
    }

    public void addGroup(Task task, Group group) {
        realm.beginTransaction();
        task.getGroups().add(group);
        realm.commitTransaction();
    }

    public void removeGroup(Task task, Group group) {
        realm.beginTransaction();
        task.getGroups().remove(group);
        realm.commitTransaction();
    }

    public void addNote(Task task, Note note) {
        realm.beginTransaction();
        task.getNotes().add(note);
        realm.commitTransaction();
    }


    public void deleteNote(Task task, Note note) {
        realm.beginTransaction();
        task.getNotes().first(note).deleteFromRealm();
        realm.commitTransaction();
    }

    public void addReminder(Task task, String reminder) {
        realm.beginTransaction();
        task.setReminder(reminder);
        realm.commitTransaction();
    }

    public void deleteReminder(Task task, String reminder) {
        realm.beginTransaction();
        task.setReminder(null);
        //task.getReminder().deleteFromRealm();
        realm.commitTransaction();

    }

    public Task getTaskById(String taskId) {
        return realm.where(Task.class).equalTo("taskId", taskId).findFirst();
    }

    public RealmResults<Task> getTasks() {
        String[] fieldNames = {"isCompleted", "name"};
        Sort[] sort = {Sort.ASCENDING, Sort.ASCENDING};
        return realm.where(Task.class).sort(fieldNames, sort).findAll();
    }

    public RealmResults<Task> getTasksByPlace(String placeId) {
        String[] fieldNames = {"isCompleted", "name"};
        Sort[] sort = {Sort.ASCENDING, Sort.ASCENDING};
        return realm.where(Task.class).equalTo("place.placeId", placeId).sort(fieldNames, sort).findAll();
    }

    public RealmResults<Task> getTasksNotInPlace(String placeId) {
        String[] fieldNames = {"isCompleted", "name"};
        Sort[] sort = {Sort.ASCENDING, Sort.ASCENDING};
        return realm.where(Task.class).not().equalTo("place.placeId", placeId).sort(fieldNames, sort).findAll();
    }

    public RealmResults<Task> getTasksNotInGroup(String groupId) {
        String[] fieldNames = {"isCompleted", "name"};
        Sort[] sort = {Sort.ASCENDING, Sort.ASCENDING};
        return realm.where(Task.class).not().equalTo("groups.groupId", groupId).sort(fieldNames, sort).findAll();
    }

    public RealmResults<Task> getTasksByGroup(String groupId) {
        String[] fieldNames = {"isCompleted", "name"};
        Sort[] sort = {Sort.ASCENDING, Sort.ASCENDING};
        return realm.where(Task.class).equalTo("groups.groupId", groupId).sort(fieldNames, sort).findAll();
    }

    public RealmResults<Task> getTasksByProject(String projectId) {
        String[] fieldNames = {"isCompleted", "name"};
        Sort[] sort = {Sort.ASCENDING, Sort.ASCENDING};
        return realm.where(Task.class).equalTo("project.projectId", projectId).sort(fieldNames, sort).findAll();
    }

    public RealmResults<Task> getTasksNotInProject(String projectId) {
        String[] fieldNames = {"isCompleted", "name"};
        Sort[] sort = {Sort.ASCENDING, Sort.ASCENDING};
        return realm.where(Task.class).isNull("project").sort(fieldNames, sort).findAll();
    }

    public void closeRealm() {
        realm.close();
    }

}