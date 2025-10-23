package be.susu.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.susu.db.entity.Contribution;

import java.util.UUID;

@Repository
public interface ContributionRepository extends JpaRepository<Contribution, java.util.UUID> {
    long countByGroupIdAndCycleNo(UUID groupId, Integer cycleNo);

}
