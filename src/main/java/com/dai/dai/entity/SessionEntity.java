package com.dai.dai.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
@Table(name = "sessions")
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "movieplay_token")
    private String token;

    @Column(name = "refresh_token")
    private String refreshToken;
}
