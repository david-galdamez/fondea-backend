package com.project.fondea.dto.faq;

import jakarta.validation.constraints.NotBlank;

public record AskQuestionRequest(
        @NotBlank(message = "La preguntas es requerida")
        String question
) {
}
