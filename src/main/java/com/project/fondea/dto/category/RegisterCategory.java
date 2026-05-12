package com.project.fondea.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterCategory {
    @NotBlank(message = "El nombre es requerido")
    private String name;
}
