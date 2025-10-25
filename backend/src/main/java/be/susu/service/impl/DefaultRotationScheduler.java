package be.susu.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import be.susu.db.entity.Cycle;
import be.susu.db.entity.CycleStatus;
import be.susu.db.entity.Membership;
import be.susu.db.repository.CycleRepository;
import be.susu.db.repository.MembershipRepository;
import be.susu.util.Frequency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Scheduler that runs daily to monitor Susu cycles.
 * Cancels expired cycles and schedules the next one automatically.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultRotationScheduler {

    private final CycleRepository cycleRepository;
    private final MembershipRepository membershipRepository;

    /**
     * Runs every day at 3 AM.
     * Cancels expired OPEN cycles and creates the next one.
     */
    @Transactional
    @Scheduled(cron = "0 0 3 * * *")
    public void monitorAndRotateCycles() {
        List<Cycle> openCycles = findOpenCycles();

        for (Cycle cycle : openCycles) {
            if (!isCycleValid(cycle)) continue;

            if (isCycleExpired(cycle)) {
                handleExpiredCycle(cycle);
            } else {
                logCycleStatus(cycle);
            }
        }
    }

    // ----------------- Helper Functions -----------------

    /**
     * Find all open cycles from repository.
     */
    private List<Cycle> findOpenCycles() {
        return cycleRepository.findAll()
                .stream()
                .filter(c -> c.getStatus() == CycleStatus.OPEN)
                .toList();
    }

    /**
     * Validate cycle data before processing.
     */
    private boolean isCycleValid(Cycle cycle) {
        if (cycle.getGroup() == null) {
            log.warn("Skipping cycle {} — missing group reference", cycle.getId());
            return false;
        }

        if (cycle.getGroup().getFrequency() == null) {
            log.warn("Skipping cycle {} — group {} missing frequency",
                    cycle.getId(), cycle.getGroup().getId());
            return false;
        }
        return true;
    }

    /**
     * Determine if the cycle has expired based on frequency.
     */
    private boolean isCycleExpired(Cycle cycle) {
        Duration allowedDuration = getAllowedDuration(cycle.getGroup().getFrequency());
        Instant expiryTime = cycle.getCreatedAt().plus(allowedDuration);
        return Instant.now().isAfter(expiryTime);
    }

    /**
     * Handle cycle expiration: cancel and create next.
     */
    private void handleExpiredCycle(Cycle cycle) {
        cancelCycle(cycle);
        createNextCycle(cycle);
    }

    /**
     * Cancel an expired cycle and persist update.
     */
    private void cancelCycle(Cycle cycle) {
        cycle.setStatus(CycleStatus.CANCELLED);
        cycle.setCompletedAt(Instant.now());
        cycleRepository.save(cycle);

        log.warn("⏰ Cycle cancelled: groupId={}, cycleNo={}, frequency={}",
                cycle.getGroup().getId(),
                cycle.getCycleNo(),
                cycle.getGroup().getFrequency());
    }

    /**
     * Create the next rotation cycle for the group.
     */
    private void createNextCycle(Cycle previousCycle) {
        var group = previousCycle.getGroup();
        List<Membership> members = membershipRepository.findByGroupIdOrderByJoinOrder(group.getId());

        if (members.isEmpty()) {
            log.error("❌ Cannot create next cycle — no members in group {}", group.getId());
            return;
        }

        Membership nextRecipient = getNextRecipient(previousCycle, members);

        Cycle nextCycle = Cycle.builder()
                .group(group)
                .cycleNo(previousCycle.getCycleNo() + 1)
                .recipient(nextRecipient)
                .status(CycleStatus.OPEN)
                .createdAt(Instant.now())
                .scheduledAt(Instant.now())
                .build();

        cycleRepository.save(nextCycle);
        log.info("🔁 Next cycle created: groupId={}, cycleNo={}, recipient={}",
                group.getId(), nextCycle.getCycleNo(), nextRecipient.getKeycloakUserId());
    }

    /**
     * Rotate and determine who the next recipient will be.
     */
    private Membership getNextRecipient(Cycle previousCycle, List<Membership> members) {
        int currentIndex = members.indexOf(previousCycle.getRecipient());
        int nextIndex = (currentIndex + 1) % members.size();
        return members.get(nextIndex);
    }

    /**
     * Log active cycles that are still valid.
     */
    private void logCycleStatus(Cycle cycle) {
        Duration allowedDuration = getAllowedDuration(cycle.getGroup().getFrequency());
        Instant expiryTime = cycle.getCreatedAt().plus(allowedDuration);
        log.info("✅ Cycle still active: groupId={}, cycleNo={}, expiresAt={}",
                cycle.getGroup().getId(), cycle.getCycleNo(), expiryTime);
    }

    /**
     * Map frequency to its duration.
     */
    private Duration getAllowedDuration(Frequency frequency) {
        return switch (frequency) {
            case DAILY -> Duration.ofDays(1);
            case WEEKLY -> Duration.ofDays(7);
            case BIWEEKLY -> Duration.ofDays(14);
            case MONTHLY -> Duration.ofDays(30);
        };
    }
}
