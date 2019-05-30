package es.iespuertodelacruz.dam.gtdnow.utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Date;

import es.iespuertodelacruz.dam.gtdnow.model.dao.TaskDao;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Task;
import io.realm.RealmResults;

public class TaskAlarmScheduler {
    private Context context;

    public TaskAlarmScheduler(Context context) {
        this.context = context;
    }

    public void scheduleAlarm(Task task) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent  = new Intent(context, ReminderReceiver.class);
        intent.putExtra(BundleHelper.TASK_NAME, task.getName());
        intent.putExtra(BundleHelper.TASK_ID, task.getTaskId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getTaskId().hashCode(), intent,  PendingIntent.FLAG_UPDATE_CURRENT);

        manager.set(AlarmManager.RTC_WAKEUP, task.getReminder().getTime(), pendingIntent);
    }


    public void cancelAlarm(Task task) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent  = new Intent(context, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getTaskId().hashCode(), intent,  PendingIntent.FLAG_UPDATE_CURRENT);

        if(pendingIntent != null)
            manager.cancel(pendingIntent);
    }

    public void cancelAllAlarms() {
        RealmResults<Task> list = new TaskDao().getTasksWithReminder();

        for (Task task: list) {
            cancelAlarm(task);
        }
    }


    public void scheduleAllAlarms() {
        RealmResults<Task> list = new TaskDao().getTasksWithReminder();

        for (Task task: list) {
            if(task.getReminder().after(new Date()))
                scheduleAlarm(task);
        }
    }

}
