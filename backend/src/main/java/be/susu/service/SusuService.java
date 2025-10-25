package be.susu.service;


import java.math.BigDecimal;
import java.util.UUID;
import java.util.List;

import be.susu.db.entity.Contribution;
import be.susu.db.entity.Membership;
import be.susu.db.entity.SusuGroup;
import be.susu.web.dto.CreateGroupDto;

public interface SusuService {
    Contribution contributeToGroup(UUID groupId, String keycloakUserId, BigDecimal amount);

    Membership addMemberToGroup(UUID groupId, String keycloakUserId);

    List<Membership> getMembersForGroup(UUID groupId);

    SusuGroup createGroup(CreateGroupDto dto, String adminKeycloakId);

}
