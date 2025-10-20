package be.susu.service;

import java.math.BigDecimal;
import java.util.UUID;

import be.susu.db.entity.Membership;
import be.susu.db.entity.Payout;

public interface PaymentService {

     public String processUserPayment(UUID groupId, String keycloakUserId, BigDecimal amount);
     public String processPayout(Payout payout, Membership recipient);

}
