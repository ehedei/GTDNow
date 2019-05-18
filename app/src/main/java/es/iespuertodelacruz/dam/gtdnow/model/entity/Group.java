package es.iespuertodelacruz.dam.gtdnow.model.entity;

import java.util.UUID;


import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;



public class Group extends RealmObject implements NamedEntity, Cloneable {
    @PrimaryKey
    private String groupId;

    private String name;

    @LinkingObjects("groups")
    private final RealmResults<Task> tasks;

    public Group() {
        groupId = UUID.randomUUID().toString();
        tasks = null;
    }

    public Group(String name) {
        this();
        this.setName(name);
    }

    public Group(String name, RealmResults<Task> tasks) {
        RealmResults<Task> tasks1;
        tasks1 = tasks;
        this.tasks = tasks1;
        this.setName(name);
    }

    public String getGroupId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmResults<Task> getTasks() {
        return tasks;
    }

    public Group clone() {
        Group group = new Group(this.getName(), this.getTasks());
        group.groupId = this.getGroupId();
        return group;
    }
}
