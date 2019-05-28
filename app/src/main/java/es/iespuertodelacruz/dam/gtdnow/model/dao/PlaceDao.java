package es.iespuertodelacruz.dam.gtdnow.model.dao;

import es.iespuertodelacruz.dam.gtdnow.model.entity.Place;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class PlaceDao {
    private Realm realm;

    public PlaceDao() {
        this.realm = Realm.getDefaultInstance();
    }

    public void createOrUpdatePlace(Place place) {
        realm.beginTransaction();
        realm.insertOrUpdate(place);
        realm.commitTransaction();
    }

    public void createOrEditPlace(String name, Place place) {
        realm.beginTransaction();
        if (place == null) {
            place = new Place();
        }
        place.setName(name);
        realm.copyToRealmOrUpdate(place);
        realm.commitTransaction();
    }


    public void setName(Place place, String name) {
        realm.beginTransaction();
        place.setName(name);
        realm.commitTransaction();
    }

    public RealmResults<Place> getPlaces() {
        return realm.where(Place.class).sort("name", Sort.ASCENDING).findAll();
    }

    public Place getPlaceById(String placeId) {
        return realm.where(Place.class).equalTo("placeId", placeId).findFirst();
    }

    public void deletePlace(Place place) {
        realm.beginTransaction();
        place.deleteFromRealm();
        realm.commitTransaction();
    }

    public void closeRealm() {
        realm.close();
    }


}
