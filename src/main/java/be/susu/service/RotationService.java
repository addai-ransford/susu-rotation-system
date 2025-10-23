package be.susu.service;

import java.math.BigDecimal;

import be.susu.db.entity.Contribution;
import be.susu.db.entity.Cycle;
import be.susu.db.entity.SusuGroup;

public interface RotationService {

    Cycle ensureOpenCycle(SusuGroup group);

    Contribution contributeToCycle(SusuGroup group, String keycloakUserId, BigDecimal amount);
}
