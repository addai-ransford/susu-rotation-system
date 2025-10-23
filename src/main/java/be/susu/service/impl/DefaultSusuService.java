package be.susu.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import be.susu.util.Frequency;
import be.susu.web.dto.CreateGroupDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import be.susu.db.entity.Contribution;
import be.susu.db.entity.Membership;
import be.susu.db.entity.SusuGroup;
import be.susu.db.repository.MembershipRepository;
import be.susu.db.repository.SusuGroupRepository;
import be.susu.service.RotationService;
import be.susu.service.SusuService;
import be.susu.web.dto.ContributionRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultSusuService implements SusuService {

    private static final Logger log = LoggerFactory.getLogger(DefaultSusuService.class);

    private final SusuGroupRepository groupRepo;
    private final MembershipRepository membershipRepo;
    private final RotationService rotationService;

    @Transactional
    @Override
    public be.susu.db.entity.SusuGroup createGroup(CreateGroupDto dto, String adminKeycloakId) {
        SusuGroup g = SusuGroup.builder()
                .name(dto.getName())
                .contributionAmount(new BigDecimal(dto.getContributionAmount()))
                .maxMembers(dto.getMaxMembers())
                .frequency(Frequency.valueOf(dto.getFrequency()))
                .adminKeycloakId(adminKeycloakId)
                .build();
        return groupRepo.save(g);
    }

    @Transactional
    @Override
    public Contribution contributeToGroup(UUID groupId, String keycloakUserId, BigDecimal amount) {
        SusuGroup group = groupRepo.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        log.info("User {} contributing {} to group {}", keycloakUserId, amount, group.getName());
        Contribution contribution = rotationService.contributeToCycle(group, keycloakUserId, amount);
        log.info("Contribution recorded: {}", contribution.getId());
        return contribution;
    }

    @Transactional
    @Override
    public Membership addMemberToGroup(UUID groupId, String keycloakUserId) {
        SusuGroup group = groupRepo.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        return membershipRepo.findByGroupIdAndKeycloakUserId(groupId, keycloakUserId)
                .orElseGet(() -> {
                    int maxJoinOrder = membershipRepo.findMaxJoinOrder(groupId);
                    Membership membership = Membership.builder()
                            .group(group)
                            .keycloakUserId(keycloakUserId)
                            .joinOrder(maxJoinOrder == 0 ? 1 : maxJoinOrder + 1)
                            .active(true)
                            .build();
                    membershipRepo.save(membership);
                    log.info("User {} joined group {}", keycloakUserId, group.getName());
                    return membership;
                });
    }

    @Override
    public List<Membership> getMembersForGroup(UUID groupId) {
        return membershipRepo.findByGroupIdOrderByJoinOrder(groupId);
    }
}
