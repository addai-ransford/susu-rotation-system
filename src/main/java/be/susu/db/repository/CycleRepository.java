package be.susu.db.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import be.susu.db.entity.Cycle;
import jakarta.persistence.LockModeType;

@Repository
public interface CycleRepository extends JpaRepository<Cycle, UUID> {

    Optional<Cycle> findTopByGroupIdOrderByCycleNoDesc(UUID groupId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Cycle c where c.group.id = :groupId and c.cycleNo = :cycleNo")
    Optional<Cycle> findByGroupIdAndCycleNoForUpdate(@Param("groupId") UUID groupId, @Param("cycleNo") Integer cycleNo);

    Optional<Cycle> findByGroupIdAndCycleNo(UUID groupId, Integer cycleNo);
}
