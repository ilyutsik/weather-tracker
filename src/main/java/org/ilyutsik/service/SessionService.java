package org.ilyutsik.service;

import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ilyutsik.exception.SessionExpiredException;
import org.ilyutsik.exception.SessionNotFoundException;
import org.ilyutsik.model.Session;
import org.ilyutsik.model.User;
import org.ilyutsik.repository.SessionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


@Service
@RequiredArgsConstructor
public class SessionService {

    private static final Integer SESSION_SECONDS_LIFETIME = 600_000;

    private final SessionRepository sessionRepository;

    @Transactional
    public Cookie startSession(User user) {
        UUID sessionToken = UUID.randomUUID();

        Session session = new Session();
        session.setId(sessionToken);
        session.setUser(user);
        session.setExpiresAt(LocalDateTime.now().plusSeconds(SESSION_SECONDS_LIFETIME));
        sessionRepository.save(session);
        Cookie cookie = new Cookie("session-token", sessionToken.toString());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(SESSION_SECONDS_LIFETIME);
        return cookie;
    }

    @Transactional
    public User getUser(String sessionToken) throws SessionNotFoundException, SessionExpiredException {
        Optional<Session> optionalSession = sessionRepository.getById(UUID.fromString(sessionToken));

        if (optionalSession.isEmpty()) {
            throw new SessionNotFoundException();
        }
        if (optionalSession.get().getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new SessionExpiredException();
        }
        return optionalSession.get().getUser();
    }

    @Transactional
    public Cookie delete(String sessionToken) {
        sessionRepository.deleteById(UUID.fromString(sessionToken));
        Cookie emptyCookie = new Cookie("session-token", sessionToken);
        emptyCookie.setPath("/");
        emptyCookie.setHttpOnly(true);
        emptyCookie.setMaxAge(0);
        return emptyCookie;
    }

}
