package org.ilyutsik.repository;

import jakarta.transaction.Transactional;
import org.ilyutsik.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByLogin(String login);

    Optional<User> findUserById(Long id);

    @Transactional
    void deleteByLogin(String login);
}
