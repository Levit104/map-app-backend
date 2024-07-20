package org.study.grabli_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.study.grabli_application.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
