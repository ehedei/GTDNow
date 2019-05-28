package es.iespuertodelacruz.dam.gtdnow.model.dao;

import es.iespuertodelacruz.dam.gtdnow.model.entity.Note;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class NoteDao {
    private Realm realm;

    public NoteDao() {
        this.realm = Realm.getDefaultInstance();
    }

    public RealmResults<Note> getNotesByTask(String taskId) {
        String [] fields = new String[] {"isCompleted", "name"};
        Sort[] sort = new Sort[] {Sort.ASCENDING, Sort.ASCENDING};

        return realm.where(Note.class).equalTo("task.taskId", taskId).sort(fields, sort).findAll();
    }

    public void deleteNoteFromRealm (Note note) {
        realm.beginTransaction();
        note.deleteFromRealm();
        realm.commitTransaction();
    }

    public void setCompletedInRealm(Note note, boolean isCompleted) {
        realm.beginTransaction();
        note.setCompleted(isCompleted);
        realm.commitTransaction();
    }

    public void createOrEditNote(String name, Note note) {
        realm.beginTransaction();
        if (note == null) {
            note = new Note();
        }
        note.setName(name);
        realm.copyToRealmOrUpdate(note);
        realm.commitTransaction();
    }

}
