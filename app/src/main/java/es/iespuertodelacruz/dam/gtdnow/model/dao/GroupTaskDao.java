package es.iespuertodelacruz.dam.gtdnow.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import es.iespuertodelacruz.dam.gtdnow.model.entity.Group;
import es.iespuertodelacruz.dam.gtdnow.model.entity.GroupTask;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Task;
import es.iespuertodelacruz.dam.gtdnow.model.utility.DatabaseHelper;

@Dao
public interface GroupTaskDao {

    @Insert
    public void insertGroupTask(GroupTask groupTask);

    @Query("SELECT " + DatabaseHelper.TABLE_GROUP + ".* FROM " +
            DatabaseHelper.TABLE_GROUP +
            " INNER JOIN " +
            DatabaseHelper.TABLE_GROUP_TASK +
            " ON " +
            DatabaseHelper.TABLE_GROUP + "." + DatabaseHelper.GROUP_ID +
            " == " +
            DatabaseHelper.TABLE_GROUP_TASK + "." + DatabaseHelper.GROUP_TASK_GROUP_ID +
            " WHERE " +
            DatabaseHelper.TABLE_GROUP_TASK + "." + DatabaseHelper.GROUP_TASK_TASK_ID + " == :taskId"
    )
    List<Group> getGroupsByTask(String taskId);


    @Query("SELECT " + DatabaseHelper.TABLE_TASK + ".* FROM " +
            DatabaseHelper.TABLE_TASK +
            " INNER JOIN " +
            DatabaseHelper.TABLE_GROUP_TASK +
            " ON " +
            DatabaseHelper.TABLE_TASK + "." + DatabaseHelper.TASK_ID +
            " == " +
            DatabaseHelper.TABLE_GROUP_TASK + "." + DatabaseHelper.GROUP_TASK_TASK_ID +
            " WHERE " +
            DatabaseHelper.TABLE_GROUP_TASK + "." + DatabaseHelper.GROUP_TASK_GROUP_ID + " == :groupId"
    )
    List<Task> getTasksbyGroup(String groupId);

}
