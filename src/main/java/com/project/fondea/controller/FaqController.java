package com.project.fondea.controller;

import com.project.fondea.dto.faq.AnswerQuestionRequest;
import com.project.fondea.dto.faq.AskQuestionRequest;
import com.project.fondea.dto.faq.FaqDto;
import com.project.fondea.dto.faq.FaqManageDto;
import com.project.fondea.filter.AuthContext;
import com.project.fondea.service.CampaignFaqService;
import com.project.fondea.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/campaigns/{campaignId}/faqs")
@RequiredArgsConstructor
public class FaqController {

    private final CampaignFaqService faqService;
    private final AuthContext authContext;

    @PostMapping
    public ResponseEntity<ApiResponse<FaqDto>> ask(
            @PathVariable UUID campaignId,
            @Valid @RequestBody AskQuestionRequest answerRequest,
            HttpServletRequest request) {

        var sponsorId = authContext.getCurrentUserId();
        var faq = faqService.ask(sponsorId, campaignId, answerRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(faq, "Pregunta enviada exitosamente", request.getRequestURI()));
    }

    // Creador responde una pregunta
    @PutMapping("/{faqId}/answer")
    public ResponseEntity<ApiResponse<FaqDto>> answer(
            @PathVariable UUID campaignId,
            @PathVariable UUID faqId,
            @Valid @RequestBody AnswerQuestionRequest answerQuestionRequest,
            HttpServletRequest request) {

        var creatorId = authContext.getCurrentUserId();
        var faq = faqService.answer(creatorId, faqId, answerQuestionRequest);

        return ResponseEntity.ok(ApiResponse.ok(faq, "Pregunta respondida exitosamente", request.getRequestURI()));
    }

    // Público — solo las respondidas
    @GetMapping
    public ResponseEntity<ApiResponse<List<FaqDto>>> getPublic(
            @PathVariable UUID campaignId,
            HttpServletRequest request) {

        var faqs = faqService.getPublicFaqs(campaignId);

        return ResponseEntity.ok(ApiResponse.ok(faqs, "", request.getRequestURI()));
    }

    // Creador — todas con detalle
    @GetMapping("/manage")
    public ResponseEntity<ApiResponse<List<FaqManageDto>>> manage(
            @PathVariable UUID campaignId,
            HttpServletRequest request) {

        var creatorId = authContext.getCurrentUserId();
        var faqs = faqService.getManageFaqs(creatorId, campaignId);

        return ResponseEntity.ok(ApiResponse.ok(faqs, "", request.getRequestURI()));
    }

    // Creador elimina una pregunta
    @DeleteMapping("/{faqId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID campaignId,
            @PathVariable UUID faqId,
            HttpServletRequest request) {

        var creatorId = authContext.getCurrentUserId();
        faqService.delete(creatorId, faqId);

        return ResponseEntity.ok(ApiResponse.ok(null, "Pregunta eliminada exitosamente", request.getRequestURI()));
    }
}