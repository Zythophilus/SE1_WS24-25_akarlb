package org.hbrs.se1.ws24.exercises.uebung4.prototype.model;

import org.hbrs.se1.ws24.exercises.uebung4.prototype.exceptions.PersistenceException;

import java.util.List;

/**
 * Interface for defining basic methods for a persistence mechanism
 * Each concrete algorithm (i.e. strategy) must implement this method
 * This interface corresponds to the abstract strategy w.r.t. to the
 * Strategy Design Pattern (GoF).
 *
 */
public interface PersistenceStrategy<E, Z> {
    public void save(List<E> member, List<Z> actors) throws PersistenceException;

    public List<E> load() throws PersistenceException;

    public List<Z> loadActors() throws PersistenceException;

    public void clear(List<E> stories, List<Z> actors) throws PersistenceException;
}

