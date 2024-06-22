package org.study.grabli_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.study.grabli_application.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);
}
