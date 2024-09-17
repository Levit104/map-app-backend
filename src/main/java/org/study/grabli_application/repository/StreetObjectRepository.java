package org.study.grabli_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.study.grabli_application.entity.StreetObject;

import java.util.List;

public interface StreetObjectRepository extends JpaRepository<StreetObject, Long> {
    @Override
    @NonNull
    @Query("select s from StreetObject s left join fetch s.type")
    List<StreetObject> findAll();
}
