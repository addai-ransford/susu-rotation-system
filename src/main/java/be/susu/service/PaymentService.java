package be.susu.service;

import java.math.BigDecimal;
import java.util.UUID;


public interface PaymentService {
    void processUserPayment(UUID groupId, String keycloakUserId, BigDecimal amount);
    void processPayout(UUID groupId, UUID recipientMembershipId, BigDecimal amount);
}
