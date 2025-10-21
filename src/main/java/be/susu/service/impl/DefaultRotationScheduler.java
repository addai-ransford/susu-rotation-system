package be.susu.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import be.susu.db.entity.Cycle;
import be.susu.db.entity.CycleStatus;
import be.susu.db.repository.CycleRepository;
import be.susu.util.Frequency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Scheduler that runs daily to monitor Susu cycles.
 * Cancels OPEN cycles that exceed their group's frequency duration.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultRotationScheduler {

    private final CycleRepository cycleRepository;

    /**
     * Runs every day at 3 AM.
     * Cancels OPEN cycles that have exceeded their allowed frequency duration.
     */
    @Transactional
    @Scheduled(cron = "0 0 3 * * *")
    public void scanAndCancelStaleCycles() {
        List<Cycle> openCycles = cycleRepository.findAll()
                .stream()
                .filter(c -> c.getStatus() == CycleStatus.OPEN)
                .toList();

        for (Cycle cycle : openCycles) {
            if (cycle.getGroup() == null || cycle.getGroup().getFrequency() == null) {
                log.warn("Skipping cycle {} — group or frequency missing", cycle.getId());
                continue;
            }

            Frequency frequency = cycle.getGroup().getFrequency();
            Duration allowedDuration = getAllowedDuration(frequency);
            Instant expiryTime = cycle.getCreatedAt().plus(allowedDuration);

            if (Instant.now().isAfter(expiryTime)) {
                cycle.setStatus(CycleStatus.CANCELLED);
                cycleRepository.save(cycle);
                log.warn("⏰ Cancelled stale cycle: groupId={}, cycleNo={}, frequency={}",
                        cycle.getGroup().getId(), cycle.getCycleNo(), frequency);
            } else {
                log.info("Cycle still active: groupId={}, cycleNo={}, frequency={}, expiresAt={}",
                        cycle.getGroup().getId(), cycle.getCycleNo(), frequency, expiryTime);
            }
        }
    }

    /**
     * Returns duration based on the group's contribution frequency.
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
