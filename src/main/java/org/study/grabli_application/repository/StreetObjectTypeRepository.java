package org.study.grabli_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.study.grabli_application.entity.StreetObjectType;

public interface StreetObjectTypeRepository extends JpaRepository<StreetObjectType, Long> {
}
