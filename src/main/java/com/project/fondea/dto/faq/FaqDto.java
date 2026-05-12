package com.project.fondea.dto.faq;

import java.util.UUID;

public record FaqDto(
        UUID id,
        String question,
        String answer
) {
}
