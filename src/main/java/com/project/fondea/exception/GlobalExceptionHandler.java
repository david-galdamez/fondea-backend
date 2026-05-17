package com.project.fondea.exception;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ─── 404 ────────────────────────────────────────────────
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFound(EntityNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Not found", ex.getMessage());
    }

    // ─── 409 Conflictos ─────────────────────────────────────
    @ExceptionHandler(PledgeAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handlePledgeAlreadyExists(PledgeAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, "Pledge already exists", ex.getMessage());
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<Map<String, Object>> incorrectPassword(IncorrectPasswordException ex) {
        return buildResponse(HttpStatus.CONFLICT, "Incorrect password", ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> userAlreadyExists(UserAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, "User already exists", ex.getMessage());
    }

    @ExceptionHandler(DuplicatePaymentException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicatePayment(DuplicatePaymentException ex) {
        return buildResponse(HttpStatus.CONFLICT, "Duplicate payment", ex.getMessage());
    }

    @ExceptionHandler(FraudReportAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleFraudReportAlreadyExists(FraudReportAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, "Fraud report already submitted", ex.getMessage());
    }

    @ExceptionHandler(CampaignAlreadyReviewedException.class)
    public ResponseEntity<Map<String, Object>> handleCampaignAlreadyReviewed(CampaignAlreadyReviewedException ex) {
        return buildResponse(HttpStatus.CONFLICT, "Campaign already reviewed", ex.getMessage());
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleAlreadyExists(ResourceAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, "Already exists", ex.getMessage());
    }

    // ─── 422 Reglas de negocio ───────────────────────────────
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessRule(BusinessRuleException ex) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Business rule violation", ex.getMessage());
    }

    @ExceptionHandler(CampaignNotActiveException.class)
    public ResponseEntity<Map<String, Object>> handleCampaignNotActive(CampaignNotActiveException ex) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Campaign not active", ex.getMessage());
    }

    @ExceptionHandler(CampaignGoalNotReachedException.class)
    public ResponseEntity<Map<String, Object>> handleCampaignGoalNotReached(CampaignGoalNotReachedException ex) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Campaign goal not reached", ex.getMessage());
    }

    @ExceptionHandler(RewardOutOfStockException.class)
    public ResponseEntity<Map<String, Object>> handleRewardOutOfStock(RewardOutOfStockException ex) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Reward out of stock", ex.getMessage());
    }

    @ExceptionHandler(WithdrawalLimitExceededException.class)
    public ResponseEntity<Map<String, Object>> handleWithdrawalLimitExceeded(WithdrawalLimitExceededException ex) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Withdrawal limit exceeded", ex.getMessage());
    }

    // ─── 403 ────────────────────────────────────────────────
    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedAction(UnauthorizedActionException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "Forbidden", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation failed");
        body.put("messages", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", "An unexpected error ocurred: " + ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status,
                                                              String error,
                                                              String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
