package org.ilyutsik.repository;

import org.ilyutsik.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> getById(UUID id);

    void deleteById(UUID id);

    Optional<Session> findByUserId(Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Session s WHERE s.expiresAt < CURRENT_TIMESTAMP")
    int deleteExpiredSessions();
}
