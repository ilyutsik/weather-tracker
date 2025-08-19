package org.ilyutsik.service;

import org.ilyutsik.repository.SessionRepository;
import org.springframework.stereotype.Service;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class SessionCleanupService {

    private final SessionRepository sessionRepository;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public SessionCleanupService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;

        scheduler.scheduleAtFixedRate(
                this::cleanupExpiredSessions,
                0,
                1,
                TimeUnit.DAYS
        );
    }

    public void cleanupExpiredSessions() {
        sessionRepository.deleteExpiredSessions();
    }
}
