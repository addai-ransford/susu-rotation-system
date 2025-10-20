package be.susu.db.repository;

import be.susu.db.entity.SusuGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;

public interface SusuGroupRepository extends JpaRepository<SusuGroup, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT g FROM SusuGroup g WHERE g.id = :id")
    Optional<SusuGroup> findByIdForUpdate(@Param("id") UUID id);

    Optional<SusuGroup> findByAdminKeycloakId(String adminKeycloakId);
}
