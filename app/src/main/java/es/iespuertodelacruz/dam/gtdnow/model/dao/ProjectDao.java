package es.iespuertodelacruz.dam.gtdnow.model.dao;

import es.iespuertodelacruz.dam.gtdnow.model.entity.Project;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class ProjectDao {
    private Realm realm;

    public ProjectDao() {
        this.realm = Realm.getDefaultInstance();
    }

    public void createOrUpdateProject(Project project) {
        realm.beginTransaction();
        realm.insertOrUpdate(project);
        realm.commitTransaction();
    }

    public void setName(Project project, String name) {
        realm.beginTransaction();
        project.setName(name);
        realm.commitTransaction();
    }

    public void setCompleted(Project project, boolean completed) {
        realm.beginTransaction();
        project.setCompleted(completed);
        realm.commitTransaction();
    }

    public RealmResults<Project> getProjects() {
        String[] fieldNames = {"isCompleted", "name"};
        Sort[] sort = {Sort.ASCENDING, Sort.ASCENDING};
        return realm.where(Project.class).sort(fieldNames, sort).findAll();
    }

    public RealmResults<Project> getProjectsNotId(String projectId) {
        String[] fieldNames = {"isCompleted", "name"};
        Sort[] sort = {Sort.ASCENDING, Sort.ASCENDING};
        return realm.where(Project.class).not().equalTo("projectId", projectId).sort(fieldNames, sort).findAll();
    }

    public Project getProjectById(String projectId) {
        return realm.where(Project.class).equalTo("projectId", projectId).findFirst();
    }

    public void deleteProject(Project project) {
        realm.beginTransaction();
        project.deleteFromRealm();
        realm.commitTransaction();
    }

    public void closeRealm() {
        realm.close();
    }

}