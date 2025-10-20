package be.susu.db.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import be.susu.db.entity.Cycle;
import be.susu.db.entity.SusuGroup;
import jakarta.persistence.LockModeType;

public interface CycleRepository extends JpaRepository<Cycle, UUID> {

    Optional<Cycle> findTopByGroupOrderByCycleNoDesc(SusuGroup group);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Cycle c WHERE c.group = :group AND c.cycleNo = :cycleNo")
    Optional<Cycle> findByGroupAndCycleNoForUpdate(@Param("group") SusuGroup group,
                                                   @Param("cycleNo") Integer cycleNo);

    Optional<Cycle> findByGroupAndCycleNo(SusuGroup group, Integer cycleNo);
}
