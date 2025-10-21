package be.susu.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import be.susu.db.entity.Contribution;
import be.susu.db.entity.SusuGroup;
import be.susu.db.repository.SusuGroupRepository;
import be.susu.service.RotationService;
import be.susu.service.SusuService;
import be.susu.web.dto.ContributionRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultSusuService implements SusuService {
 private final SusuGroupRepository groupRepo;
    private final RotationService rotationService;

    @Transactional
    @Override
    public Contribution contributeToGroup(UUID groupId, String keycloakUserId, ContributionRequest req) {
        SusuGroup group = groupRepo.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        return rotationService.contributeToCycle(group, keycloakUserId, req.getAmount());
    }
}
