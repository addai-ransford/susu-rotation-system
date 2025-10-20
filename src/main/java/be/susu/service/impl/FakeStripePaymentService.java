package be.susu.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import be.susu.db.entity.Membership;
import be.susu.db.entity.Payout;
import be.susu.service.PaymentService;

/**
 * Mock Stripe service for simulation. In a real version, this integrates with
 * Stripe Connect accounts.
 */
@Service

public class FakeStripePaymentService implements PaymentService {

    // Simulate user payment via Stripe checkout session
    @Override
    public String processUserPayment(UUID groupId, String keycloakUserId, BigDecimal amount) {
        System.out.printf("💳 [StripeMock] User %s paid %.2f to group %s%n",
                keycloakUserId, amount, groupId);
        // Return a fake Stripe payment ID
        return "pi_" + UUID.randomUUID();
    }

    // Simulate payout to recipient member
    @Override
    public String processPayout(Payout payout, Membership recipient) {
        System.out.printf("🏦 [StripeMock] Payout %.2f to recipient %s (Stripe Account: %s)%n",
                payout.getAmount(),
                recipient.getKeycloakUserId(),
                recipient.getStripeAccountId() != null ? recipient.getStripeAccountId() : "N/A");

        payout.setPaid(true);
        payout.setPaidAt(Instant.now());

        // Return fake payout ID
        return "po_" + UUID.randomUUID();
    }
}
