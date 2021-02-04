package com.sdl.lt.gateway.commons.db.repository;

public interface SequenceRepository extends AbstractRepository {
    long getNext(String name);
}
