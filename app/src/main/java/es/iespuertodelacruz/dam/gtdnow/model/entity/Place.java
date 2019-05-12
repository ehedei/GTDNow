package es.iespuertodelacruz.dam.gtdnow.model.entity;


import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


public class Place extends RealmObject implements NamedEntity{
    @PrimaryKey
    private String placeId;

    private String name;

    private double longitude;

    private double latitude;

    private RealmList<Task> tasks;


    public Place() {
        placeId = UUID.randomUUID().toString();
    }

    public Place(String name) {
        this();
        this.setName(name);
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public RealmList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(RealmList<Task> tasks) {
        this.tasks = tasks;
    }
}
