package com.dai.dai.dto.user;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class UserDto {

    private int id;
    private UUID uuid;
    private String email;
    private String nombre;
    private String apellido;
    private String nickname;
    private String imagenPerfil;
}
