package be.susu.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import be.susu.db.entity.Membership;
import be.susu.db.entity.Payout;
import be.susu.service.PaymentService;

/**
 * Mock Stripe service for simulation. In a real version, this integrates with
 * Stripe Connect accounts.
 */
@Service
@Slf4j
public class FakeStripePaymentService implements PaymentService {

    @Override
    public void processUserPayment(UUID groupId, String keycloakUserId, BigDecimal amount) {
        // simulate charge success
        log.info("Fake charge: user={} group={} amount={}", keycloakUserId, groupId, amount);
    }

    @Override
    public void processPayout(UUID groupId, UUID recipientMembershipId, BigDecimal amount) {
        log.info("Fake payout: group={} recipient={} amount={}", groupId, recipientMembershipId, amount);
    }
}
