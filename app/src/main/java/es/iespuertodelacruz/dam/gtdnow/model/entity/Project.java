package es.iespuertodelacruz.dam.gtdnow.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;
import java.util.Date;
import java.util.UUID;

import es.iespuertodelacruz.dam.gtdnow.model.utility.DatabaseHelper;
import es.iespuertodelacruz.dam.gtdnow.model.utility.DateConverter;

@Entity(tableName = DatabaseHelper.TABLE_PROJECT)
public class Project {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DatabaseHelper.PROJECT_ID)
    private String projectId;

    private String name;

    private boolean isCompleted;

    @TypeConverters({DateConverter.class})
    private Date endTime;

    public Project() {
        projectId = UUID.randomUUID().toString();
    }

    @NonNull
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(@NonNull String projectId) {
        this.projectId = projectId;
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
}
