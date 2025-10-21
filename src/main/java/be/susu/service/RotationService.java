package be.susu.service;

import java.math.BigDecimal;

import org.springframework.transaction.annotation.Transactional;

import be.susu.db.entity.Contribution;
import be.susu.db.entity.Cycle;
import be.susu.db.entity.SusuGroup;

public interface RotationService {

    @Transactional
    public Cycle ensureOpenCycle(SusuGroup group);

    @Transactional
    public Contribution contributeToCycle(SusuGroup group, String keycloakUserId, BigDecimal amount);
}
