package org.study.grabli_application.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.study.grabli_application.entity.StreetObject;

public interface StreetObjectRepository extends JpaRepository<StreetObject, Long> {

    List<StreetObject> findByIdCreator(Long id);
}
