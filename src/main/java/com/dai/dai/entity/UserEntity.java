package com.dai.dai.entity;



import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "usuarios")
public class UserEntity {

    @Id
    private Integer id;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "email")
    private String email;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "imagen_perfil")
    private String imagenPerfil;

}
