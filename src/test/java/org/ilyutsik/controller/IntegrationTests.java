package org.ilyutsik.controller;


import org.ilyutsik.model.User;
import org.ilyutsik.repository.LocationRepository;
import org.ilyutsik.repository.SessionRepository;
import org.ilyutsik.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDb() {
        sessionRepository.deleteAll();
        locationRepository.deleteAll();
        userRepository.deleteAll();
    }

    String login = "testUser";
    String password = "testPass";

    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:14")
                    .withDatabaseName("testdb")
                    .withUsername("testuser")
                    .withPassword("testpass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }
    
    @Nested
    @DisplayName("Registration tests")
    class RegistrationTest {
        
        @Test
        @DisplayName("Successful registration")
        void successfulRegistration() throws Exception {
            Optional<User> notFoundUser = userRepository.findUserByLogin(login);
            Assertions.assertTrue(notFoundUser.isEmpty());
            mockMvc.perform(post("/registration")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("login", login)
                            .param("password", password)
                            .param("repeatPassword", password))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/authorization"));

            Optional<User> user = userRepository.findUserByLogin(login);
            Assertions.assertTrue(user.isPresent());
            Assertions.assertEquals(login, user.get().getLogin());
        }

        @Test
        @DisplayName("Unsuccessful registration, login is used")
        void loginIsUsedRegistration() throws Exception {
            User user = userRepository.save(User.builder().login(login).password(password).build());

            mockMvc.perform(post("/registration")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("login", login)
                            .param("password", password)
                            .param("repeatPassword", password))
                    .andExpect(status().isConflict());
        }
        
        @Test
        @DisplayName("Unsuccessful registration, passwords not match")
        void passwordsNotMatchRegistration() throws Exception {
            mockMvc.perform(post("/registration")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("login", login)
                            .param("password", password)
                            .param("repeatPassword", password + "fail"))
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    @DisplayName("Authorization tests")
    class AuthenticationTest {

        @Test
        @DisplayName("Successful authorization")
        void successfulAuthorization() throws Exception {
            User user = userRepository.save(User.builder().login(login).password(password).build());
            mockMvc.perform(post("/authorization")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("login", login)
                            .param("password", password))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/home"))
                    .andExpect(cookie().exists("session-token"))
                    .andExpect(cookie().value("session-token", sessionRepository.findByUserId(user.getId()).get().getId().toString()));

        }

        @Test
        @DisplayName("Unsuccessful authorization, password is incorrect")
        void passwordIsIncorrectAuthorization() throws Exception {
            User user = userRepository.save(User.builder().login(login).password(password).build());
            mockMvc.perform(post("/authorization")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("login", login)
                            .param("password", password + "fail"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Unsuccessful authorization, user with login does not exist")
        void loginIsNotExistAuthorization() throws Exception {
            mockMvc.perform(post("/authorization")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("login", login)
                            .param("password", password))
                    .andExpect(status().isNotFound());
        }

    }

}
