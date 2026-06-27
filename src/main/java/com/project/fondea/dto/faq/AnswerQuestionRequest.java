package com.project.fondea.dto.faq;

import jakarta.validation.constraints.NotBlank;

public record AnswerQuestionRequest(
        @NotBlank(message = "La respuesta es requerida")
        String answer
) {
}
