package be.susu.service;

import java.util.UUID;

import be.susu.db.entity.Contribution;
import be.susu.web.dto.ContributionRequest;

public interface SusuService {
    Contribution contributeToGroup(UUID groupId, String keycloakUserId, ContributionRequest req);
}
