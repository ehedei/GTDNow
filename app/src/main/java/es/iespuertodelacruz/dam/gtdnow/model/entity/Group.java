package es.iespuertodelacruz.dam.gtdnow.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

import es.iespuertodelacruz.dam.gtdnow.model.utility.DatabaseHelper;

// TODO N:M y fk

@Entity(tableName = DatabaseHelper.TABLE_GROUP)
public class Group {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DatabaseHelper.GROUP_ID)
    private String groupId;

    private String name;

    public Group() {
        groupId = UUID.randomUUID().toString();
    }

    @NonNull
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(@NonNull String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
