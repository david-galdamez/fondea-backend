package com.project.fondea.dto.location;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterLocation {
    @NotBlank(message = "El nombre del pais es requerido")
    private String country;
    @NotBlank(message = "El nombre de la ciudad es requerido")
    private String city;
}
