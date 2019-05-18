package es.iespuertodelacruz.dam.gtdnow.model.dao;

import java.util.Date;

import es.iespuertodelacruz.dam.gtdnow.model.entity.Group;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class GroupDao {
    private Realm realm;

    public GroupDao() {
        this.realm = Realm.getDefaultInstance();
    }

    public void createOrUpdateGroup(Group group) {
        realm.beginTransaction();
        realm.insertOrUpdate(group);
        realm.commitTransaction();
    }

    public void setName(Group group, String name) {
        realm.beginTransaction();
        group.setName(name);
        realm.commitTransaction();
    }

    public RealmResults<Group> getGroups() {
        return realm.where(Group.class).sort("name", Sort.ASCENDING).findAll();
    }

    public Group getGroupById(String groupId) {
        return realm.where(Group.class).equalTo("groupId", groupId).findFirst();
    }

    public void deleteGroup(Group group) {
        realm.beginTransaction();
        group.deleteFromRealm();
        realm.commitTransaction();
    }

    public void closeRealm() {
        realm.close();
    }

}
