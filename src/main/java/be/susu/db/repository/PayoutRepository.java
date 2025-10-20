package be.susu.db.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.susu.db.entity.Payout;
import be.susu.db.entity.SusuGroup;

@Repository
public interface PayoutRepository extends JpaRepository<Payout, UUID> {
    Optional<Payout> findByGroupAndCycleNoAndPaidFalse(SusuGroup group, Integer cycleNo);
}
