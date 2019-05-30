package es.iespuertodelacruz.dam.gtdnow.model.entity;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Task extends RealmObject implements FinalizableEntity, Cloneable {

    @PrimaryKey
    private String taskId;

    private String name;

    private boolean isCompleted;

    private Date endTime;

    private Date reminder;

    private Place place;

    private Project project;

    private RealmList<Group> groups;

    private RealmList<Note> notes;

    public Task() {
        taskId = UUID.randomUUID().toString();
        groups = new RealmList<>();
        notes = new RealmList<>();
    }

    public Task(String name) {
        this();
        this.name = name;
    }

    private void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
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

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public RealmList<Group> getGroups() {
        return groups;
    }

    public void setGroups(RealmList<Group> groups) {
        this.groups = groups;
    }

    public RealmList<Note> getNotes() {
        return notes;
    }

    public void setNotes(RealmList<Note> notes) {
        this.notes = notes;
    }

    public Date getReminder() {
        return reminder;
    }

    public void setReminder(Date reminder) {
        this.reminder = reminder;
    }

    public Task clone(){
        Task task = new Task();
        task.setTaskId(this.taskId);
        task.setName(this.name);
        task.setCompleted(this.isCompleted);
        task.setEndTime(this.endTime);
        task.setNotes(this.notes);
        task.setPlace(this.place);
        task.setReminder(this.reminder);
        task.setProject(this.project);
        task.setGroups(this.groups);

        return task;
    }
}
