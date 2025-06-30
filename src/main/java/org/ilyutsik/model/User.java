package org.ilyutsik.model;

import jakarta.persistence.*;
import lombok.Data;
import org.ilyutsik.util.PasswordUtil;


@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @PrePersist
    public void hashPassword() {
        if (password != null) {
            password = PasswordUtil.hashPassword(password);
        }
    }

}
