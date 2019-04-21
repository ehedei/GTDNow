package es.iespuertodelacruz.dam.gtdnow.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import es.iespuertodelacruz.dam.gtdnow.model.utility.DatabaseHelper;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = DatabaseHelper.TABLE_GROUP_TASK,
        primaryKeys = {DatabaseHelper.GROUP_TASK_GROUP_ID, DatabaseHelper.GROUP_TASK_TASK_ID},
        foreignKeys = {
                @ForeignKey(entity = Group.class,
                    parentColumns = DatabaseHelper.GROUP_ID,
                    childColumns = DatabaseHelper.GROUP_TASK_GROUP_ID,
                    onUpdate = CASCADE,
                    onDelete = CASCADE),
                @ForeignKey(entity = Task.class,
                    parentColumns = DatabaseHelper.TASK_ID,
                    childColumns = DatabaseHelper.GROUP_TASK_TASK_ID,
                    onUpdate = CASCADE,
                    onDelete = CASCADE)
        },
        indices = {@Index(DatabaseHelper.GROUP_TASK_GROUP_ID), @Index(DatabaseHelper.GROUP_TASK_TASK_ID)})
public class GroupTask {
    @ColumnInfo(name = DatabaseHelper.GROUP_TASK_GROUP_ID)
    @NonNull
    private String groupId;

    @ColumnInfo(name = DatabaseHelper.GROUP_TASK_TASK_ID)
    @NonNull
    private String taskId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
