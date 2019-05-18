package es.iespuertodelacruz.dam.gtdnow.model.entity;

import java.util.Date;

public interface FinalizableEntity extends NamedEntity {
    Date getEndTime();
    boolean isCompleted();
}
