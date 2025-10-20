package be.susu.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.susu.db.entity.Contribution;
import be.susu.db.entity.SusuGroup;

@Repository
public interface ContributionRepository extends JpaRepository<Contribution, java.util.UUID> {
    long countByGroupAndCycleNo(SusuGroup group, Integer cycleNo);
}
