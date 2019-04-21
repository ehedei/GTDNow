package es.iespuertodelacruz.dam.gtdnow.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;
import java.util.Date;
import java.util.UUID;

import es.iespuertodelacruz.dam.gtdnow.model.utility.DatabaseHelper;
import es.iespuertodelacruz.dam.gtdnow.model.utility.DateConverter;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static android.arch.persistence.room.ForeignKey.SET_NULL;


// TODO M:N
@Entity(tableName = DatabaseHelper.TABLE_TASK,
        foreignKeys = {
                @ForeignKey(
                    entity = Project.class,
                    parentColumns = DatabaseHelper.PROJECT_ID,
                    childColumns = DatabaseHelper.TASK_PROJECT_ID,
                    onUpdate = CASCADE,
                    onDelete = SET_NULL),
                @ForeignKey(
                    entity = Place.class,
                    parentColumns = DatabaseHelper.PLACE_ID,
                    childColumns = DatabaseHelper.TASK_PLACE_ID,
                    onDelete = SET_NULL,
                    onUpdate = CASCADE)
                },
        indices = {@Index(DatabaseHelper.TASK_PROJECT_ID), @Index(DatabaseHelper.TASK_PLACE_ID)})
public class Task {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DatabaseHelper.TASK_ID)
    private String taskId;

    private String name;

    private boolean isCompleted;

    @TypeConverters({DateConverter.class})
    private Date endTime;

    @ColumnInfo(name = DatabaseHelper.TASK_PROJECT_ID)
    private String projectId;

    @ColumnInfo(name = DatabaseHelper.TASK_PLACE_ID)
    private String placeId;



    public Task() {
        taskId = UUID.randomUUID().toString();
    }

    @NonNull
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(@NonNull String taskId) {
        this.taskId = taskId;
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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
