package com.project.fondea.dto.faq;

import java.time.LocalDateTime;
import java.util.UUID;

public record FaqManageDto(
        UUID id,
        String askedBy,
        String question,
        String answer,
        LocalDateTime askedAt,
        boolean answered
) {}