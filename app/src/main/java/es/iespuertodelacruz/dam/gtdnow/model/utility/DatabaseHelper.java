package es.iespuertodelacruz.dam.gtdnow.model.utility;

public class DatabaseHelper {
    public static final String DATABASE_NAME = "GTDNow";

    // Tables
    public static final String TABLE_TASK = "TASKS";
    public static final String TABLE_NOTE = "NOTES";
    public static final String TABLE_GROUP = "GROUPS";
    public static final String TABLE_PROJECT = "PROJECTS";
    public static final String TABLE_PLACE = "PLACES";
    public static final String TABLE_GROUP_TASK = "GROUPS_TASKS";

    // Columns

    // Notes
    public static final String NOTE_ID = "note_pk";
    public static final String NOTE_TASK_ID = "task_fk";
    public static final String NOTE_IS_COMPLETED = "isCompleted";


    // Places
    public static final String PLACE_ID = "place_pk";

    // Groups
    public static final String GROUP_ID = "group_pk";

    // Tasks
    public static final String TASK_ID = "task_pk";
    public static final String TASK_PLACE_ID = "place_fk";
    public static final String TASK_PROJECT_ID = "project_fk";

    // Projects
    public static final String PROJECT_ID = "project_pk";

    // Groups_Tasks
    public static final String GROUP_TASK_GROUP_ID = "group_pk";
    public static final String GROUP_TASK_TASK_ID = "task_pk";
}
