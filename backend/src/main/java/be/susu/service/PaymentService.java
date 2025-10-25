package be.susu.service;

import be.susu.db.entity.Payout;

import java.math.BigDecimal;
import java.util.UUID;


public interface PaymentService {
    void processUserPayment(UUID groupId, String keycloakUserId, BigDecimal amount);
    void processPayout(Payout payout);
}
