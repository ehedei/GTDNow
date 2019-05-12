package es.iespuertodelacruz.dam.gtdnow.model.entity;

import java.util.UUID;


import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


public class Group extends RealmObject implements NamedEntity {
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
}
