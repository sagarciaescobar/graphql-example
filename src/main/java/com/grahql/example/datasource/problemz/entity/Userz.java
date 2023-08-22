package com.grahql.example.datasource.problemz.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "userz")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Userz {
    @Id
    private UUID id;
    private String username;
    private String email;
    private String hashedPassword;
    private URL avatar;

    @CreationTimestamp
    private LocalDateTime creationTimestamp;

    private String displayName;
    private boolean active;
}
