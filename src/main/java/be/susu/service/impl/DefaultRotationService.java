package be.susu.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.susu.db.entity.Contribution;
import be.susu.db.entity.Cycle;
import be.susu.db.entity.CycleStatus;
import be.susu.db.entity.Membership;
import be.susu.db.entity.Payout;
import be.susu.db.entity.SusuGroup;
import be.susu.db.repository.ContributionRepository;
import be.susu.db.repository.CycleRepository;
import be.susu.db.repository.MembershipRepository;
import be.susu.db.repository.PayoutRepository;
import be.susu.service.PaymentService;
import be.susu.service.RotationService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultRotationService implements RotationService {

    private final MembershipRepository membershipRepo;
    private final ContributionRepository contributionRepo;
    private final PayoutRepository payoutRepo;
    private final CycleRepository cycleRepo;
    private final PaymentService paymentService;

    @Transactional
    @Override
    public Cycle ensureOpenCycle(SusuGroup group) {
        Cycle latest = cycleRepo
                .findTopByGroupOrderByCycleNoDesc(group)
                .orElse(null);
        if (latest != null && latest.getStatus() == CycleStatus.OPEN) {
            return latest;
        }

        int nextCycleNo = (latest == null) ? 1 : latest.getCycleNo() + 1;
        List<Membership> members = membershipRepo
                .findByGroupOrderByJoinOrder(group)
                .stream()
                .filter(Membership::isActive)
                .toList();

        if (members.isEmpty()) {
            throw new IllegalStateException("No active members found.");
        }

        int idx = (nextCycleNo - 1) % members.size();
        Membership recipient = members.get(idx);

        Cycle c = Cycle.builder()
                .group(group)
                .cycleNo(nextCycleNo)
                .recipient(recipient)
                .status(CycleStatus.OPEN)
                .createdAt(Instant.now())
                .build();
        return cycleRepo.save(c);
    }

    @Transactional
    @Override
    public Contribution contributeToCycle(SusuGroup group, String keycloakUserId, BigDecimal amount) {
        Cycle cycle = ensureOpenCycle(group);
        Membership member = membershipRepo.findByGroupAndKeycloakUserId(group, keycloakUserId)
                .orElseThrow(() -> new IllegalArgumentException("Membership not found or inactive."));

        if (!member.isActive()) {
            throw new IllegalStateException("Membership inactive.");
        }
        if (group.getContributionAmount().compareTo(amount) != 0) {
            throw new IllegalArgumentException("Invalid contribution amount.");
        }

        paymentService.processUserPayment(group.getId(), keycloakUserId, amount);

        Contribution contribution = Contribution.builder()
                .group(group)
                .membership(member)
                .cycleNo(cycle.getCycleNo())
                .amount(amount)
                .createdAt(Instant.now())
                .build();
        contributionRepo.save(contribution);

        long count = contributionRepo.countByGroupAndCycleNo(group, cycle.getCycleNo());
        if (count >= group.getMaxMembers()) {
            BigDecimal total = group.getContributionAmount().multiply(BigDecimal.valueOf(group.getMaxMembers()));
            Payout payout = payoutRepo.findByGroupAndCycleNoAndPaidFalse(group, cycle.getCycleNo())
                    .orElseGet(() -> payoutRepo.save(
                    Payout.builder()
                            .group(group)
                            .cycleNo(cycle.getCycleNo())
                            .membership(cycle.getRecipient())
                            .amount(total)
                            .build())
                    );
            paymentService.processPayout(payout, cycle.getRecipient());

            cycle.setStatus(CycleStatus.COMPLETED);
            cycle.setCompletedAt(Instant.now());
            cycleRepo.save(cycle);
        }

        return contribution;
    }
}
