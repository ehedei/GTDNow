package es.iespuertodelacruz.dam.gtdnow.model.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import es.iespuertodelacruz.dam.gtdnow.model.dao.GroupDao;
import es.iespuertodelacruz.dam.gtdnow.model.dao.GroupTaskDao;
import es.iespuertodelacruz.dam.gtdnow.model.dao.NoteDao;
import es.iespuertodelacruz.dam.gtdnow.model.dao.PlaceDao;
import es.iespuertodelacruz.dam.gtdnow.model.dao.ProjectDao;
import es.iespuertodelacruz.dam.gtdnow.model.dao.TaskDao;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Group;
import es.iespuertodelacruz.dam.gtdnow.model.entity.GroupTask;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Note;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Place;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Project;
import es.iespuertodelacruz.dam.gtdnow.model.entity.Task;
import es.iespuertodelacruz.dam.gtdnow.model.utility.DatabaseHelper;

// TODO cambiar el exportSchema a True cuando la DB esté finiquitada
@Database(entities={Place.class, Project.class, Task.class, Group.class, Note.class, GroupTask.class}, version=1, exportSchema = false)
public abstract class GTDNowDb extends RoomDatabase {
    private static GTDNowDb db;

    public abstract ProjectDao projectDao();
    public abstract TaskDao taskDao();
    public abstract GroupDao groupDao();
    public abstract NoteDao noteDao();
    public abstract PlaceDao placeDao();
    public abstract GroupTaskDao groupTaskDao();


    public synchronized static GTDNowDb getDb(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context.getApplicationContext(),
                    GTDNowDb.class,
                    DatabaseHelper.DATABASE_NAME)
                    .build();
        }
        return db;
    }


}

