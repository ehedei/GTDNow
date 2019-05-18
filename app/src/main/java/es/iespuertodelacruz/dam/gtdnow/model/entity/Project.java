package es.iespuertodelacruz.dam.gtdnow.model.entity;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Project extends RealmObject implements Cloneable, FinalizableEntity{
    @PrimaryKey
    private String projectId;

    private String name;

    private boolean isCompleted;

    private Date endTime;

    private RealmList<Task> tasks;

    public Project() {
        projectId = UUID.randomUUID().toString();
    }

    public Project(String name) {
        this.name = name;
    }

    public String getProjectId() {
        return projectId;
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

    public RealmList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(RealmList<Task> tasks) {
        this.tasks = tasks;
    }

    public Project clone() {
        Project p = new Project();
        p.projectId = this.getProjectId();
        p.name = this.getName();
        p.tasks = this.getTasks();
        p.endTime = this.getEndTime();
        p.isCompleted = this.isCompleted();
        return p;
    }
}
