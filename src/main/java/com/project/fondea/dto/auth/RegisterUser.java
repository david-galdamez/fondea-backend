package com.project.fondea.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

public record RegisterUser (
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 3, max = 50, message = "El nombre tiene que ser de entre 3 a 50 caracteres")
    String name,

    @NotBlank(message = "El correo es requerido")
    @Email(message = "El correo tiene que tener un formato valido")
    String email,

    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 8, message = "La contraseña tiene que ser de minimo 8 caracteres")
    String password
) {}
