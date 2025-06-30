package org.ilyutsik.repository;

import org.ilyutsik.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> getById(UUID id);

    void deleteById(UUID id);

}
